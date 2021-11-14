package com.spaceapps.myapplication.core.repositories.files

import com.spaceapps.myapplication.core.network.calls.FilesCalls
import com.spaceapps.myapplication.core.repositories.files.results.DeleteFileResult
import com.spaceapps.myapplication.core.repositories.files.results.DownloadFileResult
import com.spaceapps.myapplication.core.repositories.files.results.UploadFileResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilesRepositoryImpl @Inject constructor(
    private val calls: FilesCalls
) : FilesRepository {

    override suspend fun getFiles() {
    }

    override suspend fun uploadFile(): UploadFileResult {
        return UploadFileResult.Failure
    }

    override suspend fun downloadFile(fileName: String): DownloadFileResult {
        return DownloadFileResult.Failure
    }

    override suspend fun deleteFile(fileName: String): DeleteFileResult {
        return DeleteFileResult.Failure
    }
}
