package com.cairosquad.viewmodel.foryou

import androidx.paging.PagingData
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState.ScreenStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForYouViewModelTest {

    private lateinit var forYouPager: ForYouPager
    private lateinit var viewModel: ForYouViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        forYouPager = mockk(relaxed = true)
        viewModel = ForYouViewModel(forYouPager)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain() // 👈 إعادة Dispatcher للوضع الطبيعي
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Then
        assertEquals(ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
    }
    @Test
    fun `onRefresh should set isRefreshing true then false`() = runTest(testDispatcher) {
        // When
        viewModel.onRefresh()

        // Wait for coroutines to finish
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.screenState.value.isRefreshing)
    }
    @Test
    fun `getForYouMovies should update forYou on success`() = runTest {
        // Given
        val dummyPagingData = PagingData.from(
            listOf(
                Movie(id = 1, title = "A", rating = 8f, posterPath = "a.jpg"),
                Movie(id = 2, title = "B", rating = 9f, posterPath = "b.jpg")
            )
        )
        val fakeFlow = flowOf(dummyPagingData)
        every { forYouPager.movies() } returns fakeFlow

        // When
        viewModel = ForYouViewModel(forYouPager) // يعيد استدعاء getForYouMovies

        // Then
        advanceUntilIdle()
        assertEquals(ScreenStatus.LOADING, viewModel.screenState.value.screenStatus)
    }
    @Test
    fun `getForYouMovies should set errorStatus UNKNOWN_ERROR on generic exception`() = runTest(testDispatcher) {
        // Arrange
        every { forYouPager.movies() } returns flow {
            throw RuntimeException("Unexpected")
        }

        // Act
        viewModel = ForYouViewModel(forYouPager)
        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertEquals(ScreenStatus.FAILED, state.screenStatus)
        assertEquals(ErrorStatus.UNKNOWN_ERROR, state.errorStatus)
    }
    @Test
    fun `getForYouMovies should set errorStatus NO_INTERNET on InternetConnectionException`() = runTest(testDispatcher) {
        // Arrange
        every { forYouPager.movies() } returns flow {
            throw InternetConnectionException()
        }

        // Act
        viewModel = ForYouViewModel(forYouPager)
        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertEquals(ScreenStatus.FAILED, state.screenStatus)
        assertEquals(ErrorStatus.NO_INTERNET, state.errorStatus)
    }
    @Test
    fun `getForYouMovies should set errorStatus UNKNOWN_ERROR on UnknownException`() = runTest(testDispatcher) {
        // Arrange
        every { forYouPager.movies() } returns flow {
            throw UnknownException()
        }

        // Act
        viewModel = ForYouViewModel(forYouPager)
        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertEquals(ScreenStatus.FAILED, state.screenStatus)
        assertEquals(ErrorStatus.UNKNOWN_ERROR, state.errorStatus)
    }
    @Test
    fun `handleSearchException should map MovioException correctly`() {
        // Given
        val networkException = NetworkException()
        val internetException = InternetConnectionException()
        val unknownException = UnknownException()

        // When/Then
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
    fun `getForYouMovies should update screen status to FAILED on error`() = runTest(testDispatcher) {
        // Arrange
        every { forYouPager.movies() } returns flow {
            throw NetworkException()
        }

        // Act
        viewModel = ForYouViewModel(forYouPager)

        // Assert
        advanceUntilIdle()
        val state = viewModel.screenState.value
        assertEquals(ScreenStatus.FAILED, state.screenStatus)
        assertEquals(ErrorStatus.NETWORK_ERROR, state.errorStatus)
    }
    @Test
    fun `getForYouMovies should update screen status to FAILED on error1`() = runTest(UnconfinedTestDispatcher()) {
        // Arrange
        every { forYouPager.movies() } returns flow {
            emit(throw NetworkException()) // trigger the exception during collection
        }

        // Act
        viewModel = ForYouViewModel(forYouPager)

        // Assert
        advanceUntilIdle()
        val state = viewModel.screenState.value
        assertEquals(ScreenStatus.FAILED, state.screenStatus)
        assertEquals(ErrorStatus.NETWORK_ERROR, state.errorStatus)
    }

    companion object {
        private val movie1 = Movie(
            id = 1,
            title = "Test Movie",
            rating = 8.0f,
            posterPath = "/test.jpg"
        )
    }
}
