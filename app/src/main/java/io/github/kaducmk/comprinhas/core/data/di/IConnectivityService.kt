package io.github.kaducmk.comprinhas.core.data.di

import kotlinx.coroutines.flow.Flow

interface IConnectivityService {
    val isConnected: Flow<Boolean>
}