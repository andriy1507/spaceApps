package com.spaceapps.myapplication.repositories.legal

import com.spaceapps.myapplication.PRIVACY_POLICY
import com.spaceapps.myapplication.TERMS_OF_USE
import com.spaceapps.myapplication.local.LegalStorage
import com.spaceapps.myapplication.network.LegalApi
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LegalRepository @Inject constructor(
    private val api: LegalApi,
    private val storage: LegalStorage
) {

    suspend fun getTermsOfUse(): GetLegalResult {
        return when (val response = request { api.getStaticContent(TERMS_OF_USE) }) {
            is Success -> {
                storage.saveTermsOfUse(response.data.content)
                GetLegalResult.Success(response.data.content)
            }
            is Error -> {
                storage.getTermsOfUse()?.let { GetLegalResult.Success(it) }
                    ?: GetLegalResult.Failure
            }
        }
    }

    suspend fun getPrivacyPolicy(): GetLegalResult {
        return when (val response = request { api.getStaticContent(PRIVACY_POLICY) }) {
            is Success -> {
                storage.savePrivacyPolicy(response.data.content)
                GetLegalResult.Success(response.data.content)
            }
            is Error -> {
                storage.getPrivacyPolicy()?.let { GetLegalResult.Success(it) }
                    ?: GetLegalResult.Failure
            }
        }
    }
}
