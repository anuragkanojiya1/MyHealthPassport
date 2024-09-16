package com.example.myhealthpassport.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myhealthpassport.Cloud.RetrofitInstanceCosmocloud
import com.example.myhealthpassport.Cloud.UploadRequestBody
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class FileViewModel : ViewModel() {

    private val _uploadState = MutableLiveData<String>()
    val uploadState: LiveData<String> get() = _uploadState

    private val _downloadState = MutableLiveData<String>()
    val downloadState: LiveData<String> get() = _downloadState

    private val _downloadUrl = MutableLiveData<String>()
    val downloadUrl: LiveData<String> get() = _downloadUrl

    fun uploadFile(file: File, environmentId: String, projectId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstanceCosmocloud.api.getPresignedUrl(
                    environmentId,
                    projectId,
                    UploadRequestBody(file.name, file.length())
                )

                val formFields = response.fields.mapValues {
                    RequestBody.create(MultipartBody.FORM, it.value)
                }

                val fileRequestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
                val filePart = MultipartBody.Part.createFormData("file", file.name, fileRequestBody)

                val uploadResponse = RetrofitInstanceCosmocloud.api.uploadFile(
                    response.url,
                    formFields,
                    filePart
                )

                if (uploadResponse.isSuccessful) {
                    _uploadState.value = "File uploaded successfully!"
                } else {
                    _uploadState.value = "File upload failed!"
                }
            } catch (e: Exception) {
                _uploadState.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun fetchDownloadUrl(fileName: String, environmentId: String, projectId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstanceCosmocloud.api.downloadFile(
                    environmentId,
                    projectId,
                    fileName
                )

                if (response.isSuccessful) {
                    _downloadUrl.value = response.body().toString()
                } else {
                    _downloadState.value = "Failed to fetch download URL!"
                }
            } catch (e: retrofit2.HttpException) {
                _downloadState.value = "Failed to fetch download URL: ${e.message()}"
            } catch (e: Exception) {
                _downloadState.value = "An error occurred: ${e.message}"
            }
        }
    }
}
