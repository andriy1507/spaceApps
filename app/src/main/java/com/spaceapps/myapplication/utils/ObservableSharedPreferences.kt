package com.spaceapps.myapplication.utils

import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
fun SharedPreferences.observeKey(key: String) = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changed ->
        if (key == changed) {
            offer(getString(key, null))
        }
    }
    offer(getString(key, null))
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose {
        unregisterOnSharedPreferenceChangeListener(listener)
    }
}