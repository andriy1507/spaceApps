package com.spaceapps.myapplication.repositories.legal

sealed class GetLegalResult {
    class Success(val content: String) : GetLegalResult()
    object Failure : GetLegalResult()
}
