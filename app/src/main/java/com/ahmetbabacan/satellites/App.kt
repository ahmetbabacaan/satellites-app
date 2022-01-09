package com.ahmetbabacan.satellites

import android.app.Application
import com.skydoves.bindables.BindingManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        BindingManager.bind(BR::class)
    }
}