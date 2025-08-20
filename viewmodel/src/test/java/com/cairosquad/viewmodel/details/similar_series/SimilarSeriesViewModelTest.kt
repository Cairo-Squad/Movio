package com.cairosquad.viewmodel.details.similar_series

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SimilarSeriesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var manageSeriesUseCase: ManageSeriesUseCase
    private lateinit var viewModel: SimilarSeriesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        manageSeriesUseCase = mockk(relaxed = true)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = SimilarSeriesViewModel(manageSeriesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should set LOADING then SUCCESS and map series correctly when fetching similar series succeeds`() = runTest {
        val seriesId = 1L
        val seriesList = listOf(series1, series2)
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } returns seriesList

        viewModel.fetchSimilarSeries(seriesId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarSeriesScreenState.ScreenStatus.SUCCESS)
        assertThat(state.series).isEqualTo(seriesList.map { it.toUiState() })
        assertThat(state.errorStatus).isNull()
    }

    @Test
    fun `should set LOADING then ERROR and NO_INTERNET when fetching similar series fails with InternetConnectionException`() = runTest {
        val seriesId = 1L
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } throws InternetConnectionException()

        viewModel.fetchSimilarSeries(seriesId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarSeriesScreenState.ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NO_INTERNET)
        assertThat(state.series).isEmpty()
    }

    @Test
    fun `should set LOADING then ERROR and NETWORK_ERROR when fetching similar series fails with NetworkException`() = runTest {
        val seriesId = 1L
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } throws NetworkException()

        viewModel.fetchSimilarSeries(seriesId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarSeriesScreenState.ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(state.series).isEmpty()
    }

    @Test
    fun `should set LOADING then ERROR and UNKNOWN_ERROR when fetching similar series fails with generic exception`() = runTest {
        val seriesId = 1L
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } throws IOException()

        viewModel.fetchSimilarSeries(seriesId)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SimilarSeriesScreenState.ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(state.series).isEmpty()
    }

    @Test
    fun `should emit NavigateBack effect when onClickBack is called`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(SimilarSeriesEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit NavigateToSeriesDetails effect when onSeriesClicked is called`() = runTest {
        val seriesId = 123L
        viewModel.effect.test {
            viewModel.onSeriesClick(seriesId)
            assertThat(awaitItem()).isEqualTo(SimilarSeriesEffect.NavigateToSeriesDetails(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should map Series to SimilarSeriesUiState correctly`() {
        val series = series1
        val uiState = series.toUiState()
        assertThat(uiState.id).isEqualTo(series.id)
        assertThat(uiState.title).isEqualTo(series.title)
        assertThat(uiState.rating).isEqualTo(series.rating) // adjust if rating is rounded in toUiState()
        assertThat(uiState.posterUrl).isEqualTo(series.posterPath)
    }

    private companion object {
        val series1 = Series(
            id = 1,
            title = "Breaking Bad",
            rating = 4.8f,
            posterPath = "/series1.jpg",
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 5
        )

        val series2 = Series(
            id = 2,
            title = "Stranger Things",
            rating = 4.6f,
            posterPath = "/series2.jpg",
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 4
        )
    }
}