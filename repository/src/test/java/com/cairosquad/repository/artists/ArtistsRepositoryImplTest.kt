package com.cairosquad.repository.artists

import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.entity.Artist
import com.cairosquad.repository.artists.data_source.local.ArtistsLocalDataSource
import com.cairosquad.repository.artists.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.utils.exception.NoInternetException
import com.cairosquad.repository.utils.exception.RepoEmptyResponseException
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfArtist
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMovieTopCast
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesTopCast
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistsRepositoryImplTest {

    private lateinit var repository: ArtistsRepositoryImpl
    private lateinit var remoteDataSource: ArtistsRemoteDataSource
    private lateinit var localDataSource: ArtistsLocalDataSource

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        repository = ArtistsRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `should return  artists when getArtistsByQuery is called from remote`() = runTest {
        val query = "John"
        val page = 1

        val result = repository.getArtistsByQuery(query, page)

        assertThat(result).isEqualTo(listOf(expectedArtist))
        coVerify(exactly = 1) { remoteDataSource.getArtistsByQuery(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getArtistsByQuery is called`() = runTest {
        val query = "John"
        val page = 1
        coEvery { remoteDataSource.getArtistsByQuery(query, page) } returns listOf(artistRemoteDto)

        val result = repository.getArtistsByQuery(query, page)

        assertThat(result).isEqualTo(listOf(expectedArtist))
        coVerify(exactly = 1) { remoteDataSource.getArtistsByQuery(query, page) }
    }

    @Test
    fun `should throw DomainEmptyResponseException when getArtistsByQuery is called and remote returns empty`() =
        runTest {
            val query = "John"
            val page = 1
            val cacheCode = "artists_query_${query}_$page"
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns emptyList()
            coEvery {
                remoteDataSource.getArtistsByQuery(
                    query,
                    page
                )
            } throws RepoEmptyResponseException()

            assertFailsWith<DomainEmptyResponseException> {
                repository.getArtistsByQuery(query, page)
            }
            coVerify(exactly = 1) { remoteDataSource.getArtistsByQuery(query, page) }
            coVerify(exactly = 0) { localDataSource.insertCacheCodeWithArtists(any()) }
        }

    @Test
    fun `should throw InternetConnectionException when getArtistsByQuery is called and no internet`() =
        runTest {
            val query = "John"
            val page = 1
            val cacheCode = "artists_query_${query}_$page"
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getArtistsByQuery(query, page) } throws NoInternetException()

            assertFailsWith<InternetConnectionException> {
                repository.getArtistsByQuery(query, page)
            }
            coVerify(exactly = 1) { remoteDataSource.getArtistsByQuery(query, page) }
            coVerify(exactly = 0) { localDataSource.insertCacheCodeWithArtists(any()) }
        }

    @Test
    fun `should return cached artist when getArtistById is called and cache is available`() =
        runTest {
            val artistId = 1L
            val cacheCode = getCacheCodeOfArtist(artistId)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns listOf(
                cachedArtistDto
            )

            val result = repository.getArtistById(artistId)

            assertThat(result).isEqualTo(expectedArtist)
            coVerify(exactly = 1) { localDataSource.getArtistsByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getArtistById(any()) }
        }

    @Test
    fun `should fetch data from remote when getArtistById is called and cache is empty`() =
        runTest {
            val artistId = 1L
            val cacheCode = "artist_$artistId"
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getArtistById(artistId) } returns artistRemoteDto
            coEvery { localDataSource.insertCacheCodeWithArtists(any()) } just Runs

            val result = repository.getArtistById(artistId)

            assertThat(result).isEqualTo(expectedArtist)
            coVerify(exactly = 1) { remoteDataSource.getArtistById(artistId) }
            coVerify(exactly = 1) { localDataSource.insertCacheCodeWithArtists(any()) }
        }

    @Test
    fun `should throw DomainEmptyResponseException when getArtistById is called and remote returns empty`() =
        runTest {
            val artistId = 1L
            val cacheCode = "artist_$artistId"
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getArtistById(artistId) } throws RepoEmptyResponseException()

            assertFailsWith<DomainEmptyResponseException> {
                repository.getArtistById(artistId)
            }
            coVerify(exactly = 1) { remoteDataSource.getArtistById(artistId) }
            coVerify(exactly = 0) { localDataSource.insertCacheCodeWithArtists(any()) }
        }

    @Test
    fun `should return cached artists when getMovieTopCast is called and cache is available`() =
        runTest {
            val movieId = 100L
            val page = 1
            val cacheCode = getCacheCodeOfMovieTopCast(movieId, page)
            val cachedArtists = listOf(cachedArtistDto)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns cachedArtists

            val result = repository.getMovieTopCast(movieId, page)

            assertThat(result).isEqualTo(listOf(expectedArtist))
            coVerify(exactly = 1) { localDataSource.getArtistsByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getMovieTopCast(any(), any()) }
        }

    @Test
    fun `should fetch data from remote when getMovieTopCast is called and cache is empty`() =
        runTest {
            val movieId = 100L
            val page = 1
            val cacheCode = getCacheCodeOfMovieTopCast(movieId, page)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getMovieTopCast(movieId, page) } returns listOf(
                artistRemoteDto
            )
            coEvery { localDataSource.insertCacheCodeWithArtists(any()) } just Runs

            val result = repository.getMovieTopCast(movieId, page)

            assertThat(result).isEqualTo(listOf(expectedArtist))
            coVerify(exactly = 1) { remoteDataSource.getMovieTopCast(movieId, page) }
            coVerify(exactly = 1) { localDataSource.insertCacheCodeWithArtists(any()) }
        }

    @Test
    fun `should return cached artists when getSeriesTopCast is called and cache is available`() =
        runTest {
            val seriesId = 200L
            val page = 1
            val cacheCode = getCacheCodeOfSeriesTopCast(seriesId, page)
            val cachedArtists = listOf(cachedArtistDto)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns cachedArtists

            val result = repository.getSeriesTopCast(seriesId, page)

            assertThat(result).isEqualTo(listOf(expectedArtist))
            coVerify(exactly = 1) { localDataSource.getArtistsByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getSeriesTopCast(any(), any()) }
        }

    @Test
    fun `should fetch data from remote when getSeriesTopCast is called and cache is empty`() =
        runTest {
            val seriesId = 200L
            val page = 1
            val cacheCode = "series_top_cast_${seriesId}_$page"
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getArtistsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getSeriesTopCast(seriesId, page) } returns listOf(
                artistRemoteDto
            )
            coEvery { localDataSource.insertCacheCodeWithArtists(any()) } just Runs

            val result = repository.getSeriesTopCast(seriesId, page)

            assertThat(result).isEqualTo(listOf(expectedArtist))
            coVerify(exactly = 1) { remoteDataSource.getSeriesTopCast(seriesId, page) }
            coVerify(exactly = 1) { localDataSource.insertCacheCodeWithArtists(any()) }
        }

    private companion object {
        private val artistRemoteDto = ArtistRemoteDto(
            id = 1,
            name = "John Doe",
            profilePath = "/path/to/photo.jpg",
            placeOfBirth = "USA",
            birthday = "2000-01-01",
            biography = "Talented actor",
            department = "Acting"
        )

        private val cachedArtistDto = ArtistCacheDto(
            id = 1L,
            name = "John Doe",
            photoPath = "/path/to/photo.jpg",
            country = "USA",
            birthDate = 946684800000L, // 2000-01-01
            biography = "Talented actor",
            department = "Acting",
            cachingTimestamp = System.currentTimeMillis()
        )

        private val expectedArtist = Artist(
            id = 1L,
            name = "John Doe",
            photoPath = "/path/to/photo.jpg",
            country = "USA",
            birthDate = 946684800000L,
            biography = "Talented actor",
            department = "Acting"
        )
    }
}