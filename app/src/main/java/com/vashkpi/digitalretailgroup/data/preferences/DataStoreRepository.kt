package com.vashkpi.digitalretailgroup.data.preferences

import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.data.models.domain.DeviceInfo
import com.vashkpi.digitalretailgroup.data.models.domain.UserInfo
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.DATE_OF_BIRTH
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.DEVICE_ID
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.FCM_TOKEN
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.GENDER
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.MIDDLE_NAME
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.NAME
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.SAVED_DEVICE_INFO_DEVICE_ID
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.SAVED_DEVICE_INFO_OS
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.SAVED_DEVICE_INFO_TOKEN
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.SAVED_DEVICE_INFO_USER_ID
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.SURNAME
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.USER_ID
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository.PreferencesKeys.USER_PHONE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.IOException

//data class UserPreferences(
//    val username: String,
//    val remember: Boolean
//)

val Context.dataStore by preferencesDataStore(
    name = AppConstants.DATA_STORE_PREFERENCES_NAME
)

class DataStoreRepository(private val context: Context, private val dataStore: DataStore<Preferences>) {

    fun clearDataStore() {
        runBlocking {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    private fun <T> readValue(key: Preferences.Key<T>): T? = runBlocking {
        return@runBlocking try {
            dataStore.data.catch { exception ->
                if (exception is IOException) {
                    //Log.e(TAG, "Error reading preferences: ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preference -> preference[key] }.firstOrNull()
        }
        catch (t: Throwable) {
            null
        }
    }

    private fun <T> saveValue(key: Preferences.Key<T>, value: T) = runBlocking {
        dataStore.edit { preference ->
            preference[key] = value
        }
    }

    private object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("FCM_TOKEN")

        val USER_ID = stringPreferencesKey("USER_ID")
        val USER_PHONE = stringPreferencesKey("USER_PHONE")

        val DEVICE_ID = stringPreferencesKey("DEVICE_ID")

        val NAME = stringPreferencesKey("NAME")
        val MIDDLE_NAME = stringPreferencesKey("MIDDLE_NAME")
        val SURNAME = stringPreferencesKey("SURNAME")
        val DATE_OF_BIRTH = stringPreferencesKey("DATE_OF_BIRTH")
        val GENDER = stringPreferencesKey("GENDER")

        val SAVED_DEVICE_INFO_USER_ID = stringPreferencesKey("SAVED_DEVICE_INFO_USER_ID")
        val SAVED_DEVICE_INFO_TOKEN = stringPreferencesKey("SAVED_DEVICE_INFO_TOKEN")
        val SAVED_DEVICE_INFO_DEVICE_ID = stringPreferencesKey("SAVED_DEVICE_INFO_DEVICE_ID")
        val SAVED_DEVICE_INFO_OS = stringPreferencesKey("SAVED_DEVICE_INFO_OS")
    }

    init {
        //init crucial default values to not to headbutt if they are empty
        updateFcmTokenInternal()
    }

    /**
     * Update FCM token
     */
    private fun updateFcmTokenInternal() {
        FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { result ->
                    fcmToken = result.token
                }
            }
        }
    }

    var fcmToken: String
        get() {
            return readValue(FCM_TOKEN) ?: "not_obtained"
        }
        set(value) {
            saveValue(FCM_TOKEN, value)
        }

    var userId: String
        get() {
            return readValue(USER_ID) ?: ""
        }
        set(value) {
            saveValue(USER_ID, value)
        }

    var userPhone: String
        get() {
            return readValue(USER_PHONE) ?: ""
        }
        set(value) {
            saveValue(USER_PHONE, value)
        }

    private fun getDeviceIdInternal(): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "not_obtained"
        } catch (t: Throwable) {
            "not_obtained"
        }
    }

    var deviceId: String
        get() {
            return readValue(DEVICE_ID) ?: getDeviceIdInternal()
        }
        set(value) {
            saveValue(DEVICE_ID, value)
        }

    var userInfo: UserInfo
        get() {
            return UserInfo(
                readValue(NAME) ?: "",
                readValue(SURNAME) ?: "",
                readValue(MIDDLE_NAME) ?: "",
                readValue(DATE_OF_BIRTH) ?: "",
                readValue(GENDER) ?: ""
            )
        }
        set(value) {
            saveValue(NAME, value.name)
            saveValue(SURNAME, value.surname)
            saveValue(MIDDLE_NAME, value.middle_name)
            saveValue(DATE_OF_BIRTH, value.date_of_birth)
            saveValue(GENDER, value.gender)
        }

    var savedDeviceInfo: DeviceInfo
        get() {
            return DeviceInfo(
                readValue(SAVED_DEVICE_INFO_USER_ID) ?: "",
                readValue(SAVED_DEVICE_INFO_TOKEN) ?: "",
                readValue(SAVED_DEVICE_INFO_DEVICE_ID) ?: "",
                readValue(SAVED_DEVICE_INFO_OS) ?: ""
            )
        }
        set(value) {
            saveValue(SAVED_DEVICE_INFO_USER_ID, value.user_id)
            saveValue(SAVED_DEVICE_INFO_TOKEN, value.token)
            saveValue(SAVED_DEVICE_INFO_DEVICE_ID, value.device_id)
            saveValue(SAVED_DEVICE_INFO_OS, value.os)
        }

