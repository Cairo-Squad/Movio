package com.cairosquad.movio.di

import androidx.room.Room
import com.cairosquad.local.common.MovioDataBase
import com.cairosquad.local.search.cache.cacheDataSourceImpl
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.discovery.DiscoveryDataSourceImpl
import com.cairosquad.local.search.discovery.dao.DiscoveryDao
import com.cairosquad.local.search.recent.LocalRecentSearchDataSourceImpl
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.search.data_source.local.CacheDataSource
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataSourceModule = module {

    single {
        Room.databaseBuilder(androidContext(), MovioDataBase::class.java, "MovioDataBase").build()
    }
    single<CacheDao> {
        get<MovioDataBase>().cacheDao()
    }

    single<LocalRecentSearchDao> {
        get<MovioDataBase>().recentSearchDao()
    }

    single<DiscoveryDao> {
        get<MovioDataBase>().discoveryDao()
    }

    single<CacheDataSource> {
        cacheDataSourceImpl(get())
    }

    single<LocalRecentSearchDataSource> {
        LocalRecentSearchDataSourceImpl(get())
    }

    single<DiscoveryDataSource> {
        DiscoveryDataSourceImpl(get(), get())
    }
}