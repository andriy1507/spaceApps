package com.spaceapps.myapplication.core.utils

inline val CharSequence?.isEmail: Boolean get() = isMatch(REGEX_EMAIL)

inline val CharSequence?.isPassword: Boolean get() = isMatch(REGEX_ADVANCED_PASSWORD)

inline val CharSequence?.isResetToken: Boolean get() = isMatch(REGEX_RESET_TOKEN)

fun CharSequence?.isMatch(regex: String): Boolean =
    !this.isNullOrEmpty() && Regex(regex).matches(this)

const val REGEX_PASSWORD = "^" +
    "(?=.*[0-9])" + // at least 1 digit
    "(?=.*[a-zA-Z])" + // any letter
    "(?=\\S+$)" + // no white spaces
    ".{8,}" + // at least 8 characters
    "$"
const val REGEX_ADVANCED_PASSWORD = "^" +
    "(?=.*\\d)" + // at least 1 digit
    "(?=.*[a-z\\u0430-\\u044F])" + // at least one lower-case latin/cyrillic letter
    "(?=.*[A-Z\\u0410-\\u042F])" + // at least one upper-case latin/cyrillic letter
    "(?=\\S+$)" + // no white-spaces
    "(?=.*[~!?@#$%^&*_\\-+()\\[\\]{}></\\\\|\"'.,:;])" + // at least on special symbol
    ".{8,128}" + // from 8 to 128 characters
    "$"

const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

const val REGEX_RESET_TOKEN = "^[0-9]{6,6}\$"
