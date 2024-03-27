package com.example.comprinhas.data

import android.content.Context
import android.util.Log
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

data class AppPreferences(val name: String, val welcomeScreen: Boolean, val lastChanged: Long, val isLoading: Boolean)

class PreferencesRepository(
    private val preferencesDatastore: DataStore<Preferences>,
    context: Context
) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val WELCOME_SCREEN = booleanPreferencesKey("welcome_screen")
        val LAST_CHANGED = longPreferencesKey("last_changed")
        val IS_LOADING = booleanPreferencesKey("is_loading")
    }

    val preferencesFlow: Flow<AppPreferences> = preferencesDatastore.data.map { preferences ->
        val name = preferences[PreferencesKeys.USER_NAME] ?: ""
        val welcomeScreen = preferences[PreferencesKeys.WELCOME_SCREEN] ?: true
        val lastChanged = preferences[PreferencesKeys.LAST_CHANGED] ?: -1
        val isLoading = preferences[PreferencesKeys.IS_LOADING] ?: true
        AppPreferences(name, welcomeScreen, lastChanged, isLoading)
    }

    suspend fun updateLastChanged(id: Long) {
        Log.d("PREFERENCES", "localLastChanged alterado para $id")
        preferencesDatastore.edit {preferences ->
            preferences[PreferencesKeys.LAST_CHANGED] = id
        }
    }

    suspend fun updateLoadingScreen(isLoading: Boolean) {
        Log.d("PREFERENCES", "isLoading = $isLoading")
        preferencesDatastore.edit {preferences ->
            preferences[PreferencesKeys.IS_LOADING] = isLoading
        }
    }

    suspend fun updateNameAndWelcomeScreen(name: String, welcomeScreen: Boolean) {
        preferencesDatastore.edit {preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.WELCOME_SCREEN] = welcomeScreen
        }
    }
}