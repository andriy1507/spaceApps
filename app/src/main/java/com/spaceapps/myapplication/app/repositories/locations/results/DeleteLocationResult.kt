package com.spaceapps.myapplication.app.repositories.locations.results

sealed class DeleteLocationResult {
    object Success : DeleteLocationResult()
    class Error(val exception: Exception) : DeleteLocationResult()
}
