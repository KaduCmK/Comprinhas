package com.example.comprinhas.core.data.di

import com.example.comprinhas.core.data.UsuarioService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideUsuarioService(): UsuarioService = UsuarioService()
}