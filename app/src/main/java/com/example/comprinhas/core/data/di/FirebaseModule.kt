package com.example.comprinhas.core.data.di

import android.content.Context
import com.example.comprinhas.core.data.UsuarioService
import com.example.comprinhas.home.data.di.ShoppingListService
import com.example.comprinhas.list.data.di.ShoppingItemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideUsuarioService(): UsuarioService = UsuarioService()

    @Provides
    fun provideShoppingListService(): ShoppingListService =
        ShoppingListService()

    @Provides
    fun provideShoppingItemService(): ShoppingItemService =
        ShoppingItemService()

    @Provides
    fun provideQrCodeService(@ApplicationContext context: Context): QrCodeService =
        QrCodeService(context)
}