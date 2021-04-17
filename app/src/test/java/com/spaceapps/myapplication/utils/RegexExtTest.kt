package com.spaceapps.myapplication.utils

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

@SmallTest
class RegexExtTest {

    @Test
    fun `assert password is valid`() {
        assertThat(VALID_PASSWORD.isPassword).isTrue()
    }

    @Test
    fun `assert only letters password is invalid`() {
        assertThat(ONLY_LETTERS_PASSWORD.isPassword).isFalse()
    }

    @Test
    fun `assert only numbers password is invalid`() {
        assertThat(ONLY_NUMBERS_PASSWORD.isPassword).isFalse()
    }

    @Test
    fun `assert password of less than 8 characters is invalid`() {
        assertThat(TOO_SHORT_PASSWORD.isPassword).isFalse()
    }

    @Test
    fun `assert email is valid`() {
        assertThat(VALID_EMAIL.isEmail).isTrue()
    }

    @Test
    fun `assert email without @ is invalid`() {
        assertThat(EMAIL_WITHOUT_AT.isEmail).isFalse()
    }

    @Test
    fun `assert email without dot is invalid`() {
        assertThat(EMAIL_WITHOUT_DOT.isEmail).isFalse()
    }

    companion object {
        const val VALID_PASSWORD = "string123"
        const val ONLY_LETTERS_PASSWORD = "qwertyasd"
        const val ONLY_NUMBERS_PASSWORD = "123456789"
        const val TOO_SHORT_PASSWORD = "qwe123"
        const val VALID_EMAIL ="123asd@mail.com"
        const val EMAIL_WITHOUT_AT = "string.mail.com"
        const val EMAIL_WITHOUT_DOT = "string@mail"
    }
}