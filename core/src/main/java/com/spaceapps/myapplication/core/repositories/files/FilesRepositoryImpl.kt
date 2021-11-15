package com.spaceapps.myapplication.core.repositories.files

import android.content.Context
import android.os.Environment
import com.spaceapps.myapplication.core.network.calls.FilesCalls
import com.spaceapps.myapplication.core.repositories.files.results.DeleteFileResult
import com.spaceapps.myapplication.core.repositories.files.results.DownloadFileResult
import com.spaceapps.myapplication.core.repositories.files.results.UploadFileResult
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilesRepositoryImpl @Inject constructor(
    private val calls: FilesCalls,
    private val dispatchersProvider: DispatchersProvider,
    context: Context
) : FilesRepository {

    private val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

    override suspend fun getFiles() = withContext(dispatchersProvider.IO) {
    }

    override suspend fun uploadFile(): UploadFileResult = withContext(dispatchersProvider.IO) {
        return@withContext UploadFileResult.Failure
    }

    override suspend fun downloadFile(fileName: String): DownloadFileResult =
        withContext(dispatchersProvider.IO) {
            return@withContext DownloadFileResult.Failure
        }

    override suspend fun deleteFile(fileName: String): DeleteFileResult =
        withContext(dispatchersProvider.IO) {
            return@withContext DeleteFileResult.Failure
        }
}
