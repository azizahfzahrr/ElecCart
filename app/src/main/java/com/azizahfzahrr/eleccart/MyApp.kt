package com.azizahfzahrr.eleccart

import android.app.Application
import androidx.room.Room
import com.azizahfzahrr.eleccart.data.source.local.CartDatabase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}