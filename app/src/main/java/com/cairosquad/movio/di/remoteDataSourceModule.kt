package com.cairosquad.movio.di

import com.cairosquad.remote.artists.ArtistsApiService
import com.cairosquad.remote.artists.RemoteArtistDataSourceImpl
import com.cairosquad.remote.movie.MovieApiService
import com.cairosquad.remote.movie.RemoteMovieDataSourceImpl
import com.cairosquad.remote.search.RemoteMovieDiscoveryDataSourceImpl
import com.cairosquad.remote.search.RemoteSearchDataSourceImpl
import com.cairosquad.remote.search.SearchApiService
import com.cairosquad.remote.series.RemoteSeriesDataSourceImpl
import com.cairosquad.remote.series.SeriesApiService
import com.cairosquad.remote.utils.HttpClientFactory
import com.cairosquad.remote.utils.HttpEngine
import com.cairosquad.remote.utils.retrofit.provideRetrofit
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.series.data_source.remote.RemoteSeriesDataSource
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDataSourceModule = module {

    single {
        HttpClientFactory.create(
            engine = HttpEngine.provide()
        )
    }
    single {
        provideRetrofit(
            tokenProvider = { null }
        )
    }


    single {
        provideRetrofit(
            tokenProvider = { null }
        )
    }

    single<SeriesApiService> {
        get<Retrofit>().create(SeriesApiService::class.java)
    }

    single<SearchApiService> {
        get<Retrofit>().create(SearchApiService::class.java)
    }

    single<RemoteSearchDataSource> {
        RemoteSearchDataSourceImpl(get())
    }

    single<RemoteMovieDiscoveryDataSource> {
        RemoteMovieDiscoveryDataSourceImpl(get())
    }

    single<ArtistsRemoteDataSource> {
        RemoteArtistDataSourceImpl(get())
    }

    single<RemoteMovieDataSource> {
        RemoteMovieDataSourceImpl(get())
    }

    single<RemoteSeriesDataSource> {
        RemoteSeriesDataSourceImpl(get())
    }

    single<ArtistsApiService> {
        get<Retrofit>().create(ArtistsApiService::class.java)
    }
    single<MovieApiService> {
        get<Retrofit>().create(MovieApiService::class.java)
    }
}