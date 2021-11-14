package com.spaceapps.myapplication.core.repositories.files.results

sealed class DeleteFileResult {
    object Success : DeleteFileResult()
    object Failure : DeleteFileResult()
}
