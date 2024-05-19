package com.example.comprinhas.ui.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.dataStore
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(
        AppPreferences(false, "", "", "",0)
    )

    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.welcomeScreen, it.name, it.listId, it.listPassword, it.lastChanged)
                }
            }

            return _appPreferences
        }

    fun updateUserPrefs(name: String, listId: String, listPassword: String) {
        runBlocking {
            preferencesRepository.updateUserPrefs(name, listId, listPassword)
        }
    }

    fun updateNameAndListId(name: String, listId: String) {
        viewModelScope.launch {
            preferencesRepository.updateNameAndListId(name, listId)
        }
    }
}