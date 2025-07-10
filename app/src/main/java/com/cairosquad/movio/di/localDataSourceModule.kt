package com.cairosquad.movio.di

import androidx.room.Room
import com.cairosquad.local.common.MovioDataBase
import com.cairosquad.local.search.cache.SearchCacheDataSourceImpl
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.recent.SearchHistoryDataSourceImpl
import com.cairosquad.local.search.recent.dao.RecentSearchDao
import com.cairosquad.repository.dataSource.local.SearchHistoryDataSource
import com.cairosquad.repository.search.dataSource.local.SearchCacheDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataSourceModule = module {

    single {
        Room.databaseBuilder(androidContext(), MovioDataBase::class.java, "MovioDataBase").build()
    }
    single<CacheDao> {
        get<MovioDataBase>().cacheDao()
    }

    single<RecentSearchDao> {
        get<MovioDataBase>().searchHistoryDao()
    }

    single<SearchCacheDataSource> {
        SearchCacheDataSourceImpl(get())
    }

    single<SearchHistoryDataSource> {
        SearchHistoryDataSourceImpl(get())
    }
}