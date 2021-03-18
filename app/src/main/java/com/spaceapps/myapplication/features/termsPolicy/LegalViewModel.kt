package com.spaceapps.myapplication.features.termsPolicy

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.repositories.StaticContentRepository
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LegalViewModel @Inject constructor(
    private val repository: StaticContentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val type = savedStateHandle.get<LegalType>("legalType")
        ?: throw IllegalArgumentException("Legal type can not be null")

    val content = savedStateHandle.getLiveData<String>("legalContent")

    init {
        getLegalContent()
    }

    private fun getLegalContent() = async {
        request {
            when (type) {
                LegalType.TermsOfUse -> repository.getTermsOfUse()
                LegalType.PrivacyPolicy -> repository.getPrivacyPolicy()
            }
        }.onSuccess {
            content.postValue(it)
        }
    }
}
