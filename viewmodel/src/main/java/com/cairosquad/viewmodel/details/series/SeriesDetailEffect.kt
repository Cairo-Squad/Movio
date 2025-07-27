package com.cairosquad.viewmodel.details.series

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class SeriesDetailEffect {

	data object NavigateBack : SeriesDetailEffect()
	data object PlayTrailer : SeriesDetailEffect()
	data class NavigateToArtistDetails(val artistId: Long) : SeriesDetailEffect()
	data class NavigateToAllArtists(val seriesId: Long) : SeriesDetailEffect()
	data class NavigateToSeasonDetails(val seriesId: Long, val seasonNumber: Int) :
			SeriesDetailEffect()

	data class NavigateToAllSeasons(val seriesId: Long) : SeriesDetailEffect()
	data class NavigateToAllReviews(val seriesId: Long) : SeriesDetailEffect()
	data class NavigateToSeriesDetails(val seriesId: Long) : SeriesDetailEffect()
	data class NavigateToAllSimilar(val seriesId: Long) : SeriesDetailEffect()

	data class ErrorHappened(val message: ErrorStatus) : SeriesDetailEffect()
	data object NavigateToLogin : SeriesDetailEffect()
}
