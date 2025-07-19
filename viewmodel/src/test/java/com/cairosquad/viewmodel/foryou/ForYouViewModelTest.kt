package com.cairosquad.viewmodel.foryou

import androidx.paging.PagingData
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ForYouViewModelTest {

    private lateinit var forYouPager: ForYouPager
    private lateinit var viewModel: ForYouViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        forYouPager = mockk()
        viewModel = ForYouViewModel(forYouPager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onRefresh should set isRefreshing true then false`() = runTest(testDispatcher) {
        // Given
        every { forYouPager.movies() } returns flowOf(PagingData.empty())

        // When
        viewModel.onRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.screenState.value.isRefreshing)
    }

    @Test
    fun `getForYouMovies should update forYou on success`() = runTest(testDispatcher) {
        // Given
        val dummyMovies = listOf(
            Movie(id = 1, title = "A", rating = 8f, posterPath = "a.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = ""),
            Movie(id = 2, title = "B", rating = 9f, posterPath = "b.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = "")
        )
        val dummyPagingData = PagingData.from(dummyMovies)
        every { forYouPager.movies() } returns flowOf(dummyPagingData)

        // When
        viewModel = ForYouViewModel(forYouPager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(
            SearchScreenState.ScreenStatus.LOADING,
            viewModel.screenState.value.screenStatus
        )
        assertEquals(null, viewModel.screenState.value.errorStatus)
    }

    @Test
    fun `handleSearchException should map MovioException correctly`() {
        // Given
        val networkException = NetworkException()
        val internetException = InternetConnectionException()
        val unknownException = UnknownException()

        // When & Then
        assertEquals(
            ErrorStatus.NETWORK_ERROR,
            viewModel.handleSearchException(networkException)
        )
        assertEquals(
            ErrorStatus.NO_INTERNET,
            viewModel.handleSearchException(internetException)
        )
        assertEquals(
            ErrorStatus.UNKNOWN_ERROR,
            viewModel.handleSearchException(unknownException)
        )
    }

    @Test
    fun `handleSearchException should map generic exception to UNKNOWN_ERROR`() {
        // Given
        val genericException = RuntimeException("Generic error")

        // When/Then
        assertEquals(
            ErrorStatus.UNKNOWN_ERROR,
            viewModel.handleSearchException(genericException)
        )
    }

    @Test
    fun `cacheMappedPagingData should map Movie to UiState correctly`() = runTest(testDispatcher) {
        // Given
        val originalMovies = listOf(
            Movie(id = 1, title = "Movie 1", rating = 8.5f, posterPath = "/poster1.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = ""),
            Movie(id = 2, title = "Movie 2", rating = 7.0f, posterPath = "/poster2.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = "")
        )
        val pagingData = PagingData.from(originalMovies)
        every { forYouPager.movies() } returns flowOf(pagingData)

        // When
        viewModel = ForYouViewModel(forYouPager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.screenState.value.forYou)
    }

    @Test
    fun `cacheMappedPagingData should handle empty PagingData`() = runTest(testDispatcher) {
        // Given
        every { forYouPager.movies() } returns flowOf(PagingData.empty())

        // When
        viewModel = ForYouViewModel(forYouPager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertNotNull(viewModel.screenState.value.forYou)
        assertEquals(
            SearchScreenState.ScreenStatus.LOADING,
            viewModel.screenState.value.screenStatus
        )
    }

    @Test
    fun `cacheMappedPagingData should cache data in viewModelScope`() = runTest(testDispatcher) {
        // Given
        val movies = listOf(movie1)
        val pagingData = PagingData.from(movies)
        every { forYouPager.movies() } returns flowOf(pagingData)

        // When
        viewModel = ForYouViewModel(forYouPager)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val cachedFlow = viewModel.screenState.value.forYou
        assertNotNull(cachedFlow)
    }


    companion object {
        private val movie1 = Movie(
            id = 1,
            title = "Test Movie",
            rating = 8.0f,
            posterPath = "/test.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )
    }
}