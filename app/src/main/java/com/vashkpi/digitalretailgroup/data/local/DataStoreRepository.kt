package com.vashkpi.digitalretailgroup.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository.PreferencesKeys.REMEMBER
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository.PreferencesKeys.USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserPreferences(
    val username: String,
    val remember: Boolean
)

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
        val USERNAME = stringPreferencesKey("username")
        val REMEMBER = booleanPreferencesKey("remember")
    }

    val readFromDataStore : Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                //Log.d("DataStoreRepository", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val username = preference[USERNAME] ?: ""
            val remember = preference[REMEMBER] ?: false
            UserPreferences(username, remember)
        }

    suspend fun saveToDataStore(username: String, remember: Boolean) {
        dataStore.edit { preference ->
            preference[USERNAME] = username
            preference[REMEMBER] = remember
        }
    }

    suspend fun removeUsername() {
        dataStore.edit { preference ->
            preference.remove(USERNAME)
        }
    }
}