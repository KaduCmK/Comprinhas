package com.example.comprinhas.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class AppPreferences(val name: String, val welcomeScreen: Boolean, val lastChanged: Long)

class PreferencesRepository(
    private val preferencesDatastore: DataStore<Preferences>,
    context: Context
) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val WELCOME_SCREEN = booleanPreferencesKey("welcome_screen")
        val LAST_CHANGED = longPreferencesKey("last_changed")
    }

    val preferencesFlow: Flow<AppPreferences> = preferencesDatastore.data.map { preferences ->
        val name = preferences[PreferencesKeys.USER_NAME] ?: ""
        val welcomeScreen = preferences[PreferencesKeys.WELCOME_SCREEN] ?: true
        val localLastChanged = preferences[PreferencesKeys.LAST_CHANGED] ?: -1
        AppPreferences(name, welcomeScreen, localLastChanged)
    }

    val lastChanged = preferencesDatastore.data.map {
        it[PreferencesKeys.LAST_CHANGED] ?: -1
    }

    suspend fun updateNameAndWelcomeScreen(name: String, welcomeScreen: Boolean) {
        preferencesDatastore.edit {preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.WELCOME_SCREEN] = welcomeScreen
        }
    }
}