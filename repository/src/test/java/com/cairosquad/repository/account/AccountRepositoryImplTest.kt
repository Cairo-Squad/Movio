package com.cairosquad.repository.account

import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class AccountRepositoryImplTest {

    private val remoteDataSource: AccountRemoteDataSource = mockk()
    private val localDataSource: AccountLocalDataSource = mockk()
    private lateinit var repository: AccountRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = AccountRepositoryImpl(remoteDataSource, localDataSource)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovieLists returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getMovieLists(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getMovieLists(any(), any()) }
    }

    @Test
    fun `getSeriesLists returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getSeriesLists(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getSeriesLists(any(), any()) }
    }

    @Test
    fun `getMoviesLists returns list if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getMovieLists(123, 1) } returns listOf(MediaListDto(id = 123, "", 1, ""))

        val result = repository.getMovieLists(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getSeriesLists returns list if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getSeriesLists(123, 1) } returns listOf(MediaListDto(id = 123, "", 1, ""))

        val result = repository.getSeriesLists(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getFavoriteMovies returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getFavoriteMovies(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getFavoriteMovies(any(), any()) }
    }

    @Test
    fun `getFavoriteSeries returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getFavoriteSeries(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getFavoriteSeries(any(), any()) }
    }

    @Test
    fun `getFavoriteMovies returns list of Movies if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getFavoriteMovies(123, 1) } returns listOf(MovieRemoteDto())

        val result = repository.getFavoriteMovies(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getFavoriteSeries returns list of Movies if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getFavoriteSeries(123, 1) } returns listOf(SeriesRemoteDto())

        val result = repository.getFavoriteSeries(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `addMovieToFavorite does nothing when account id is null`() = runTest {
        val movieId = 123L
        coEvery { localDataSource.getAccountId() } returns null

        repository.addMovieToFavorite(movieId)

        coVerify(exactly = 0) { remoteDataSource.addMovieToFavorite(any(), any()) }
    }

    @Test
    fun `addMovieToFavorite calls remote data source when account id exists`() = runTest {
        val accountId = 123L
        val movieId = 456L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.addMovieToFavorite(accountId, movieId) } just Runs

        repository.addMovieToFavorite(movieId)

        coVerify { remoteDataSource.addMovieToFavorite(accountId, movieId) }
    }

    @Test
    fun `addSeriesToFavorite does nothing when account id is null`() = runTest {
        val seriesId = 123L
        coEvery { localDataSource.getAccountId() } returns null

        repository.addSeriesToFavorite(seriesId)

        coVerify(exactly = 0) { remoteDataSource.addSeriesToFavorite(any(), any()) }
    }

    @Test
    fun `addSeriesToFavorite calls remote data source when account id exists`() = runTest {
        val accountId = 123L
        val seriesId = 456L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.addSeriesToFavorite(accountId, seriesId) } just Runs

        repository.addSeriesToFavorite(seriesId)

        coVerify { remoteDataSource.addSeriesToFavorite(accountId, seriesId) }
    }

    @Test
    fun `addMovieToHistory does nothing when account id is null`() = runTest {
        val movieId = 123L
        coEvery { localDataSource.getAccountId() } returns null

        repository.addMovieToHistory(movieId)

        coVerify(exactly = 0) { remoteDataSource.addMovieToHistory(any(), any()) }
    }

    @Test
    fun `addMovieToHistory calls remote data source when account id exists`() = runTest {
        val accountId = 123L
        val movieId = 456L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.addMovieToHistory(accountId, movieId) } just Runs

        repository.addMovieToHistory(movieId)

        coVerify { remoteDataSource.addMovieToHistory(accountId, movieId) }
    }

    // addSeriesToHistory tests
    @Test
    fun `addSeriesToHistory does nothing when account id is null`() = runTest {
        val seriesId = 123L
        coEvery { localDataSource.getAccountId() } returns null

        repository.addSeriesToHistory(seriesId)

        coVerify(exactly = 0) { remoteDataSource.addSeriesToHistory(any(), any()) }
    }

    @Test
    fun `addSeriesToHistory calls remote data source when account id exists`() = runTest {
        val accountId = 123L
        val seriesId = 456L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.addSeriesToHistory(accountId, seriesId) } just Runs

        repository.addSeriesToHistory(seriesId)

        coVerify { remoteDataSource.addSeriesToHistory(accountId, seriesId) }
    }

    @Test
    fun `getHistoryMovies returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getHistoryMovies(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getHistoryMovies(any(), any()) }
    }

    @Test
    fun `getHistorySeries returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getHistorySeries(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getHistorySeries(any(), any()) }
    }

    @Test
    fun `getHistoryMovies returns list if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getHistoryMovies(123, 1) } returns listOf(MovieRemoteDto())

        val result = repository.getHistoryMovies(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getHistorySeries returns list if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getHistorySeries(123, 1) } returns listOf(SeriesRemoteDto())

        val result = repository.getHistorySeries(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getRatedItems returns empty lists if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val moviesResult = repository.getRatedMovies(1)
        val seriesResult = repository.getRatedSeries(1)

        assertThat(moviesResult).isEmpty()
        assertThat(seriesResult).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getRatedMovies(any(), any()) }
        coVerify(exactly = 0) { remoteDataSource.getRatedSeries(any(), any()) }
    }
}