package com.cairosquad.repository.search

import com.cairosquad.domain.exception.DUnauthorizedException
import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.exception.DomainJsonParsingException
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.exception.NoInternetException
import com.cairosquad.repository.utils.exception.RepoEmptyResponseException
import com.cairosquad.repository.utils.exception.RepoJsonParsingException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.cairosquad.repository.utils.exception.UnknownDataSourceException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    private val moviesRemoteDataSource = mockk<MoviesRemoteDataSource>()
    private val seriesRemoteDataSource = mockk<SeriesRemoteDataSource>()
    private val artistsRemoteDataSource = mockk<ArtistsRemoteDataSource>()
    private val localDataSource = mockk<LocalRecentSearchDataSource>(relaxed = true)
    private lateinit var repository: SearchRepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = SearchRepositoryImpl(
            moviesRemoteDataSource,
            seriesRemoteDataSource,
            artistsRemoteDataSource,
            localDataSource
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return all queries when getAllHistory is called`() = runTest {
        // Given
        val expectedQueries = listOf("query1", "query2", "query3")
        coEvery { localDataSource.getAll() } returns expectedQueries

        // When
        val result = repository.getAllHistory()

        // Then
        assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `should throw UnknownException when getAllHistory fails with UnknownDataSourceException`() = runTest {
        // Given
        val exception = UnknownDataSourceException("Failed to fetch history")
        coEvery { localDataSource.getAll() } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.getAllHistory()
        }

        // Then
        assertEquals("Failed to fetch history", thrown.message)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `should throw InternetConnectionException when getAllHistory fails with NoInternetException`() = runTest {
        // Given
        val exception = NoInternetException("No internet connection")
        coEvery { localDataSource.getAll() } throws exception

        // When
        val thrown = assertFailsWith<InternetConnectionException> {
            repository.getAllHistory()
        }

        // Then
        assertEquals("No internet connection", thrown.message)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `should throw UnknownException when getAllHistory fails with non-DataSourceException`() = runTest {
        // Given
        val exception = RuntimeException("Unexpected error")
        coEvery { localDataSource.getAll() } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.getAllHistory()
        }

        // Then
        assertEquals("Unexpected error", thrown.message)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `should return merged unique queries when getAllHistoryByQuery is called with non-blank query`() = runTest {
        // Given
        val query = "dark"
        val localQueries = listOf("dark", "dark series")
        val movies = listOf(
            MovieRemoteDto(id = 1, title = "Dark Knight", posterPath = "/dk.jpg", voteAverage = 8.8),
            MovieRemoteDto(id = 2, title = "Dark Shadows", posterPath = "/ds.jpg", voteAverage = 6.2)
        )
        val series = listOf(
            SeriesRemoteDto(id = 42, name = "Dark", posterPath = "/dark.jpg", voteAverage = 8.8f),
            SeriesRemoteDto(id = 43, name = "Dark Matter", posterPath = "/dm.jpg", voteAverage = 7.5f)
        )
        val artists = listOf(
            ArtistRemoteDto(id = 5, name = "Dark Artist", profilePath = "/da.jpg")
        )
        coEvery { localDataSource.getByQuery(query) } returns localQueries
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } returns movies
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } returns series
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } returns artists

        // When
        val result = repository.getAllHistoryByQuery(query)

        // Then
        val expected = listOf(
            "dark",
            "dark series",
            "Dark Knight",
            "Dark Shadows",
            "Dark",
            "Dark Matter",
            "Dark Artist"
        ).distinct()
        assertEquals(expected.sorted(), result.sorted()) // Sort for consistent comparison
        coVerify { localDataSource.getByQuery(query) }
        coVerify { moviesRemoteDataSource.getMoviesByQuery(query, 1) }
        coVerify { seriesRemoteDataSource.getSeriesByQuery(query, 1) }
        coVerify { artistsRemoteDataSource.getArtistsByQuery(query, 1) }
    }

    @Test
    fun `should throw UnknownException when getAllHistoryByQuery fails with UnknownDataSourceException for non-blank query`() = runTest {
        // Given
        val query = "dark"
        val exception = UnknownDataSourceException("Failed to fetch query results")
        coEvery { localDataSource.getByQuery(query) } throws exception
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } returns emptyList()
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } returns emptyList()
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } returns emptyList()

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Failed to fetch query results", thrown.message)
    }

    @Test
    fun `should throw InternetConnectionException when getAllHistoryByQuery fails with NoInternetException for non-blank query`() = runTest {
        // Given
        val query = "dark"
        val exception = NoInternetException("No internet connection")
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } throws exception
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } throws exception
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } throws exception
        coEvery { localDataSource.getByQuery(query) } returns emptyList()

        // When
        val thrown = assertFailsWith<InternetConnectionException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("No internet connection", thrown.message)
    }

    @Test
    fun `should throw DomainEmptyResponseException when getAllHistoryByQuery fails with RepoEmptyResponseException for non-blank query`() = runTest {
        // Given
        val query = "dark"
        val exception = RepoEmptyResponseException("Empty response body")
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } throws exception
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } throws exception
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } throws exception
        coEvery { localDataSource.getByQuery(query) } returns emptyList()


        // When
        val thrown = assertFailsWith<DomainEmptyResponseException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Empty response body", thrown.message)
    }

    @Test
    fun `should throw DomainJsonParsingException when getAllHistoryByQuery fails with RepoJsonParsingException for non-blank query`() = runTest {
        // Given
        val query = "dark"
        val exception = RepoJsonParsingException("Failed to parse response")
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } throws exception
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } throws exception
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } throws exception
        coEvery { localDataSource.getByQuery(query) } returns emptyList()

        // When
        val thrown = assertFailsWith<DomainJsonParsingException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Failed to parse response", thrown.message)
    }

    @Test
    fun `should throw DUnauthorizedException when getAllHistoryByQuery fails with UnauthorizedException for non-blank query`() = runTest {
        // Given
        val query = "dark"
        val exception = UnauthorizedException("Unauthorized access")
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } throws exception
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } throws exception
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } throws exception
        coEvery { localDataSource.getByQuery(query) } returns emptyList()

        // When
        val thrown = assertFailsWith<DUnauthorizedException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Unauthorized access", thrown.message)
    }

    @Test
    fun `should throw UnknownException when getAllHistoryByQuery fails with non-DataSourceException for non-blank query`() = runTest {
        // Given
        val query = "dark"
        val exception = RuntimeException("Unexpected error")
        coEvery { localDataSource.getByQuery(query) } throws exception
        coEvery { seriesRemoteDataSource.getSeriesByQuery(query, 1) } throws exception
        coEvery { moviesRemoteDataSource.getMoviesByQuery(query, 1) } throws exception
        coEvery { artistsRemoteDataSource.getArtistsByQuery(query, 1) } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Unexpected error", thrown.message)
    }

    @Test
    fun `should return all queries when getAllHistoryByQuery is called with blank query`() = runTest {
        // Given
        val query = ""
        val expectedQueries = listOf("query1", "query2", "query3")
        coEvery { localDataSource.getAll() } returns expectedQueries

        // When
        val result = repository.getAllHistoryByQuery(query)

        // Then
        assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { localDataSource.getAll() }
        coVerify(exactly = 0) { moviesRemoteDataSource.getMoviesByQuery(any(), any()) }
        coVerify(exactly = 0) { seriesRemoteDataSource.getSeriesByQuery(any(), any()) }
        coVerify(exactly = 0) { artistsRemoteDataSource.getArtistsByQuery(any(), any()) }
    }

    @Test
    fun `should throw UnknownException when getAllHistoryByQuery fails with UnknownDataSourceException for blank query`() = runTest {
        // Given
        val query = ""
        val exception = UnknownDataSourceException("Failed to fetch all queries")
        coEvery { localDataSource.getAll() } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Failed to fetch all queries", thrown.message)
        coVerify { localDataSource.getAll() }
    }

    @Test
    fun `should throw InternetConnectionException when getAllHistoryByQuery fails with NoInternetException for blank query`() = runTest {
        // Given
        val query = ""
        val exception = NoInternetException("No internet connection")
        coEvery { localDataSource.getAll() } throws exception

        // When
        val thrown = assertFailsWith<InternetConnectionException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("No internet connection", thrown.message)
        coVerify { localDataSource.getAll() }
    }

    @Test
    fun `should throw UnknownException when getAllHistoryByQuery fails with non-DataSourceException for blank query`() = runTest {
        // Given
        val query = ""
        val exception = RuntimeException("Unexpected error")
        coEvery { localDataSource.getAll() } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.getAllHistoryByQuery(query)
        }

        // Then
        assertEquals("Unexpected error", thrown.message)
        coVerify { localDataSource.getAll() }
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
    fun `should throw UnknownException when clearAll fails with UnknownDataSourceException`() = runTest {
        // Given
        val exception = UnknownDataSourceException("Failed to clear history")
        coEvery { localDataSource.clearAll() } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.clearAll()
        }

        // Then
        assertEquals("Failed to clear history", thrown.message)
        coVerify(exactly = 1) { localDataSource.clearAll() }
    }

    @Test
    fun `should throw InternetConnectionException when clearAll fails with NoInternetException`() = runTest {
        // Given
        val exception = NoInternetException("No internet connection")
        coEvery { localDataSource.clearAll() } throws exception

        // When
        val thrown = assertFailsWith<InternetConnectionException> {
            repository.clearAll()
        }

        // Then
        assertEquals("No internet connection", thrown.message)
        coVerify(exactly = 1) { localDataSource.clearAll() }
    }

    @Test
    fun `should throw UnknownException when clearAll fails with non-DataSourceException`() = runTest {
        // Given
        val exception = RuntimeException("Unexpected error")
        coEvery { localDataSource.clearAll() } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.clearAll()
        }

        // Then
        assertEquals("Unexpected error", thrown.message)
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
    fun `should throw UnknownException when removeQuery fails with UnknownDataSourceException`() = runTest {
        // Given
        val queryToRemove = "old query"
        val exception = UnknownDataSourceException("Failed to remove query")
        coEvery { localDataSource.removeQuery(queryToRemove) } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.removeQuery(queryToRemove)
        }

        // Then
        assertEquals("Failed to remove query", thrown.message)
        coVerify(exactly = 1) { localDataSource.removeQuery(queryToRemove) }
    }

    @Test
    fun `should throw InternetConnectionException when removeQuery fails with NoInternetException`() = runTest {
        // Given
        val queryToRemove = "old query"
        val exception = NoInternetException("No internet connection")
        coEvery { localDataSource.removeQuery(queryToRemove) } throws exception

        // When
        val thrown = assertFailsWith<InternetConnectionException> {
            repository.removeQuery(queryToRemove)
        }

        // Then
        assertEquals("No internet connection", thrown.message)
        coVerify(exactly = 1) { localDataSource.removeQuery(queryToRemove) }
    }

    @Test
    fun `should throw UnknownException when removeQuery fails with non-DataSourceException`() = runTest {
        // Given
        val queryToRemove = "old query"
        val exception = RuntimeException("Unexpected error")
        coEvery { localDataSource.removeQuery(queryToRemove) } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.removeQuery(queryToRemove)
        }

        // Then
        assertEquals("Unexpected error", thrown.message)
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

    @Test
    fun `should throw UnknownException when addQuery fails with UnknownDataSourceException`() = runTest {
        // Given
        val queryToAdd = "new query"
        val exception = UnknownDataSourceException("Failed to add query")
        coEvery { localDataSource.addQuery(queryToAdd) } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.addQuery(queryToAdd)
        }

        // Then
        assertEquals("Failed to add query", thrown.message)
        coVerify(exactly = 1) { localDataSource.addQuery(queryToAdd) }
    }

    @Test
    fun `should throw InternetConnectionException when addQuery fails with NoInternetException`() = runTest {
        // Given
        val queryToAdd = "new query"
        val exception = NoInternetException("No internet connection")
        coEvery { localDataSource.addQuery(queryToAdd) } throws exception

        // When
        val thrown = assertFailsWith<InternetConnectionException> {
            repository.addQuery(queryToAdd)
        }

        // Then
        assertEquals("No internet connection", thrown.message)
        coVerify(exactly = 1) { localDataSource.addQuery(queryToAdd) }
    }

    @Test
    fun `should throw UnknownException when addQuery fails with non-DataSourceException`() = runTest {
        // Given
        val queryToAdd = "new query"
        val exception = RuntimeException("Unexpected error")
        coEvery { localDataSource.addQuery(queryToAdd) } throws exception

        // When
        val thrown = assertFailsWith<UnknownException> {
            repository.addQuery(queryToAdd)
        }

        // Then
        assertEquals("Unexpected error", thrown.message)
        coVerify(exactly = 1) { localDataSource.addQuery(queryToAdd) }
    }
}