//    val getUserInfo: Flow<UserInfo> = dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                //Log.d("DataStoreRepository", exception.message.toString())
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preference ->
//            val name = preference[NAME] ?: ""
//            val surname = preference[SURNAME] ?: ""
//            val middleName = preference[MIDDLE_NAME] ?: ""
//            val dateOfBirth = preference[DATE_OF_BIRTH] ?: ""
//            val gender = preference[GENDER] ?: ""
//            UserInfo(name, surname, middleName, dateOfBirth, gender)
//        }
//
//    fun saveUserInfo(userInfo: UserInfo) {
//        runBlocking {
//            dataStore.edit { preference ->
//                preference[NAME] = userInfo.name
//                preference[SURNAME] = userInfo.surname
//                preference[MIDDLE_NAME] = userInfo.middle_name
//                preference[DATE_OF_BIRTH] = userInfo.date_of_birth
//                preference[GENDER] = userInfo.gender
//            }
//        }
//    }

//    fun saveFcmToken(fcmToken: String) {
//        runBlocking {
//            dataStore.edit { preference ->
//                preference[FCM_TOKEN] = fcmToken
//            }
//        }
//    }
//
//    fun getFcmToken(): Flow<String> {
//        return dataStore.data
//            .catch { exception ->
//                if (exception is IOException) {
//                    //Log.e(TAG, "Error reading preferences: ", exception)
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }
//            .map { preference ->
//                preference[FCM_TOKEN] ?: ""
//            }
//    }

//    fun saveUserId(userId: String) {
//        runBlocking {
//            dataStore.edit { preference ->
//                preference[USER_ID] = userId
//            }
//        }
//    }
//
//    fun getUserId(): Flow<String> {
//        return dataStore.data
//            .catch { exception ->
//                if (exception is IOException) {
//                    //Log.e(TAG, "Error reading preferences: ", exception)
//                    emit(emptyPreferences())
//                } else {
//                    throw exception
//                }
//            }
//            .map { preference ->
//                preference[USER_ID] ?: ""
//            }
//    }

//    val readFromDataStore : Flow<UserPreferences> = dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                //Log.d("DataStoreRepository", exception.message.toString())
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preference ->
//            val username = preference[USERNAME] ?: ""
//            val remember = preference[REMEMBER] ?: false
//            UserPreferences(username, remember)
//        }
//
//    suspend fun saveToDataStore(username: String, remember: Boolean) {
//        dataStore.edit { preference ->
//            preference[USERNAME] = username
//            preference[REMEMBER] = remember
//        }
//    }

//    suspend fun removeUsername() {
//        dataStore.edit { preference ->
//            preference.remove(USERNAME)
//        }
//    }
//
//    private object PreferencesKeys {
//        val USERNAME = stringPreferencesKey("username")
//        val REMEMBER = booleanPreferencesKey("remember")
//    }
//
//    val readFromDataStore : Flow<UserPreferences> = dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                //Log.d("DataStoreRepository", exception.message.toString())
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }
//        .map { preference ->
//            val username = preference[USERNAME] ?: ""
//            val remember = preference[REMEMBER] ?: false
//            UserPreferences(username, remember)
//        }
//
//    suspend fun saveToDataStore(username: String, remember: Boolean) {
//        dataStore.edit { preference ->
//            preference[USERNAME] = username
//            preference[REMEMBER] = remember
//        }
//    }
}