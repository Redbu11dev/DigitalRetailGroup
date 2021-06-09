package com.vashkpi.digitalretailgroup.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.vashkpi.digitalretailgroup.AppConstants

class PreferencesRepository(private val pref: SharedPreferences, private val gson: Gson) {

    private val editor = pref.edit()

    private fun String.putLongPreference(long: Long) {
        editor.putLong(this, long)
        editor.commit()
    }

    private fun String.putIntPreference(int: Int) {
        editor.putInt(this, int)
        editor.commit()
    }

    private fun String.putStringPreference(string: String) {
        editor.putString(this, string)
        editor.commit()
    }

    private fun String.putBooleanPreference(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }

    private fun String.getLongPreference() = pref.getLong(this, 0)

    private fun String.getIntPreference() = pref.getInt(this, 0)

    private fun String.getStringPreference() = pref.getString(this, "")!!

    private fun String.getBooleanPreference() = pref.getBoolean(this, false)

    companion object {
        const val PREF_API_URL = "PREF_API_URL"

        const val TOKEN = "TOKEN"
    }

    var apiUrl: String
        get() = PREF_API_URL.getStringPreference()
        set(value) = PREF_API_URL.putStringPreference(value)

    var token: String
        get() = TOKEN.getStringPreference()
        set(value) = TOKEN.putStringPreference(value)

    init {
        //init default values to not to headbutt if they are empty
        if (apiUrl.isEmpty()) {
            apiUrl = AppConstants.DEFAULT_API_BASE_URL
        }
    }

}