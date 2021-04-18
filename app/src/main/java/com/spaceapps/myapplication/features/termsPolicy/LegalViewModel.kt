package com.spaceapps.myapplication.features.termsPolicy

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.repositories.legal.LegalRepository
import com.spaceapps.myapplication.repositories.legal.GetLegalResult
import com.spaceapps.myapplication.utils.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LegalViewModel @Inject constructor(
    private val repository: LegalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val type = savedStateHandle.get<LegalType>("legalType")
        ?: throw IllegalArgumentException("Legal type can not be null")

    val content = savedStateHandle.getLiveData<String>("legalContent")

    init {
        getLegalContent()
    }

    private fun getLegalContent() = launch {
        val result = when (type) {
            LegalType.TermsOfUse -> repository.getTermsOfUse()
            LegalType.PrivacyPolicy -> repository.getPrivacyPolicy()
        }
        when (result) {
            is GetLegalResult.Success -> content.postValue(result.content)
            is GetLegalResult.Failure -> Unit
        }
    }
}
