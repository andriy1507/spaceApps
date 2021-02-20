package com.spaceapps.myapplication.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.spaceapps.myapplication.Settings
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override fun readFrom(input: InputStream): Settings {
        return try {
            Settings.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read Settings", e)
        }
    }

    override fun writeTo(t: Settings, output: OutputStream) {
        t.writeTo(output)
    }

    override val defaultValue: Settings
        get() = Settings.getDefaultInstance()
}