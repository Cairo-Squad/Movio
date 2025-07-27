package com.cairosquad.domain.usecase.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetArtistDetailsUseCaseTest {
    private val artistsRepository: ArtistsRepository = mockk(relaxed = true)
    private lateinit var useCase: GetArtistDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetArtistDetailsUseCase(artistsRepository)
    }

    @Test
    fun `getArtist SHOULD return artist from repository`() = runTest {
        coEvery { artistsRepository.getArtistById(312L) } returns actor

        val result = useCase.getArtist(312L)

        assertThat(result).isEqualTo(actor)
        coVerify(exactly = 1) { artistsRepository.getArtistById(312L) }
    }

    @Test
    fun `getMoviesOfArtist SHOULD return movies from repository`() = runTest {
        coEvery { artistsRepository.getMoviesOfArtist(312L) } returns listOf(movie)

        val result = useCase.getMoviesOfArtist(312L)

        assertThat(result).containsExactly(movie)
        coVerify(exactly = 1) { artistsRepository.getMoviesOfArtist(312L) }
    }

    @Test
    fun `getSeriesOfArtist SHOULD return series from repository`() = runTest {
        coEvery { artistsRepository.getSeriesOfArtist(312L) } returns listOf(series1, series2)

        val result = useCase.getSeriesOfArtist(312L)

        assertThat(result).containsExactly(series1, series2)
        coVerify(exactly = 1) { artistsRepository.getSeriesOfArtist(312L) }
    }

    // 🔥 Edge Case
    @Test(expected = RuntimeException::class)
    fun `getArtist SHOULD throw exception when repository fails`() = runTest {
        coEvery { artistsRepository.getArtistById(312L) } throws RuntimeException("Failed to fetch")

        useCase.getArtist(312L)
    }

    private companion object {

        private val movie = Movie(
            id = 123,
            runtimeMinutes = 45,
            rating = 9f,
            title = "Spider Man",
            posterPath = "/poster.png",
            genres = listOf(Genre(1, "Action")),
            overview = "overview",
            releaseDate = 123L,
            trailerPath = "",
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
            id = 2L,
            title = "Series Two",
            rating = 7.5f,
            posterPath = "/poster2.png",
            trailerPath = "trailer2",
            genres = listOf(Genre(3L, "Comedy")),
            overview = "Second series overview",
            releaseDate = 654L,
            seasonsCount = 2
        )
    }
}