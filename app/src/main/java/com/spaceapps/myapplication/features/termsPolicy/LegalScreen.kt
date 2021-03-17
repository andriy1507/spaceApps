package com.spaceapps.myapplication.features.termsPolicy

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun LegalScreen(vm: LegalViewModel) {
    val content by vm.content.observeAsState()
    if (content == null) {
        CircularProgressIndicator()
    } else {
        Text(text = content.orEmpty())
    }
}