package com.spaceapps.myapplication.core.repositories.signalr

import kotlinx.coroutines.flow.Flow

interface SignalrRepository {
    fun subscribeTest(): Flow<String>
}
