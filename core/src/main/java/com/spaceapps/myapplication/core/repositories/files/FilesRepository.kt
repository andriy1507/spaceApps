package com.spaceapps.myapplication.core.repositories.files

import com.spaceapps.myapplication.core.repositories.files.results.DeleteFileResult
import com.spaceapps.myapplication.core.repositories.files.results.DownloadFileResult
import com.spaceapps.myapplication.core.repositories.files.results.UploadFileResult

interface FilesRepository {

    suspend fun getFiles()

    suspend fun uploadFile(): UploadFileResult

    suspend fun downloadFile(fileName: String): DownloadFileResult

    suspend fun deleteFile(fileName: String): DeleteFileResult
}
