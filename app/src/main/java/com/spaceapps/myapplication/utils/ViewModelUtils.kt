package com.spaceapps.myapplication.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

inline fun ViewModel.async(
    context: CoroutineContext = Dispatchers.IO,
    crossinline block: suspend () -> Unit
) = viewModelScope.launch(context) { block() }
