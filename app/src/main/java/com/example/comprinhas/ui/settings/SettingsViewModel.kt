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
import com.example.comprinhas.http.DatabaseApi
import com.example.comprinhas.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository =
        PreferencesRepository(application.dataStore)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow
    private val retrofitService = DatabaseApi.retrofitService

    private var _appPreferences by mutableStateOf(
        AppPreferences(false,  "",0)
    )

    val login = MutableStateFlow(false)

    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.welcomeScreen, it.name, it.lastChanged)
                }
            }

            return _appPreferences
        }

    fun updateUserPrefs(rawName: String) {
        val name = rawName.trim()

        viewModelScope.launch {
            preferencesRepository.updateUserPrefs(name)
        }
    }

    fun createList(rawUsername: String, rawListName: String, listPassword: String) {
        val username = rawUsername.trim()
        val listName = rawListName.trim()

        viewModelScope.launch {
            preferencesRepository.updateUiState(UiState.LOADING)
            val response = retrofitService.createList(username, listName, listPassword)

            if (response.code() == 200) {
                preferencesRepository.updateUiState(UiState.LOADED)
                preferencesRepository.updateUserPrefs(username)
                login.value = true
            }
            else {
                preferencesRepository.updateUiState(UiState.NO_CONNECTION)
            }
        }
    }
}