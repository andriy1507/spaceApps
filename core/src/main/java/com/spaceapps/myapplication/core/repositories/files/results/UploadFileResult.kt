package com.spaceapps.myapplication.core.repositories.files.results

sealed class UploadFileResult {
    object Success : UploadFileResult()
    object Failure : UploadFileResult()
}
