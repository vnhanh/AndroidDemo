package com.vnhanh.network.di

import android.content.Context
import com.vnhanh.network.util.INetWorkChecker
import com.vnhanh.network.util.NetWorkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkChecker(
        @ApplicationContext context: Context
    ) : INetWorkChecker =
        NetWorkChecker(context = context)

}
