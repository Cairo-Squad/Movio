package com.cairosquad.repository.account

import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
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
    private val remoteMovieDataSource: MoviesRemoteDataSource = mockk()
    private val remoteSeriesDataSource: SeriesRemoteDataSource = mockk()
    private lateinit var repository: AccountRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = AccountRepositoryImpl(
            accountRemoteDataSource = remoteDataSource,
            accountLocalDataSource = localDataSource,
            movieRemoteDataSource = remoteMovieDataSource,
            seriesRemoteDataSource = remoteSeriesDataSource
        )
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
        coEvery { remoteDataSource.getMovieLists(123, 1) } returns listOf(
            MediaListDto(
                id = 123,
                "",
                1,
                ""
            )
        )

        val result = repository.getMovieLists(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getSeriesLists returns list if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteDataSource.getSeriesLists(123, 1) } returns listOf(
            MediaListDto(
                id = 123,
                "",
                1,
                ""
            )
        )

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
        coEvery { remoteMovieDataSource.getMoviesGenres() } returns listOf(
            GenreDto(
                id = 123,
                "asd"
            )
        )
        coEvery { remoteDataSource.getFavoriteMovies(123, 1) } returns listOf(MovieRemoteDto())

        val result = repository.getFavoriteMovies(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getFavoriteSeries returns list of Movies if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteSeriesDataSource.getSeriesGenres() } returns listOf(
            GenreDto(
                id = 123,
                "asd"
            )
        )
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
        coEvery { remoteMovieDataSource.getMoviesGenres() } returns listOf(
            GenreDto(
                id = 123,
                "asd"
            )
        )
        coEvery { remoteDataSource.getHistoryMovies(123, 1) } returns listOf(MovieRemoteDto())

        val result = repository.getHistoryMovies(1)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `getHistorySeries returns list if account id is not null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns 123
        coEvery { remoteSeriesDataSource.getSeriesGenres() } returns listOf(
            GenreDto(
                id = 123,
                "asd"
            )
        )
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

    @Test
    fun `addMovieToList calls remote data source`() = runTest {
        val listId = 10L
        val movieId = 20L
        coEvery { remoteDataSource.addMovieToList(listId, movieId) } just Runs

        repository.addMovieToList(listId, movieId)

        coVerify { remoteDataSource.addMovieToList(listId, movieId) }
    }

    @Test
    fun `createList calls remote data source`() = runTest {
        val listName = "My List"
        coEvery { remoteDataSource.createList(listName) } just Runs

        repository.createList(listName)

        coVerify { remoteDataSource.createList(listName) }
    }

    @Test
    fun `removeMovieFromList calls remote data source`() = runTest {
        val listId = 10L
        val movieId = 20L
        coEvery { remoteDataSource.removeMovieFromList(listId, movieId) } just Runs

        repository.removeMovieFromList(listId, movieId)

        coVerify { remoteDataSource.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `removeMovieFromHistory does nothing if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        repository.removeMovieFromHistory(20L)

        coVerify(exactly = 0) { remoteDataSource.removeMovieFromHistory(any(), any()) }
    }

    @Test
    fun `removeMovieFromHistory calls remote data source if account id exists`() = runTest {
        val accountId = 123L
        val movieId = 20L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.removeMovieFromHistory(accountId, movieId) } just Runs

        repository.removeMovieFromHistory(movieId)

        coVerify { remoteDataSource.removeMovieFromHistory(accountId, movieId) }
    }

    @Test
    fun `removeSeriesFromHistory does nothing if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        repository.removeSeriesFromHistory(30L)

        coVerify(exactly = 0) { remoteDataSource.removeMovieFromHistory(any(), any()) }
    }

    @Test
    fun `removeSeriesFromHistory calls remote data source if account id exists`() = runTest {
        val accountId = 123L
        val seriesId = 30L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.removeMovieFromHistory(accountId, seriesId) } just Runs

        repository.removeSeriesFromHistory(seriesId)

        coVerify { remoteDataSource.removeMovieFromHistory(accountId, seriesId) }
    }

    @Test
    fun `removeAccountDetails calls local data source`() = runTest {
        coEvery { localDataSource.removeAccount() } just Runs

        repository.removeAccountDetails()

        coVerify { localDataSource.removeAccount() }
    }

    @Test
    fun `getRatedMovies returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getRatedMovies(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getRatedMovies(any(), any()) }
    }

    @Test
    fun `getRatedMovies returns list with ratings if account id exists`() = runTest {
        val accountId = 123L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.getRatedMovies(accountId, 1) } returns listOf(
            MovieRemoteDto(userRating = 7.5)
        )

        val result = repository.getRatedMovies(1)

        assertThat(result).isNotEmpty()
        assertThat(result.first().second).isEqualTo(7.5)
    }

    @Test
    fun `getRatedSeries returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getRatedSeries(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getRatedSeries(any(), any()) }
    }

    @Test
    fun `getRatedSeries returns list with ratings if account id exists`() = runTest {
        val accountId = 123L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.getRatedSeries(accountId, 1) } returns listOf(
            SeriesRemoteDto(userRating = 8.0)
        )

        val result = repository.getRatedSeries(1)

        assertThat(result).isNotEmpty()
        assertThat(result.first().second).isEqualTo(8.0)
    }

    @Test
    fun `removeMovieFromFavorite does nothing when account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        repository.removeMovieFromFavorite(10L)

        coVerify(exactly = 0) { remoteDataSource.removeMovieFromFavorite(any(), any()) }
    }

    @Test
    fun `removeMovieFromFavorite calls remote data source when account id exists`() = runTest {
        val accountId = 123L
        val movieId = 10L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.removeMovieFromFavorite(accountId, movieId) } just Runs

        repository.removeMovieFromFavorite(movieId)

        coVerify { remoteDataSource.removeMovieFromFavorite(accountId, movieId) }
    }

    @Test
    fun `removeSeriesFromFavorite does nothing when account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        repository.removeSeriesFromFavorite(20L)

        coVerify(exactly = 0) { remoteDataSource.removeSeriesFromFavorite(any(), any()) }
    }

    @Test
    fun `removeSeriesFromFavorite calls remote data source when account id exists`() = runTest {
        val accountId = 123L
        val seriesId = 20L
        coEvery { localDataSource.getAccountId() } returns accountId
        coEvery { remoteDataSource.removeSeriesFromFavorite(accountId, seriesId) } just Runs

        repository.removeSeriesFromFavorite(seriesId)

        coVerify { remoteDataSource.removeSeriesFromFavorite(accountId, seriesId) }
    }

    @Test
    fun `getSeriesOfList returns list of series`() = runTest {
        val listId = 100L
        coEvery { localDataSource.getAccount() } returns mockk(relaxed = true)
        coEvery { remoteSeriesDataSource.getSeriesGenres() } returns listOf(
            GenreDto(id = 1, name = "Drama")
        )
        coEvery { remoteDataSource.getSeriesOfList(listId, 1) } returns listOf(
            SeriesRemoteDto()
        )

        val result = repository.getSeriesOfList(listId, 1)

        assertThat(result).isNotEmpty()
        coVerify { remoteDataSource.getSeriesOfList(listId, 1) }
    }
}