package com.vashkpi.digitalretailgroup

import android.provider.Settings

object AppConstants {
    const val DEVICE_TYPE = "phone"
    const val DEVICE_PLATFORM = "android"

    //const val DEVICE_ID = Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)

    //preferences
    const val DEFAULT_SHARED_PREFERENCES_NAME = "app_preferences"
    const val DATA_STORE_PREFERENCES_NAME = "app_data_store_preferences"

    //API
    //const val DEFAULT_API_BASE_URL = "http://192.168.88.66:8080/sbs/"
//    const val DEFAULT_API_BASE_URL = "http://109.226.211.209:59301/sbs/"
    const val DEFAULT_API_BASE_URL = "http://cp.vashkpi.ru:59301/sbs/hs/bss/v1/"

    //const val DEFAULT_API_BASE_URL = "http://akpoker.vashkpi.ru/sbs/"

    const val API_TIMEOUT_CONNECTION_SECONDS = 15L
    const val API_TIMEOUT_READ_SECONDS = 15L //
    const val API_TIMEOUT_WRITE_SECONDS = 15L
}

