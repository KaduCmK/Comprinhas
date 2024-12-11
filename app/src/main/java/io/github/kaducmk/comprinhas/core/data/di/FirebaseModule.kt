package io.github.kaducmk.comprinhas.core.data.di

import android.content.Context
import io.github.kaducmk.comprinhas.core.data.UsuarioService
import io.github.kaducmk.comprinhas.home.data.di.ShoppingListService
import io.github.kaducmk.comprinhas.list.data.di.ShoppingItemService
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

    @Provides
    fun provideConnectivityService(@ApplicationContext context: Context): IConnectivityService =
        ConnectivityService(context)
}