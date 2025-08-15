package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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
class AccountUseCaseTest {

    private val accountRepository: AccountRepository = mockk()
    private lateinit var useCase: AccountUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = AccountUseCase(accountRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAccountDetails returns account from repository`() = runTest {
        val expectedAccount = Account(
            id = 1, name = "Youssef",
            username = "pixelise",
            avatarPath = "/image.png"
        )

        coEvery { accountRepository.getAccountDetails() } returns expectedAccount

        val result = useCase.getAccountDetails()

        assertThat(result).isEqualTo(expectedAccount)
        coVerify { accountRepository.getAccountDetails() }
    }

    @Test
    fun `getSeriesLists returns series list from repository`() = runTest {
        val expectedLists = listOf(
            MediaList(id = 123, "seriesList1", mediaCount = 1),
            MediaList(id = 321, name = "seriesList2", mediaCount = 2)
        )

        coEvery { accountRepository.getSeriesLists(1) } returns expectedLists

        val result = useCase.getSeriesLists(1)

        assertThat(result).isEqualTo(expectedLists)
        coVerify { accountRepository.getSeriesLists(1) }
    }

    @Test
    fun `getMoviesLists returns movie list from repository`() = runTest {
        val expectedLists = listOf(
            MediaList(id = 123, "movieList1", mediaCount = 3),
            MediaList(id = 321, name = "movieList2", mediaCount = 4)
        )

        coEvery { accountRepository.getMovieLists(1) } returns expectedLists

        val result = useCase.getMoviesLists(1)

        assertThat(result).isEqualTo(expectedLists)
        coVerify { accountRepository.getMovieLists(1) }
    }

    @Test
    fun `getFavoriteMovies returns favorite movies from repository`() = runTest {
        val expectedMovies = listOf(
            Movie(
                1,
                "Inception",
                8.5f,
                "/poster.jpg",
                emptyList(),
                "A mind-bending thriller",
                1620000000,
                148,
                "/trailer.mp4"
            )
        )

        coEvery { accountRepository.getFavoriteMovies(1) } returns expectedMovies

        val result = useCase.getFavoriteMovies(1)

        assertThat(result).isEqualTo(expectedMovies)
        coVerify { accountRepository.getFavoriteMovies(1) }
    }

    @Test
    fun `getFavoriteSeries returns favorite series from repository`() = runTest {
        val expectedSeries = listOf(
            Series(
                2,
                "Dark",
                9.0f,
                "/poster.jpg",
                "/trailer.mp4",
                emptyList(),
                "Time travel mystery",
                1620000000,
                3
            )
        )

        coEvery { accountRepository.getFavoriteSeries(1) } returns expectedSeries

        val result = useCase.getFavoriteSeries(1)

        assertThat(result).isEqualTo(expectedSeries)
        coVerify { accountRepository.getFavoriteSeries(1) }
    }

    @Test
    fun `addMovieToFavorite calls repository with correct id`() = runTest {
        val movieId = 10L
        coEvery { accountRepository.addMovieToFavorite(movieId) } just Runs

        useCase.addMovieToFavorite(movieId)

        coVerify { accountRepository.addMovieToFavorite(movieId) }
    }

    @Test
    fun `addSeriesToFavorite calls repository with correct id`() = runTest {
        val seriesId = 20L
        coEvery { accountRepository.addSeriesToFavorite(seriesId) } answers { Unit }

        useCase.addSeriesToFavorite(seriesId)

        coVerify { accountRepository.addSeriesToFavorite(seriesId) }
    }

    @Test
    fun `addMovieToHistory calls repository with correct id`() = runTest {
        val movieId = 100L
        coEvery { accountRepository.addMovieToHistory(movieId) } answers { Unit }

        useCase.addMovieToHistory(movieId)

        coVerify { accountRepository.addMovieToHistory(movieId) }
    }

    @Test
    fun `addSeriesToHistory calls repository with correct id`() = runTest {
        val seriesId = 200L
        coEvery { accountRepository.addSeriesToHistory(seriesId) } answers { Unit }

        useCase.addSeriesToHistory(seriesId)

        coVerify { accountRepository.addSeriesToHistory(seriesId) }
    }

    @Test
    fun `getHistoryMovies returns movie history from repository`() = runTest {
        val expectedMovies = listOf(
            Movie(
                1,
                "The Matrix",
                9.0f,
                "/matrix.jpg",
                emptyList(),
                "Sci-fi classic",
                1610000000,
                136,
                "/trailer.mp4"
            )
        )

        coEvery { accountRepository.getHistoryMovies(1) } returns expectedMovies

        val result = useCase.getHistoryMovies(1)

        assertThat(result).isEqualTo(expectedMovies)
        coVerify { accountRepository.getHistoryMovies(1) }
    }

    @Test
    fun `getHistorySeries returns series history from repository`() = runTest {
        val expectedSeries = listOf(
            Series(
                3,
                "Breaking Bad",
                9.5f,
                "/bb.jpg",
                "/trailer.mp4",
                emptyList(),
                "Crime drama",
                1600000000,
                5
            )
        )

        coEvery { accountRepository.getHistorySeries(1) } returns expectedSeries

        val result = useCase.getHistorySeries(1)

        assertThat(result).isEqualTo(expectedSeries)
        coVerify { accountRepository.getHistorySeries(1) }
    }

    @Test
    fun `removeAccountDetails calls repository`() = runTest {
        coEvery { accountRepository.removeAccountDetails() } just Runs

        useCase.removeAccountDetails()

        coVerify { accountRepository.removeAccountDetails() }
    }

    @Test
    fun `removeMovieFromFavorite calls repository with correct id`() = runTest {
        val movieId = 10L
        coEvery { accountRepository.removeMovieFromFavorite(movieId) } just Runs

        useCase.removeMovieFromFavorite(movieId)

        coVerify { accountRepository.removeMovieFromFavorite(movieId) }
    }

    @Test
    fun `removeSeriesFromFavorite calls repository with correct id`() = runTest {
        val seriesId = 20L
        coEvery { accountRepository.removeSeriesFromFavorite(seriesId) } just Runs

        useCase.removeSeriesFromFavorite(seriesId)

        coVerify { accountRepository.removeSeriesFromFavorite(seriesId) }
    }

    @Test
    fun `removeMovieFromHistory calls repository with correct id`() = runTest {
        val movieId = 100L
        coEvery { accountRepository.removeMovieFromHistory(movieId) } just Runs

        useCase.removeMovieFromHistory(movieId)

        coVerify { accountRepository.removeMovieFromHistory(movieId) }
    }

    @Test
    fun `removeSeriesFromHistory calls repository with correct id`() = runTest {
        val seriesId = 200L
        coEvery { accountRepository.removeSeriesFromHistory(seriesId) } just Runs

        useCase.removeSeriesFromHistory(seriesId)

        coVerify { accountRepository.removeSeriesFromHistory(seriesId) }
    }

    @Test
    fun `getMoviesOfList returns movies from repository`() = runTest {
        val listId = 1L
        val page = 1
        val expectedMovies = listOf(
            Movie(
                1,
                "Inception",
                8.5f,
                "/poster.jpg",
                emptyList(),
                "A mind-bending thriller",
                1620000000,
                148,
                "/trailer.mp4"
            )
        )

        coEvery { accountRepository.getMoviesOfList(listId, page) } returns expectedMovies

        val result = useCase.getMoviesOfList(listId, page)

        assertThat(result).isEqualTo(expectedMovies)
        coVerify { accountRepository.getMoviesOfList(listId, page) }
    }

    @Test
    fun `getSeriesOfList returns series from repository`() = runTest {
        val listId = 2L
        val page = 1
        val expectedSeries = listOf(
            Series(
                2,
                "Dark",
                9.0f,
                "/poster.jpg",
                "/trailer.mp4",
                emptyList(),
                "Time travel mystery",
                1620000000,
                3
            )
        )

        coEvery { accountRepository.getSeriesOfList(listId, page) } returns expectedSeries

        val result = useCase.getSeriesOfList(listId, page)

        assertThat(result).isEqualTo(expectedSeries)
        coVerify { accountRepository.getSeriesOfList(listId, page) }
    }

    @Test
    fun `addMovieToList calls repository with correct ids`() = runTest {
        val listId = 1L
        val movieId = 50L
        coEvery { accountRepository.addMovieToList(listId, movieId) } just Runs

        useCase.addMovieToList(listId, movieId)

        coVerify { accountRepository.addMovieToList(listId, movieId) }
    }

    @Test
    fun `createList calls repository with correct name`() = runTest {
        val listName = "My Watchlist"
        coEvery { accountRepository.createList(listName) } just Runs

        useCase.createList(listName)

        coVerify { accountRepository.createList(listName) }
    }

    @Test
    fun `removeMovieFromList calls repository with correct ids`() = runTest {
        val listId = 1L
        val movieId = 99L
        coEvery { accountRepository.removeMovieFromList(listId, movieId) } just Runs

        useCase.removeMovieFromList(listId, movieId)

        coVerify { accountRepository.removeMovieFromList(listId, movieId) }
    }
}
