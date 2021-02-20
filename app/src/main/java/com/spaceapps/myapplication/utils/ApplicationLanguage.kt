package com.spaceapps.myapplication.utils

import androidx.annotation.StringDef
import com.spaceapps.myapplication.ENGLISH
import com.spaceapps.myapplication.UKRAINIAN

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@StringDef(UKRAINIAN, ENGLISH, open = false)
annotation class ApplicationLanguage
