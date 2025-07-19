package com.cairosquad.viewmodel.details.top_cast

import com.cairosquad.domain.usecase.movies.GetMovieDetailsUseCase
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Artist
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopCastViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getSeriesDetailsUseCase: GetSeriesDetailsUseCase
    private lateinit var viewModel: TopCastViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getMovieDetailsUseCase = mockk(relaxed = true)
        getSeriesDetailsUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTopCast should return movie cast when isMovie is true`() = testScope.runTest {
        // Given
        coEvery { getMovieDetailsUseCase.getMovieTopCast(MOVIE_ID) } returns testCast
        viewModel = TopCastViewModel(
            mediaId = MOVIE_ID,
            isMovie = true,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getSeriesDetailsUseCase = getSeriesDetailsUseCase,
            dispatcher = testDispatcher
        )

        // When
        viewModel.getTopCast()
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.cast).hasSize(1)
        assertThat(state.cast.first().name).isEqualTo("Joseph Mawle")
        assertThat(state.error).isNull()
    }

    @Test
    fun `getTopCast should return series cast when isMovie is false`() = testScope.runTest {
        // Given
        coEvery { getSeriesDetailsUseCase.getSeriesTopCast(SERIES_ID, page = 1) } returns testCast
        viewModel = TopCastViewModel(
            mediaId = SERIES_ID,
            isMovie = false,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getSeriesDetailsUseCase = getSeriesDetailsUseCase,
            dispatcher = testDispatcher
        )

        // When
        viewModel.getTopCast()
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.cast).hasSize(1)
        assertThat(state.cast.first().name).isEqualTo("Joseph Mawle")
        assertThat(state.error).isNull()
    }

    @Test
    fun `getTopCast should return error when use case throws exception`() = testScope.runTest {
        // Given
        coEvery { getMovieDetailsUseCase.getMovieTopCast(MOVIE_ID) } throws Exception(ERROR_MESSAGE)
        viewModel = TopCastViewModel(
            mediaId = MOVIE_ID,
            isMovie = true,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            getSeriesDetailsUseCase = getSeriesDetailsUseCase,
            dispatcher = testDispatcher
        )

        // When
        viewModel.getTopCast()
        advanceUntilIdle()

        // Then
        val state = viewModel.screenState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.cast).isEmpty()
        assertThat(state.error).isEqualTo(ERROR_MESSAGE)
    }

    private companion object {
        const val MOVIE_ID = 101L
        const val SERIES_ID = 202L
        const val ERROR_MESSAGE = "Network error"

        val testCast = listOf(
            Artist(
                id = 1,
                name = "Joseph Mawle",
                photoPath = "/1Ocb9v3h54beGVoJMm4w50UQhLf.jpg",
                country = "United Kingdom",
                birthDate = 322704000000L,
                biography = "Joseph Mawle, born in Oxford...",
                department = "Acting"
            )
        )
    }
}
