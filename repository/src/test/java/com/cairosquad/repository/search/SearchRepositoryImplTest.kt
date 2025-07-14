package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.dto.CachedArtistDto
import com.cairosquad.repository.search.data_source.local.dto.CachedMovieDto
import com.cairosquad.repository.search.data_source.local.dto.CachedSeriesDto
import com.cairosquad.repository.search.data_source.local.SearchCacheDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ApiArtistDto
import com.cairosquad.repository.search.data_source.remote.dto.ApiMovieDto
import com.cairosquad.repository.search.data_source.remote.dto.ApiSeriesDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant


@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {


    private val remoteDS = mockk<RemoteSearchDataSource>()
    private val cacheDS = mockk<SearchCacheDataSource>()
    private lateinit var repo: SearchRepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repo = SearchRepositoryImpl(remoteDS, cacheDS)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `getSeries returns cached list when present`() = runTest {
        //Given
        val query = "dark"
        val cacheDto = CachedSeriesDto(
            id = 42,
            name = "Dark",
            posterPath = "/dark.jpg",
            voteAverage = 8.8,
            query = query,
            timestamp = Instant.now().toEpochMilli()
        )
        coEvery { cacheDS.getCachedSeries(query) } returns listOf(cacheDto)
        //When
        val result = repo.getSeries(query)
        //Then
        assertEquals(listOf(Series(42, "Dark", 8.8f, "/dark.jpg")), result)
        coVerify { cacheDS.getCachedSeries(query) }
        coVerify(exactly = 0) { remoteDS.getSeries(any()) }
    }

    @Test
    fun `getSeries fetches remote and caches when cache empty`() = runTest {
        //Given
        val query = "lost"
        coEvery { cacheDS.getCachedSeries(query) } returns emptyList()
        val remoteDto = ApiSeriesDto(
            id = 7,
            name = "Lost",
            posterPath = "/lost.jpg",
            voteAverage = 8.3,
        )
        coEvery { remoteDS.getSeries(query) } returns listOf(remoteDto)
        coEvery { cacheDS.cacheSeries(query, any()) } just runs
        //When
        val result = repo.getSeries(query)
        //Then
        assertEquals(listOf(Series(7, "Lost", 8.3f, "/lost.jpg")), result)
        coVerify { remoteDS.getSeries(query) }
        coVerify { cacheDS.cacheSeries(query, any()) }
    }


    @Test
    fun `getMovies returns cached list when present`() = runTest {
        //Given
        val query = "inception"
        val cacheDto = CachedMovieDto(
            id = 1,
            title = "Inception",
            posterPath = "/inc.jpg",
            voteAverage = 8.8,
            query = query,
            timestamp = Instant.now().toEpochMilli()
        )
        coEvery { cacheDS.getCachedMovies(query) } returns listOf(cacheDto)
        //When
        val result = repo.getMovies(query)
        //Then
        assertEquals(listOf(Movie(1, "Inception", 8.8f, "/inc.jpg")), result)
        coVerify(exactly = 0) { remoteDS.getMovies(any()) }
    }

    @Test
    fun `getMovies fetches remote and caches when cache empty`() = runTest {
        //Given
        val query = "matrix"
        coEvery { cacheDS.getCachedMovies(query) } returns emptyList()
        val remoteDto = mockk<ApiMovieDto>(relaxed = true) {
            every { toEntity() } returns Movie(99, "Matrix", 8.7f, "/mx.jpg")

        }
        coEvery { remoteDS.getMovies(query) } returns listOf(remoteDto)
        coEvery { cacheDS.cacheMovies(query, any()) } just runs
        //When
        val result = repo.getMovies(query)
        //Then
        assertEquals(listOf(Movie(99, "Matrix", 8.7f, "/mx.jpg")), result)
        coVerify { remoteDS.getMovies(query) }
        coVerify { cacheDS.cacheMovies(query, any()) }
    }

    @Test
    fun `getArtists returns cached list when present`() = runTest {
        //Given
        val query = "weeknd"
        val cacheDto = CachedArtistDto(
            id = 5,
            name = "The Weeknd",
            photoPath = "/w.jpg",
            query = query,
            timestamp = Instant.now().toEpochMilli()
        )
        coEvery { cacheDS.getCachedArtists(query) } returns listOf(cacheDto)
        //When
        val result = repo.getArtists(query)
        //Then
        assertEquals(listOf(Artist(5, "The Weeknd", "/w.jpg")), result)
        coVerify(exactly = 0) { remoteDS.getArtists(any()) }
    }

    @Test
    fun `getArtists fetches remote and caches when cache empty`() = runTest {
        //Given
        val query = "adele"
        coEvery { cacheDS.getCachedArtists(query) } returns emptyList()
        val remoteDto = mockk<ApiArtistDto>(relaxed = true) {
            every { toEntity() } returns Artist(8, "Adele", "/a.jpg")

        }
        coEvery { remoteDS.getArtists(query) } returns listOf(remoteDto)
        coEvery { cacheDS.cacheArtist(query, any()) } just runs
        //When
        val result = repo.getArtists(query)
        //Then
        assertEquals(listOf(Artist(8, "Adele", "/a.jpg")), result)
        coVerify { remoteDS.getArtists(query) }
        coVerify { cacheDS.cacheArtist(query, any()) }
    }
}