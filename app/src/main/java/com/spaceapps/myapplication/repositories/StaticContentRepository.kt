package com.spaceapps.myapplication.repositories

import com.spaceapps.myapplication.PRIVACY_POLICY
import com.spaceapps.myapplication.TERMS_OF_USE
import com.spaceapps.myapplication.local.StaticContentStorage
import com.spaceapps.myapplication.network.StaticContentApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StaticContentRepository @Inject constructor(
    private val api: StaticContentApi,
    private val storage: StaticContentStorage
) {

    suspend fun getTermsOfUse(): String {
        val terms = storage.getTermsOfUse()
        return if (terms != null) {
            terms
        } else {
            val response = api.getStaticContent(TERMS_OF_USE)
            storage.saveTermsOfUse(response.content)
            response.content
        }
    }

    suspend fun getPrivacyPolicy(): String {
        val policy = storage.getPrivacyPolicy()
        return if (policy != null) {
            policy
        } else {
            val response = api.getStaticContent(PRIVACY_POLICY)
            storage.savePrivacyPolicy(response.content)
            response.content
        }
    }

}