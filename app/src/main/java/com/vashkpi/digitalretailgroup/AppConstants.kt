package com.vashkpi.digitalretailgroup

object AppConstants {
    const val DEVICE_TYPE = "phone"
    const val DEVICE_PLATFORM = "android"

    //preferences
    const val DEFAULT_SHARED_PREFERENCES_NAME = "app_preferences"
    const val DATA_STORE_PREFERENCES_NAME = "app_data_store_preferences"

    //API
    //const val DEFAULT_API_BASE_URL = "http://192.168.88.66:8080/sbs/"
    const val DEFAULT_API_BASE_URL = "http://109.226.211.209:59301/sbs/"
    //const val DEFAULT_API_BASE_URL = "http://akpoker.vashkpi.ru/sbs/"

    const val API_TIMEOUT_CONNECTION_SECONDS = 15L
    const val API_TIMEOUT_READ_SECONDS = 45L
    const val API_TIMEOUT_WRITE_SECONDS = 15L

    //other
    enum class GenderValues(val value: String) {
        MALE("мужской"),
        FEMALE("женский")
    }
}

