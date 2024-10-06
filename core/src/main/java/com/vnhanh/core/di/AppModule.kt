package com.vnhanh.core.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.vnhanh.core.network.INetWorkChecker
import com.vnhanh.core.network.NetWorkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNetworkChecker(
        @ApplicationContext context: Context
    ) : INetWorkChecker = NetWorkChecker(context = context)

    @Provides
    @Singleton
    fun provideSavedStateHandle() : SavedStateHandle = SavedStateHandle()
}
