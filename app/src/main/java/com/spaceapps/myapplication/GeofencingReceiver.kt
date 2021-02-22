package com.spaceapps.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GeofencingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val longitude = intent.extras?.getDouble(LONGITUDE)
        val latitude = intent.extras?.getDouble(LATITUDE)
        Timber.d("Lon: $longitude; Lat: $latitude")
    }

    companion object {
        private const val LONGITUDE = "LONGITUDE"
        private const val LATITUDE = "LATITUDE"

        fun getIntent(context: Context, lon: Double, lat: Double) =
            Intent(context, GeofencingReceiver::class.java).apply {
                putExtra(LONGITUDE, lon)
                putExtra(LATITUDE, lat)
            }
    }
}
