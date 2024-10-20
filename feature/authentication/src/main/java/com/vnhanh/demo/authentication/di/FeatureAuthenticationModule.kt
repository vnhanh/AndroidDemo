package com.vnhanh.demo.authentication.di

import com.vnhanh.demo.authentication.domain.AuthValidatorImpl
import com.vnhanh.demo.authentication.domain.IAuthValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object FeatureAuthenticationModule {
    @Provides
    fun provideAuthValidator(): IAuthValidator = AuthValidatorImpl()
}
