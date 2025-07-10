package com.cairosquad.movio.di

import SearchHistoryDao
import SearchHistoryDataSourceImpl
import androidx.room.Room
import com.cairosquad.local.MovioDataBase
import com.cairosquad.local.cacheSearch.CacheDao
import com.cairosquad.local.cacheSearch.SearchCacheDataSourceImpl
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

    single<SearchHistoryDao> {
        get<MovioDataBase>().searchHistoryDao()
    }

    single<SearchCacheDataSource> {
        SearchCacheDataSourceImpl(get())
    }

    single<SearchHistoryDataSource> {
        SearchHistoryDataSourceImpl()
    }
}