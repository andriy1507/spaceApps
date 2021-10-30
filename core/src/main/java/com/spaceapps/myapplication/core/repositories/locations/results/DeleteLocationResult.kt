package com.spaceapps.myapplication.core.repositories.locations.results

sealed class DeleteLocationResult {
    object Success : DeleteLocationResult()
    class Error(val exception: Exception) : DeleteLocationResult()
}
