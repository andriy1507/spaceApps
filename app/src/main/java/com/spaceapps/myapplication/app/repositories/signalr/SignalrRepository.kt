package com.spaceapps.myapplication.app.repositories.signalr

import kotlinx.coroutines.flow.Flow

interface SignalrRepository {
    fun subscribeTest(): Flow<String>
}
