package com.cairosquad.repository.artists

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.artists.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.artists.data_source.local.toEntity
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest


@OptIn(ExperimentalCoroutinesApi::class)
class ArtistsRepositoryImplTest {
    private lateinit var artistsRemoteDataSource: ArtistsRemoteDataSource
    private lateinit var cacheDataSource: CacheDataSource
    private lateinit var repository: ArtistsRepositoryImpl

    @BeforeTest
    fun setUp() {
        artistsRemoteDataSource = mockk(relaxed = true)
        cacheDataSource = mockk(relaxed = true)
        repository = ArtistsRepositoryImpl(artistsRemoteDataSource, cacheDataSource)
    }

    @Test
    fun `getMoviesOfArtist fetches from remote and caches when cache is empty`() = runTest {
        val artistId = 5L

        val remoteMovieDtos = remoteMovies.map {
            MovieRemoteDto(
                id = it.id.toInt(),
                title = it.title,
                posterPath = it.posterPath,
                voteAverage = it.rating.toDouble()
            )
        }

        coEvery { cacheDataSource.clearExpiredCache(any()) } just Runs
        coEvery { cacheDataSource.getCachedArtistMovies(artistId) } returns emptyList()
        coEvery { artistsRemoteDataSource.getMoviesOfArtist(artistId) } returns remoteMovieDtos
        coEvery { cacheDataSource.cacheMovies(any()) } just Runs
        coEvery { cacheDataSource.cacheArtistMovies(any()) } just Runs

        val result = repository.getMoviesOfArtist(artistId)

        assertThat(result).isEqualTo(remoteMovies)
    }

    @Test
    fun `getArtist returns cached artist if available`() = runTest {
        val artistId = 1L

        val expected = cachedDto.toEntity()

        coEvery { cacheDataSource.clearExpiredCache(any()) } just Runs
        coEvery { cacheDataSource.getCachedArtists(artistId) } returns cachedDto

        val result = repository.getArtistById(artistId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getArtist fetches from remote and caches when cache is empty`() = runTest {
        val artistId = 2L

        coEvery { cacheDataSource.clearExpiredCache(any()) } just Runs
        coEvery { cacheDataSource.getCachedArtists(artistId) } throws IllegalStateException()
        coEvery { artistsRemoteDataSource.getArtistById(artistId) } returns artistRemoteDto
        coEvery { cacheDataSource.cacheArtist(any()) } just Runs

        val result = repository.getArtistById(artistId)

        assertThat(result).isEqualTo(expectedArtist)
    }


    @Test
    fun `getMoviesOfArtist returns cached movies if available`() = runTest {
        val artistId = 3L

        val cachedMovies = expectedMovies.map {
            MovieCacheDto(
                id = it.id.toInt(),
                title = it.title,
                posterPath = it.posterPath,
                voteAverage = it.rating.toDouble(),
                timestamp = System.currentTimeMillis(),
                page = 1,
                query = ""
            )
        }

        coEvery { cacheDataSource.clearExpiredCache(any()) } just Runs
        coEvery { cacheDataSource.getCachedArtistMovies(artistId) } returns cachedMovies

        val result = repository.getMoviesOfArtist(artistId)

        assertThat(result).isEqualTo(expectedMovies)
    }


    @Test
    fun `getSeriesOfArtist returns from remote and caches when cache is empty`() = runTest {
        val artistId = 4L

        coEvery { cacheDataSource.clearExpiredCache(any()) } just Runs
        coEvery { cacheDataSource.getCachedArtistSeries(artistId) } returns emptyList()
        coEvery { artistsRemoteDataSource.getSeriesOfArtist(artistId) } returns remoteSeriesDto
        coEvery { cacheDataSource.cacheSeries(any()) } just Runs
        coEvery { cacheDataSource.cacheArtistSeries(any()) } just Runs

        val result = repository.getSeriesOfArtist(artistId)

        assertThat(result).isEqualTo(expectedSeries)
    }

    private companion object {
        val remoteMovies = listOf(
            Movie(
                id = 1, title = "Remote Movie 1", posterPath = "/poster1.jpg", rating = 8.0f,
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = ""
            ),
            Movie(
                id = 2, title = "Remote Movie 2", posterPath = "/poster2.jpg", rating = 7.0f,
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = ""
            ),
        )
        val cachedDto = ArtistCacheDto(
            id = 1,
            name = "Jane",
            photoPath = "/jane.jpg",
            cachingTimestamp = System.currentTimeMillis(),
            page = 1,
            query = "",
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
        val artistRemoteDto = ArtistRemoteDto(id = 2, name = "John", profilePath = "/john.jpg")
        val remoteSeriesDto =
            listOf(SeriesRemoteDto(id = 1, name = "Series A", posterPath = "/img.jpg"))
        val expectedArtist = Artist(
            id = 2,
            name = "John",
            photoPath = "/john.jpg",
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
        val expectedSeries =
            listOf(Series(
                id = 1, title = "Series A", posterPath = "/img.jpg", rating = 0f,
                trailerPath = "",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                seasonsCount = 1
            ))
    }

    val expectedMovies = listOf(
        Movie(
            id = 1, title = "Film 1", rating = 7.5f, posterPath = "/film1.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )
    )
}