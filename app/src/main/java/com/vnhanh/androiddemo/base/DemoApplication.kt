package com.vnhanh.androiddemo.base

import android.app.Application
import com.vnhanh.androiddemo.BuildConfig
import com.vnhanh.common.log.AppDebugTree
import timber.log.Timber

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.LOGGABLE) {
            Timber.plant(AppDebugTree())
        }
    }
}
