package com.cairosquad.repository.account

import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
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

    // addSeriesToFavorite tests
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

    // addMovieToHistory tests
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
    fun `getRatedItems returns empty lists if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getRatedItems(1)

        assertThat(result.first).isEmpty()
        assertThat(result.second).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getRatedMovies(any(), any()) }
        coVerify(exactly = 0) { remoteDataSource.getRatedSeries(any(), any()) }
    }
}