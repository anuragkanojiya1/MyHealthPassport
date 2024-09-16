package com.example.myhealthpassport.Cloud

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import retrofit2.http.Url

interface ObjectStorageApi {

    @POST("upload")
    suspend fun getPresignedUrl(
        @Header("environmentId") environmentId: String,
        @Header("projectId") projectId: String,
        @Body requestBody: UploadRequestBody
    ): PresignedUrlResponse

    @Multipart
    @POST
    suspend fun uploadFile(
        @Url url: String,
        @PartMap formFields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    ): Response<Void>

    @GET("download")
    suspend fun downloadFile(
        @Header("environmentId") environmentId: String,
        @Header("projectId") projectId: String,
        @Query("name") filename: String
    ): Response<String> // Updated to return a String
}