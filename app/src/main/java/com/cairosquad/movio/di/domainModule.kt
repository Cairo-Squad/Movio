package com.cairosquad.movio.di

import com.cairosquad.domain.usecase.artists.GetArtistDetailsUseCase
import com.cairosquad.domain.usecase.movies.GetAllMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetFreeToWatchMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMoreRecommendedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetMovieDetailsUseCase
import com.cairosquad.domain.usecase.movies.GetMoviesGenresUseCase
import com.cairosquad.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetPopularMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetSuggestedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetTrendingMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetUpcomingMoviesUseCase
import com.cairosquad.domain.usecase.search.ClearSearchHistoryUseCase
import com.cairosquad.domain.usecase.search.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.usecase.search.SearchUseCase
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
import com.cairosquad.domain.usecase.series.GetAllSeriesUseCase
import com.cairosquad.domain.usecase.series.GetMoreRecommendedSeriesUseCase
import com.cairosquad.domain.usecase.series.GetOnTvSeriesUseCase
import com.cairosquad.domain.usecase.series.GetPopularSeriesUseCase
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.domain.usecase.series.GetSeriesGenresUseCase
import com.cairosquad.domain.usecase.series.GetTopRatingSeriesUseCase
import com.cairosquad.domain.usecase.series.GetTrendingSeriesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SearchUseCase)
    singleOf(::GetPersonalizedMoviesUseCase)
    singleOf(::GetSuggestedMoviesUseCase)
    singleOf(::GetLocalSearchHistoryUseCase)
    singleOf(::ClearSearchHistoryUseCase)
    singleOf(::GetSeriesDetailsUseCase)
    singleOf(::GetArtistDetailsUseCase)
    singleOf(::GetMovieDetailsUseCase)

    // home
    singleOf(::GetFreeToWatchMoviesUseCase)
    singleOf(::GetMoreRecommendedMoviesUseCase)
    singleOf(::GetTopRatingMoviesUseCase)
    singleOf(::GetTrendingMoviesUseCase)
    singleOf(::GetUpcomingMoviesUseCase)
    singleOf(::GetNowPlayingMoviesUseCase)
    singleOf(::GetAiringTodaySeriesUseCase)
    singleOf(::GetMoreRecommendedSeriesUseCase)
    singleOf(::GetOnTvSeriesUseCase)
    singleOf(::GetTopRatingSeriesUseCase)
    singleOf(::GetPopularSeriesUseCase)
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::GetMoviesGenresUseCase)
    singleOf(::GetSeriesGenresUseCase)
    singleOf(::GetTrendingSeriesUseCase)
    singleOf(::GetAllMoviesUseCase)
    singleOf(::GetAllSeriesUseCase)

}