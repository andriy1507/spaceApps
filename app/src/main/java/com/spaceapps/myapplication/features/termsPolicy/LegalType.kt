package com.spaceapps.myapplication.features.termsPolicy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class LegalType : Parcelable {
    TermsOfUse,
    PrivacyPolicy
}