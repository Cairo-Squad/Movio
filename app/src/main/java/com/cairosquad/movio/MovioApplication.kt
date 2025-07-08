package com.cairosquad.movio

import android.app.Application
import com.cairosquad.movio.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovioApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(androidContext = this@MovioApplication)
            modules(appModule)
        }
    }
}