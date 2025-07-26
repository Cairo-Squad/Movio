package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import io.mockk.coEvery
import io.mockk.coVerify
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
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    private val remoteDS = mockk<RemoteSearchDataSource>()
    private val localDataSource = mockk<LocalRecentSearchDataSource>(relaxed = true)
    private val cacheDS = mockk<CacheDataSource>()
    private lateinit var repository: SearchRepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = SearchRepositoryImpl(remoteDS, cacheDS, localDataSource)
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `should return cached series list when series are found in cache`() = runTest {
        //Given
        val page = 1
        coEvery { cacheDS.getCachedSeries(QUERY1,page) } returns listOf(cacheDto1)
        coEvery { cacheDS.clearExpiredCache(any()) } returns Unit
        //When
        val result = repository.getSeries(QUERY1,page)

        //Then
        assertEquals(
            listOf(
                Series(
                    42, "Dark", 8.8f, "/dark.jpg",
                    trailerPath = "",
                    genres = emptyList(),
                    overview = "",
                    releaseDate = 0L,
                    seasonsCount = 1
                )
            ), result
        )
        coVerify { cacheDS.getCachedSeries(QUERY1,page) }
        coVerify(exactly = 0) { remoteDS.getSeries(any(),page) }
    }

    @Test
    fun `should fetch and cache series list when series not found in cache`() = runTest {
        //Given
        val page = 1
        coEvery { cacheDS.getCachedSeries(QUERY2,page) } returns emptyList()
        coEvery { remoteDS.getSeries(QUERY2,page) } returns listOf(remoteDto2)
        coEvery { cacheDS.cacheSeries(any()) } just runs
        coEvery { cacheDS.clearExpiredCache(any()) } returns Unit
        //When
        val result = repository.getSeries(QUERY2,page)

        //Then
        assertEquals(listOf(Series(
            7, "Lost", 8.3f, "/lost.jpg",
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 1
        )), result)
        coVerify { remoteDS.getSeries(QUERY2,page) }
        assertEquals(
            listOf(
                Series(
                    7, "Lost", 8.3f, "/lost.jpg",
                    trailerPath = "",
                    genres = emptyList(),
                    overview = "",
                    releaseDate = 0L,
                    seasonsCount = 1
                )
            ), result
        )
        coVerify { cacheDS.cacheSeries(any()) }
    }


    @Test
    fun `should return cached movies list when movies are found in cache`() = runTest {
        //Given
        val page = 1
        coEvery { cacheDS.getCachedMovies(QUERY3, page) } returns listOf(cacheDto3)
        coEvery { cacheDS.clearExpiredCache(any()) } returns Unit
        //When
        val result = repository.getMovies(QUERY3,page)

        //Then
        assertEquals(
            listOf(
                Movie(
                    1, "Inception", 8.8f, "/inc.jpg",
                    genres = emptyList(),
                    overview = "",
                    releaseDate = 0L,
                    runtimeMinutes = 0,
                    trailerPath = ""
                )
            ), result
        )
        coVerify(exactly = 0) { remoteDS.getMovies(any(),page) }
    }

    @Test
    fun `should fetch and cache movies list when movies not found in cache`() = runTest {
        //Given
        val page = 1
        coEvery { cacheDS.getCachedMovies(QUERY4,page) } returns emptyList()
        coEvery { remoteDS.getMovies(QUERY4, page) } returns listOf(remoteDto4)
        coEvery { cacheDS.cacheMovies(any()) } just runs
        coEvery { cacheDS.clearExpiredCache(any()) } returns Unit
        //When
        val result = repository.getMovies(QUERY4,page)

        //Then
        assertEquals(
            listOf(
                Movie(
                    99, "Matrix", rating = 8.7f, "/mx.jpg",
                    genres = emptyList(),
                    overview = "",
                    releaseDate = 0L,
                    runtimeMinutes = 0,
                    trailerPath = ""
                )
            ), result
        )
        coVerify { remoteDS.getMovies(QUERY4,page) }
        coVerify { cacheDS.cacheMovies(any()) }
    }

    @Test
    fun `should return cached artists list when artists are found in cache`() = runTest {
        //Given
        val page = 1
        coEvery { cacheDS.getCachedArtists(QUERY5,page) } returns listOf(cacheDto5)
        coEvery { cacheDS.clearExpiredCache(any()) } returns Unit
        //When
        val result = repository.getArtists(QUERY5,page)

        //Then
        assertEquals(listOf(Artist(5, "The Weeknd", "/w.jpg")), result)
        coVerify(exactly = 0) { remoteDS.getArtists(any(),page) }
    }

    @Test
    fun `should fetch and cache artists list when artists not found in cache`() = runTest {
        //Given
        val page = 1
        coEvery { cacheDS.getCachedArtists(QUERY6,page) } returns emptyList()
        coEvery { remoteDS.getArtists(QUERY6,page) } returns listOf(remoteDto6)
        coEvery { cacheDS.cacheArtist(any()) } just runs
        coEvery { cacheDS.clearExpiredCache(any()) } returns Unit
        //When
        val result = repository.getArtists(QUERY6,page)
        //Then
        assertEquals(listOf(Artist(8, "Adele", "/a.jpg")), result)
        coVerify { remoteDS.getArtists(QUERY6,page) }
        coVerify { cacheDS.cacheArtist(any()) }
    }

    private companion object {
        const val QUERY1 = "dark"
        val page = 1
        val cacheDto1 = SeriesCacheDto(
            id = 42,
            page = page,
            name = "Dark",
            query = QUERY1,
            posterPath = "/dark.jpg",
            voteAverage = 8.8,
            timestamp = Date().time
        )
        const val QUERY2 = "lost"
        val remoteDto2 = SeriesRemoteDto(
            id = 7,
            name = "Lost",
            posterPath = "/lost.jpg",
            voteAverage = 8.3,
        )
        const val QUERY3 = "inception"
        val cacheDto3 = MovieCacheDto(
            id = 1,
            title = "Inception",
            query = QUERY3,
            page = 1,
            posterPath = "/inc.jpg",
            voteAverage = 8.8,
            timestamp = Instant.now().toEpochMilli()
        )
        const val QUERY4 = "matrix"
        val remoteDto4 = MovieRemoteDto(
            id = 99,
            title = "Matrix",
            posterPath = "/mx.jpg",
            voteAverage = 8.7,
        )
        const val QUERY5 = "weeknd"
        val cacheDto5 = ArtistCacheDto(
            id = 5,
            name = "The Weeknd",
            query = QUERY5,
            page = 1,
            photoPath = "/w.jpg",
            timestamp = Instant.now().toEpochMilli(),
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
        const val QUERY6 = "adele"
        val remoteDto6 = ArtistRemoteDto(
            id = 8,
            name = "Adele",
            profilePath = "/a.jpg",
        )
    }

    @Test
    fun `should return all queries when getAll is called`() = runTest {
        // Given
        val expectedQueries = listOf("query1", "query2", "query3")
        coEvery { localDataSource.getAll() } returns expectedQueries

        //When
        val result = repository.getAllHistory()

        // Then
        assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `should return all queries when getByQuery is called with blank query`() = runTest {
        // Given
        val expectedQueries = emptyList<String>()
        val blankQuery = ""
        coEvery { localDataSource.getAll() } returns expectedQueries
        //When
        val result = repository.getAllHistoryByQuery(blankQuery)

        // Then
        assertEquals(expectedQueries, result)
    }

    @Test
    fun `should clear all queries when clearAll is called`() = runTest {
        // Given
        coEvery { localDataSource.clearAll() } returns Unit

        // When
        repository.clearAll()

        // Then
        coVerify(exactly = 1) { localDataSource.clearAll() }
    }

    @Test
    fun `should remove specific query when removeQuery is called`() = runTest {
        // Given
        val queryToRemove = "old query"
        coEvery { localDataSource.removeQuery(queryToRemove) } returns Unit

        // When
        repository.removeQuery(queryToRemove)

        // Then
        coVerify(exactly = 1) { localDataSource.removeQuery(queryToRemove) }
    }

    @Test
    fun `should add query when addQuery is called`() = runTest {
        // Given
        val queryToAdd = "new query"
        coEvery { localDataSource.addQuery(queryToAdd) } returns Unit

        // When
        repository.addQuery(queryToAdd)

        // Then
        coVerify(exactly = 1) { localDataSource.addQuery(queryToAdd) }
    }
}