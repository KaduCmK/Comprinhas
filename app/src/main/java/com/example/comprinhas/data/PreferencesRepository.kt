package com.example.comprinhas.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

data class AppPreferences(
    val welcomeScreen: Boolean,
    val name: String,
    val listId: String,
    val lastChanged: Long,
    val isLoading: Boolean
)

class PreferencesRepository(
    private val preferencesDatastore: DataStore<Preferences>,
    context: Context
) {

    private object PreferencesKeys {
        val WELCOME_SCREEN = booleanPreferencesKey("welcome_screen")
        val USER_NAME = stringPreferencesKey("user_name")
        val LIST_ID = stringPreferencesKey("list_id")
        val LAST_CHANGED = longPreferencesKey("last_changed")
        val IS_LOADING = booleanPreferencesKey("is_loading")
    }

    val preferencesFlow: Flow<AppPreferences> = preferencesDatastore.data.map { preferences ->
        val welcomeScreen = preferences[PreferencesKeys.WELCOME_SCREEN] ?: true
        val name = preferences[PreferencesKeys.USER_NAME] ?: ""
        val listId = preferences[PreferencesKeys.LIST_ID] ?: ""
        val lastChanged = preferences[PreferencesKeys.LAST_CHANGED] ?: -1
        val isLoading = preferences[PreferencesKeys.IS_LOADING] ?: true
        AppPreferences(welcomeScreen, name, listId, lastChanged, isLoading)
    }

    suspend fun getNameAndListId(): Pair<String, String> {
        return preferencesDatastore.data.map {
            val name = it[PreferencesKeys.USER_NAME] ?: ""
            val listId = it[PreferencesKeys.LIST_ID] ?: ""
            name to listId
        }.first()
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

    suspend fun updateNameAndListId(name: String, listId: String) {
        preferencesDatastore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.LIST_ID] = listId
        }
    }

    suspend fun updateWelcomeScreen(name: String, listId: String) {
        preferencesDatastore.edit {preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.LIST_ID] = listId
            preferences[PreferencesKeys.WELCOME_SCREEN] = false
        }
    }
}