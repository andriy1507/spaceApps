package com.spaceapps.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeofencingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        GeofencingEvent.fromIntent(intent)
    }
}
