package com.cairosquad.movio.di

import androidx.room.Room
import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.movie.MoviesCacheDao
import com.cairosquad.local.cache.movie.MoviesLocalDataSourceImpl
import com.cairosquad.local.cache.request.RequestCachedDao
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.local.login.LocalAuthenticationDataSourceImpl
import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.local.search.recent.LocalRecentSearchDataSourceImpl
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.utils.MovioDataBase
import com.cairosquad.repository.login.data_source.local.LocalAuthenticationDataSource
import com.cairosquad.repository.movie.data_source.local.MoviesLocalDataSource
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataSourceModule = module {

    single {
        Room.databaseBuilder(androidContext(), MovioDataBase::class.java, "MovioDataBase").build()
    }

    single<LoginDao> {
        get<MovioDataBase>().loginDao()
    }

    single<MoviesCacheDao> {
        get<MovioDataBase>().moviesCacheDao()
    }

    single<GenreDao> {
        get<MovioDataBase>().genreDao()
    }

    single<RequestCachedDao> {
        get<MovioDataBase>().requestCachedDao()
    }

    single<ReviewDao> {
        get<MovioDataBase>().reviewDao()
    }

    single<LocalRecentSearchDao> {
        get<MovioDataBase>().recentSearchDao()
    }

    single<LocalAuthenticationDataSource> {
        LocalAuthenticationDataSourceImpl(get())
    }

    single<LocalRecentSearchDataSource> {
        LocalRecentSearchDataSourceImpl(get())
    }

    single<MoviesLocalDataSource> {
        MoviesLocalDataSourceImpl(get(), get(), get(), get())
    }
}