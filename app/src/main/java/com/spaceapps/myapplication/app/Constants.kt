package com.spaceapps.myapplication.app

import com.spaceapps.myapplication.BuildConfig

const val AUTH_HEADER = "Authorization"
const val AUTH_HEADER_PREFIX = "Bearer"
const val DATABASE_NAME = "space_apps_db"
const val PREFERENCES_DATA_STORE = "${BuildConfig.APPLICATION_ID}.PREFS_DATA_STORE"

const val MINUTES_IN_DEGREE = 60
const val SECONDS_IN_DEGREE = 3600
const val DEFAULT_MAP_ZOOM = 18f

const val INITIAL_PAGE = 1
const val PAGE_SIZE = 10

const val DEGREES_DMS = "DEGREES_DMS"
const val DEGREES_DECIMAL = "DEGREES_DECIMAL"

const val SYSTEM_UTM = "SYSTEM_UTM"
const val SYSTEM_S43 = "SYSTEM_S43"
const val SYSTEM_S63 = "SYSTEM_S63"
const val SYSTEM_GEO = "SYSTEM_GEO"

const val NOTIFICATION_TYPE = "type"
const val NOTIFICATION_NEW_LOGIN = "new_login"
const val NOTIFICATION_DEVICE_MANUFACTURER = "device_manufacturer"
const val NOTIFICATION_DEVICE_MODEL = "device_model"
const val NOTIFICATION_DEVICE_OS_VERSION = "device_os_version"

const val DEEP_LINK_URI = "https://spaceapps.com"
