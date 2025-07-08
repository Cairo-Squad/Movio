package com.cairosquad.movio

import android.app.Application
import com.cairosquad.movio.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MovioApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidContext(this@MovioApplication)
            modules(appModule)
        }
    }
}