package com.spaceapps.myapplication.core.repositories.files.results

sealed class DownloadFileResult {
    object Success : DownloadFileResult()
    object Failure : DownloadFileResult()
}
