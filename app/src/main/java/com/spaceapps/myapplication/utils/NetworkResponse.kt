package com.spaceapps.myapplication.utils

import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

inline fun <T> request(request: () -> T): NetworkResponse<T> {
    return try {
        Success(request())
    } catch (e: Exception) {
        Timber.e(e)
        Error(e)
    }
}

inline fun <T> tryOrNull(action: () -> T): T? {
    return try {
        action()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

sealed class NetworkResponse<out T> {

    inline infix fun onSuccess(action: (T) -> Unit): NetworkResponse<T> {
        if (this is Success) action(data)
        return this
    }

    inline infix fun onError(action: (Exception) -> Unit): NetworkResponse<T> {
        if (this is Error) action(error)
        return this
    }

    inline infix fun onNetworkError(action: (IOException) -> Unit) =
        onError { if (it is IOException) action(it) }

    inline infix fun onRequestError(action: (HttpException) -> Unit) =
        onError { if (it is HttpException) action(it) }
}

class Success<T>(val data: T) : NetworkResponse<T>()
class Error(val error: Exception) : NetworkResponse<Nothing>() {

    val errorMessage: String?
        get() = if (error is HttpException) error.response()?.errorBody()?.string() else null

    val statusCode: Int?
        get() = if (error is HttpException) error.response()?.code() else null

    val headers: Map<String, List<String>>?
        get() = if (error is HttpException) error.response()?.headers()?.toMultimap() else null
}
