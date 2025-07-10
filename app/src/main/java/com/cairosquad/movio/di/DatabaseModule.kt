package com.cairosquad.movio.di

import androidx.room.Room
import com.cairosquad.local.MovioDataBase
import com.cairosquad.local.cacheSearch.CacheDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), MovioDataBase::class.java, "MovioDataBase").build()
    }
    single<CacheDao> {
        get<MovioDataBase>().cacheDao()
    }
}