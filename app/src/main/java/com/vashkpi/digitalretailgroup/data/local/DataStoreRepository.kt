package com.vashkpi.digitalretailgroup.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository.PreferencesKeys.AUTH_TOKEN
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository.PreferencesKeys.FCM_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

//data class UserPreferences(
//    val username: String,
//    val remember: Boolean
//)

val Context.dataStore by preferencesDataStore(
    name = AppConstants.DATA_STORE_PREFERENCES_NAME
)

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("FCM_TOKEN")
        val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
    }

    suspend fun saveFcmToken(fcmToken: String) {
        dataStore.edit { preference ->
            preference[FCM_TOKEN] = fcmToken
        }
    }

    fun getFcmToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    //Log.e(TAG, "Error reading preferences: ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                preference[FCM_TOKEN] ?: "" //TODO if empty - try obtaining the token in here(?) directly somehow
            }
    }

    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preference ->
            preference[AUTH_TOKEN] = authToken
        }
    }

    fun getAuthToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    //Log.e(TAG, "Error reading preferences: ", exception)
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                preference[AUTH_TOKEN] ?: ""
            }
    }

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