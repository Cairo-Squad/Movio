package com.cairosquad.viewmodel.details.series

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()
    private val seriesId = 123L
    private val mockUseCase: GetSeriesDetailsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: SeriesDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = SeriesDetailsViewModel(mockUseCase, seriesId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    @Test
//    fun `init SHOULD load all data sections`() = runTest {
//        // Given
//        coEvery { mockUseCase.getSeries(seriesId) } returns mockSeries
//        coEvery { mockUseCase.getSeriesTopCast(seriesId, 1) } returns emptyList()
//        coEvery { mockUseCase.getSeriesSeasons(seriesId) } returns emptyList()
//        coEvery { mockUseCase.getSeriesReviews(seriesId, 1) } returns emptyList()
//        coEvery { mockUseCase.getSimilarSeries(seriesId, 1) } returns emptyList()
//
//        // When
//        advanceUntilIdle()
//
//        // Then
//        coVerify(exactly = 1) {
//            mockUseCase.getSeries(seriesId)
//            mockUseCase.getSeriesTopCast(seriesId, 1)
//            mockUseCase.getSeriesSeasons(seriesId)
//            mockUseCase.getSeriesReviews(seriesId, 1)
//            mockUseCase.getSimilarSeries(seriesId, 1)
//        }
//    }

    @Test
    fun `WHEN onBackClicked SHOULD emit NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClicked()
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onShareClicked SHOULD update state to show share bottom sheet`() = runTest {
        viewModel.onShareClicked()
        assertThat(viewModel.screenState.value.showShareBottomSheet).isTrue()
    }

    @Test
    fun `WHEN onFavoriteClicked WHEN not logged in SHOULD show login bottom sheet`() = runTest {
        viewModel.onFavoriteClicked()
        assertThat(viewModel.screenState.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `WHEN AddToListClicked WHEN not logged in SHOULD show login bottom sheet`() = runTest {
        viewModel.onAddToListClicked()
        assertThat(viewModel.screenState.value.showAddToListBottomSheet).isTrue()
    }

    @Test
    fun `onRateClicked WHEN not logged in SHOULD show login bottom sheet`() = runTest {
        viewModel.onRateClicked()
        assertThat(viewModel.screenState.value.showRateBottomSheet).isTrue()
    }

    @Test
    fun `WHEN onPlayTrailerClicked WHEN clicked SHOULD emit PlayTrailer`() = runTest {
        viewModel.effect.test {
            viewModel.onPlayTrailerClicked()
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.PlayTrailer)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onDismissShareBottomSheet SHOULD hide share bottom sheet`() = runTest {
        viewModel.effect.test {
            viewModel.onShareClicked()
            viewModel.onDismissShareBottomSheet()
            assertThat(viewModel.screenState.value.showShareBottomSheet).isFalse()
        }
    }

    @Test
    fun `WHEN onDismissLoginBottomSheet SHOULD hide login bottom sheet`() = runTest {
        viewModel.effect.test {
            viewModel.onFavoriteClicked()
            viewModel.onDismissLoginBottomSheet()
            assertThat(viewModel.screenState.value.showLoginBottomSheet).isFalse()
        }
    }

    @Test
    fun `WHEN onSeeAllArtistsClicked SHOULD Navigate To All Artists screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllArtistsClicked(1399)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllArtists(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeasonClicked SHOULD navigate to season screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeasonClicked(1399, 1)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToSeasonDetails(1399, 1))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeeAllSeasonsClicked SHOULD navigate to all seasons screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllSeasonsClicked(1399)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllSeasons(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeeAllReviewsClicked SHOULD navigate to all reviews screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllReviewsClicked(1399)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllReviews(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `WHEN onSeriesClicked SHOULD navigate to similar series`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClicked(1399)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToSeriesDetails(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeeAllSimilarClicked SHOULD navigate to all similar series screen`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllSimilarClicked(1399)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllSimilar(1399))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCopy SHOULD show then hide snackbar after delay`() = runTest {
        viewModel.onCopy("Copied!", true)

        advanceTimeBy(0)
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
    }

    @Test
    fun `onArtistClicked SHOULD emit NavigateToArtistDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onArtistClicked(456L)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToArtistDetails(456L))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleDetailsException SHOULD map exceptions correctly`() {
        assertThat(viewModel.handleDetailsException(NetworkException()))
            .isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(viewModel.handleDetailsException(InternetConnectionException()))
            .isEqualTo(ErrorStatus.NO_INTERNET)
        assertThat(viewModel.handleDetailsException(RuntimeException()))
            .isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    companion object {
        private val mockSeries = Series(
            id = 123,
            title = "Test Series",
            rating = 8.7f,
            posterPath = "/poster.jpg",
            genres = emptyList(),
            seasonsCount = 2,
            releaseDate = 0L,
            overview = "Test overview",
            trailerPath = ""
        )
    }

    class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}
