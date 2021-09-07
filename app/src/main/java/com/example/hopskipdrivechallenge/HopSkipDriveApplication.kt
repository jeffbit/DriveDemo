package com.example.hopskipdrivechallenge

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class HopSkipDriveApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //debug
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}