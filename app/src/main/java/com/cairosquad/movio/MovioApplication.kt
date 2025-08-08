package com.cairosquad.movio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovioApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}