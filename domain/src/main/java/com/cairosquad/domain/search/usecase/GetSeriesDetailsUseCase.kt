package com.cairosquad.domain.search.usecase

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Review
import com.cairosquad.entity.Series
import com.cairosquad.entity.Season

class GetSeriesDetailsUseCase {

    suspend fun getSeriesById(seriesId: Long): Series {
        return fakeSeries
    }

    suspend fun getReviewsBySeriesId(seriesId: Long): List<Review> {
        return listOf(fakeReview)
    }

    suspend fun getSeriesSeasonsBySeriesId(seriesId: Long): List<Season> {
        return listOf(fakeSeason)
    }

    suspend fun getEpisodesBySeriesIdAndSeasonNumber(seriesId: Long, seasonNumber: Int): List<Episode> {
        return listOf(fakeEpisode)
    }

    suspend fun getSimilarSeries(seriesId: Long): List<Series> {
        return listOf(fakeSeries)
    }

    suspend fun getTopCastBySeriesId(seriesId: Long): List<Artist> {
        return listOf(fakeArtist)
    }

    private companion object {

        val fakeSeries = Series(id = 1, title = "", rating = 0f, posterPath = "", genres = emptyList())

        val fakeArtist = Artist(id = 1, name = "", photoPath = "")

        val fakeReview = Review(id = 1, author = "", authorPhotoPath = "", rating = "", date = 0, description = "")

        val fakeSeason = Season(seasonNumber = 1, seasonName = "", seriesId = 1, episodesCount = 0, rating = 0f, posterPath = "", overview = "", airDate = 0)

        val fakeEpisode = Episode(id = 1, episodeNumber = 1, photoPath = "", episodeName = "", runtimeMinutes = 0, rating = 0f, seasonNumber = 1, seriesId = 1)
    }
}