package com.spaceapps.myapplication

import com.spaceapps.myapplication.models.PushNotification
import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun moshiEnumTest() {
        val notification = "{\"type\": \"1\"}"
        val res = Moshi.Builder().build().adapter(PushNotification::class.java).fromJson(notification)
        assert(res?.type == PushNotification.PushNotificationType.MESSAGE)
    }
}