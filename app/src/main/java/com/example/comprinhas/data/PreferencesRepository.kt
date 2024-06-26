package com.example.comprinhas.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.comprinhas.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AppPreferences(
    val welcomeScreen: Boolean,
    val name: String,
    val lastChanged: Long,
)

class PreferencesRepository(
    private val preferencesDatastore: DataStore<Preferences>,
) {

    private object PreferencesKeys {
        val WELCOME_SCREEN = booleanPreferencesKey("welcome_screen")
        val USER_NAME = stringPreferencesKey("user_name")
        val LAST_CHANGED = longPreferencesKey("last_changed")
        val UI_STATE = intPreferencesKey("ui_state")
    }

    val preferencesFlow: Flow<AppPreferences> = preferencesDatastore.data.map { preferences ->
        val welcomeScreen = preferences[PreferencesKeys.WELCOME_SCREEN] ?: true
        val name = preferences[PreferencesKeys.USER_NAME] ?: ""
        val lastChanged = preferences[PreferencesKeys.LAST_CHANGED] ?: -1
        AppPreferences(welcomeScreen, name, lastChanged)
    }

    suspend fun updateUiState(state: UiState) {
        preferencesDatastore.edit{ it[PreferencesKeys.UI_STATE] = state.ordinal }
    }
    val uiState: Flow<UiState> = preferencesDatastore.data.map { preferences ->
        val e = preferences[PreferencesKeys.UI_STATE] ?: 1
        enumValues<UiState>().first { it.ordinal == e }
    }

    suspend fun updateUserPrefs(name: String) {
        preferencesDatastore.edit {preferences ->
            preferences[PreferencesKeys.WELCOME_SCREEN] = false
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }
}