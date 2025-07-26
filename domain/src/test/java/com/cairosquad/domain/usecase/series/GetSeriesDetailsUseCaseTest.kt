package com.cairosquad.domain.usecase.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetSeriesDetailsUseCaseTest {

    private val seriesRepository: SeriesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetSeriesDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetSeriesDetailsUseCase(seriesRepository)
    }

    @Test
    fun `getSeries SHOULD return series from repository`() = runTest {
        coEvery { seriesRepository.getSeriesById(1L) } returns series1

        val result = useCase.getSeries(1L)

        assertThat(result).isEqualTo(series1)
        coVerify(exactly = 1) { seriesRepository.getSeriesById(1L) }
    }

    @Test
    fun `getSeriesReviews SHOULD return reviews from repository`() = runTest {
        coEvery { seriesRepository.getSeriesReviews(1L, 1) } returns listOf(review)

        val result = useCase.getSeriesReviews(1L, 1)

        assertThat(result).containsExactly(review)
        coVerify(exactly = 1) { seriesRepository.getSeriesReviews(1L, 1) }
    }

    @Test
    fun `getSeriesSeasons SHOULD return seasons from repository`() = runTest {
        coEvery { seriesRepository.getSeriesSeasons(1L) } returns listOf(season)

        val result = useCase.getSeriesSeasons(1L)

        assertThat(result).containsExactly(season)
        coVerify(exactly = 1) { seriesRepository.getSeriesSeasons(1L) }
    }

    @Test
    fun `getEpisodes SHOULD return episodes from repository`() = runTest {
        coEvery { seriesRepository.getEpisodes(1L, 1) } returns listOf(episode)

        val result = useCase.getEpisodes(1L, 1)

        assertThat(result).containsExactly(episode)
        coVerify(exactly = 1) { seriesRepository.getEpisodes(1L, 1) }
    }

    @Test
    fun `getSimilarSeries SHOULD return similar series from repository`() = runTest {
        coEvery { seriesRepository.getSimilarSeries(1L, 1) } returns listOf(series2)

        val result = useCase.getSimilarSeries(1L, 1)

        assertThat(result).containsExactly(series2)
        coVerify(exactly = 1) { seriesRepository.getSimilarSeries(1L, 1) }
    }

    @Test
    fun `getSeriesTopCast SHOULD return top cast from repository`() = runTest {
        coEvery { seriesRepository.getSeriesTopCast(1L, 1) } returns listOf(actor)

        val result = useCase.getSeriesTopCast(1L, 1)

        assertThat(result).containsExactly(actor)
        coVerify(exactly = 1) { seriesRepository.getSeriesTopCast(1L, 1) }
    }

    @Test
    fun `getSeries SHOULD throw exception when repository fails`() = runTest {
        coEvery { seriesRepository.getSeriesById(1L) } throws RuntimeException("Failed to fetch")

        assertThrows<RuntimeException> {
            useCase.getSeries(1L)
        }
    }

    private companion object {
        private val series1 = Series(
            id = 1,
            title = "series1",
            rating = 8f,
            posterPath = "/poster.png",
            trailerPath = "trailer",
            genres = listOf(Genre(id = 1L, "genre1"), Genre(id = 2L, "genre2")),
            overview = "overview",
            releaseDate = 321L,
            seasonsCount = 3,
        )
        private val series2 = Series(
            id = 2,
            title = "series2",
            rating = 7.6f,
            posterPath = "/poster.png",
            trailerPath = "trailer",
            genres = listOf(Genre(id = 1L, "genre1"), Genre(id = 2L, "genre2")),
            overview = "overview",
            releaseDate = 321L,
            seasonsCount = 3,
        )
        private val season = Season(
            seasonNumber = 1,
            seasonName = "season",
            seriesId = 1,
            episodesCount = 25,
            rating = 6.5f,
            posterPath = "/poster.png",
            overview = "overVIew",
            airDate = 321L
        )
        private val actor = Artist(
            id = 312,
            name = "Keanu",
            photoPath = "/keanu.jpeg",
            country = "Japan",
            birthDate = 1234L,
            biography = "bio graphy",
            department = "actor"
        )
        private val review = Review(
            id = 123.toString(),
            author = "Ana",
            authorPhotoPath = "/poster.png",
            rating = 8.0,
            date = 123,
            description = ""
        )
        private val episode = Episode(
            id = 123,
            episodeNumber = 2,
            photoPath = "/photo.png",
            episodeName = "episode name",
            runtimeMinutes = 45,
            rating = 9f,
            seasonNumber = 2,
            seriesId = 123,
        )
    }
}