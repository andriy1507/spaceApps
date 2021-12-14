package com.spaceapps.myapplication.core.utils

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = MutableStateFlow(initialValue)

    val observer = Observer<T> { value ->
        if (value != stateFlow.value) {
            stateFlow.value = value
        }
    }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) {
                liveData.value = value
            }
        }
    }.launchIn(scope)

    return stateFlow
}

fun <T> SavedStateHandle.getValue(key: String): ReadWriteProperty<Any, T?> {
    return object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>) = get<T>(key)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) =
            set(key, value)
    }
}
