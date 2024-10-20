package com.vnhanh.authentication.di

import com.vnhanh.authentication.api.IAuthenticationApi
import com.vnhanh.authentication.data.AuthenticationRepository
import com.vnhanh.authentication.data.IAuthRepository
import com.vnhanh.network.util.IApiLauncher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationDataModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        api: IAuthenticationApi,
        apiLauncher: IApiLauncher,
    ) : IAuthRepository =
        AuthenticationRepository(
            api = api,
            launcher = apiLauncher,
        )
}
