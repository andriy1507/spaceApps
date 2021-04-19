package com.spaceapps.myapplication.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

inline fun <T> ViewModel.async(
    context: CoroutineContext = Dispatchers.IO,
    crossinline block: suspend () -> T
) = viewModelScope.async(context) { block() }

inline fun ViewModel.launch(
    context: CoroutineContext = Dispatchers.IO,
    crossinline block: suspend () -> Unit
) = viewModelScope.launch(context) { block() }
