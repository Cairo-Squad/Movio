package com.cairosquad.movio

import android.app.Application
import android.util.Log
import com.cairosquad.ui.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovioApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}