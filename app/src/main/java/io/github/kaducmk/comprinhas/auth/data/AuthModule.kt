package io.github.kaducmk.comprinhas.auth.data

import android.content.Context
import io.github.kaducmk.comprinhas.core.data.UsuarioService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {

    @Provides
    fun provideAuthService(
        usuarioService: UsuarioService,
        @ActivityContext context: Context
    ): AuthService = AuthService(usuarioService, context)
}