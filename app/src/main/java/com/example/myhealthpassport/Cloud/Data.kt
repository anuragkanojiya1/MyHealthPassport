package com.example.myhealthpassport.Cloud

data class UploadRequestBody(
    val name: String,
    val size: Long
)

data class PresignedUrlResponse(
    val url: String,
    val fields: Map<String, String>
)

data class FileDownloadResponse(
    val downloadUrl: String
)