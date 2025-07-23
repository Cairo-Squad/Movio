package com.cairosquad.viewmodel.foryou

import androidx.paging.PagingData
import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForYouViewModelTest {

    private lateinit var forYouPager: ForYouPager
    private lateinit var viewModel: ForYouViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        forYouPager = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `onRefresh should toggle isRefreshing`() = runTest {
        every { forYouPager.movies() } returns flowOf(PagingData.empty())
        viewModel = ForYouViewModel(forYouPager)

        viewModel.onRefresh()
        testScheduler.advanceUntilIdle()

        assertFalse(viewModel.screenState.value.isRefreshing)
    }

    @Test
    fun `getForYouMovies should emit data on success`() = runTest {
        every { forYouPager.movies() } returns flowOf(PagingData.from(listOf(movie1)))
        viewModel = ForYouViewModel(forYouPager)
        testScheduler.advanceUntilIdle()

        assertNotNull(viewModel.screenState.value.forYou)
        assertEquals(ForYouState.ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
    }

    @Test
    fun `handleSearchException maps known exceptions correctly`() {
        viewModel = ForYouViewModel(forYouPager)
        assertEquals(ErrorStatus.NETWORK_ERROR, viewModel.handleSearchException(NetworkException()))
        assertEquals(ErrorStatus.NO_INTERNET, viewModel.handleSearchException(InternetConnectionException()))
        assertEquals(ErrorStatus.UNKNOWN_ERROR, viewModel.handleSearchException(UnknownException()))
    }

    @Test
    fun `handleSearchException maps unknown exception to UNKNOWN_ERROR`() {
        viewModel = ForYouViewModel(forYouPager)
        assertEquals(ErrorStatus.UNKNOWN_ERROR, viewModel.handleSearchException(RuntimeException("unexpected")))
    }

    @Test
    fun `cacheMappedPagingData should cache and map correctly`() = runTest {
        every { forYouPager.movies() } returns flowOf(PagingData.from(listOf(movie1)))
        viewModel = ForYouViewModel(forYouPager)
        testScheduler.advanceUntilIdle()

        assertNotNull(viewModel.screenState.value.forYou)
    }

    @Test
    fun `cacheMappedPagingData should handle empty data`() = runTest {
        every { forYouPager.movies() } returns flowOf(PagingData.empty())
        viewModel = ForYouViewModel(forYouPager)
        testScheduler.advanceUntilIdle()

        assertNotNull(viewModel.screenState.value.forYou)
        assertEquals(ForYouState.ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
    }

    @Test
    fun `onMovieClicked should emit NavigateToMovieDetails effect`() = runTest {
        every { forYouPager.movies() } returns flowOf(PagingData.empty())
        viewModel = ForYouViewModel(forYouPager)

        viewModel.effect.test {
            viewModel.onMovieClicked(10L)
            assertEquals(ForYouEffect.NavigateToMovieDetails(10L), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    companion object {
        private val movie1 = Movie(
            id = 1,
            title = "Movie Test",
            rating = 8.0f,
            posterPath = "/poster.jpg",
            genres = emptyList(),
            overview = "Test",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )
    }
}
