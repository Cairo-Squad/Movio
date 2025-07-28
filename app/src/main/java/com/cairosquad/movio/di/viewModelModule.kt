package com.cairosquad.movio.di

import com.cairosquad.viewmodel.auth_gate.AuthGate
import com.cairosquad.viewmodel.details.artist.ArtistViewModel
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsViewModel
import com.cairosquad.viewmodel.details.movie.MovieViewModel
import com.cairosquad.viewmodel.details.reviews.ReviewsViewModel
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel
import com.cairosquad.viewmodel.details.series.season.SeasonsViewModel
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesViewModel
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesViewModel
import com.cairosquad.viewmodel.details.top_cast.TopCastViewModel
import com.cairosquad.viewmodel.foryou.ForYouViewModel
import com.cairosquad.viewmodel.home.HomeViewModel
import com.cairosquad.viewmodel.login.LoginViewModel
import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.see_all.SeeAllViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::ForYouViewModel)
    viewModel { (seriesId: Long, seasonNumber: Int) ->
        EpisodesDetailsViewModel(
            manageSeriesUseCase = get(),
            seriesId = seriesId,
            seasonNumber = seasonNumber
        )
    }
    viewModelOf(::SimilarMoviesViewModel)
    viewModel { (movieId: Long) ->
        MovieViewModel(movieId = movieId, movieUseCase = get())
    }

	viewModel { (seriesId: Long) ->
		SeriesDetailsViewModel(
            manageSeriesUseCase = get(),
			loginUseCase = get(),
			seriesId = seriesId
		)
	}
	viewModel { (mediaId: Long, isMovie: Boolean) ->
		TopCastViewModel(
			mediaId = mediaId,
			isMovie = isMovie,
            manageMoviesUseCase = get(),
            manageSeriesUseCase = get()
		)
	}

    viewModel { (mediaId: Long, isMovie: Boolean) ->
        ReviewsViewModel(
            mediaId = mediaId,
            isMovie = isMovie,
            manageMoviesUseCase = get(),
            manageSeriesUseCase = get()
        )
    }

    viewModel { (artistId: Long) ->
        ArtistViewModel(manageArtistUseCase = get(), artistId = artistId)
    }

	viewModel { (seriesId: Long) ->
		SeriesDetailsViewModel(
            manageSeriesUseCase = get(),
			loginUseCase = get(),
			seriesId = seriesId
		)
	}

    viewModel { (seriesId: Long) ->
        SeasonsViewModel(
            manageSeriesUseCase = get(),
            seriesId = seriesId,
        )
    }

	viewModelOf(::SimilarSeriesViewModel)
	viewModelOf(::LoginViewModel)
	viewModelOf(::HomeViewModel)
	viewModelOf(::SeeAllViewModel)

	singleOf(::AuthGate)
}
