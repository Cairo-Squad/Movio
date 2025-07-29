package com.cairosquad.movio

import android.app.Application
import com.cairosquad.movio.di.appModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class MovioApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(androidContext = this@MovioApplication)
            modules(appModule)
        }
    }
}