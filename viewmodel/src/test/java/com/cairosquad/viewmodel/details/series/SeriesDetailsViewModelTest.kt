package com.cairosquad.viewmodel.details.series

import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.model.RatingResult
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState.SectionStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val manageSeriesUseCase: ManageSeriesUseCase = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val accountUseCase: AccountUseCase = mockk(relaxed = true)
    private val ratedItemsUseCase: GetRatedItemsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: SeriesDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }


    @Test
    fun `should convert to error state when get series by id fails`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesById(seriesId) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.basicDetailsSectionState).isEqualTo(SectionStatus.ERROR)
    }

    @Test
    fun `should convert to error state when get series top cast fails`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesTopCast(seriesId, 1) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.castSectionState).isEqualTo(SectionStatus.ERROR)
    }

    @Test
    fun `should convert to error state when get series seasons fails`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesSeasons(seriesId) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.seasonsSectionState).isEqualTo(SectionStatus.ERROR)
    }

    @Test
    fun `should convert to error state when get series reviews fails`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesReviews(seriesId, 1) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.reviewsSectionState).isEqualTo(SectionStatus.ERROR)
    }

    @Test
    fun `should convert to error state when get similar series fails`() = runTest {
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        assertThat(viewModel.screenState.value.similarSeriesSectionState).isEqualTo(SectionStatus.ERROR)
    }

    @Test
    fun `should do nothing when get favorite series fails and user is logged in`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        coEvery { accountUseCase.getFavoriteSeries(1) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isFavorite).isFalse()
    }

    @Test
    fun `should do nothing when add series to history fails`() = runTest {
        coEvery { accountUseCase.addSeriesToHistory(seriesId) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isFavorite).isFalse()
    }

    @Test
    fun `should do nothing when check user login fails`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isFavorite).isFalse()
    }

    @Test
    fun `should do nothing when load favorite series fails`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        coEvery { accountUseCase.getFavoriteSeries(1) } throws RuntimeException()
        val viewModel = SeriesDetailsViewModel(
            manageSeriesUseCase,
            loginUseCase,
            accountUseCase,
            ratedItemsUseCase,
            seriesId
        )
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isFavorite).isFalse()
    }

    @Test
    fun `should emit NavigateBack effect when back clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onBackClicked()
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should show share bottom sheet when share clicked`() = runTest {
        viewModel.onShareClicked()
        assertThat(viewModel.screenState.value.showShareBottomSheet).isTrue()
    }

    @Test
    fun `should emit PlayTrailer effect when play trailer clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onPlayTrailerClicked()
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.PlayTrailer)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should hide share bottom sheet when dismiss share bottom sheet`() = runTest {
        viewModel.onShareClicked()
        viewModel.onDismissShareBottomSheet()
        assertThat(viewModel.screenState.value.showShareBottomSheet).isFalse()
    }

    @Test
    fun `should navigate to all artists screen when see all artists clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllArtistsClicked(seriesId)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllArtists(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to season details screen when season clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeasonClicked(seriesId, 1)
            assertThat(awaitItem()).isEqualTo(
                SeriesDetailEffect.NavigateToSeasonDetails(
                    seriesId,
                    1
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to all seasons screen when see all seasons clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllSeasonsClicked(seriesId)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllSeasons(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to all reviews screen when see all reviews clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllReviewsClicked(seriesId)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllReviews(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to series details screen when series clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClicked(seriesId)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToSeriesDetails(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to all similar series screen when see all similar clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllSimilarClicked(seriesId)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToAllSimilar(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit NavigateToLogin effect when navigate to login clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onNavigateToLogin()
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit NavigateToArtistDetails effect when artist clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onArtistClicked(artist.id)
            assertThat(awaitItem()).isEqualTo(SeriesDetailEffect.NavigateToArtistDetails(artist.id))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should map exceptions correctly when handling details exception`() = runTest {
        assertThat(viewModel.handleDetailsException(NetworkException())).isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(viewModel.handleDetailsException(InternetConnectionException())).isEqualTo(
            ErrorStatus.NO_INTERNET
        )
        assertThat(viewModel.handleDetailsException(RuntimeException())).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should open login bottom sheet when favorite clicked and not logged in`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns false
        viewModel.onFavoriteClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `should add to favorites when favorite clicked and logged in and not favorite`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        viewModel.updateState { it.copy(isFavorite = false) }
        coEvery { accountUseCase.addSeriesToFavorite(seriesId) } returns Unit
        viewModel.onFavoriteClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isFavorite).isTrue()
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
    }

    @Test
    fun `should remove from favorites when favorite clicked and logged in and favorite`() =
        runTest {
            coEvery { loginUseCase.isUserLoggedIn() } returns true
            viewModel.updateState { it.copy(isFavorite = true) }
            coEvery { accountUseCase.removeSeriesFromFavorite(seriesId) } returns Unit
            viewModel.onFavoriteClicked()
            advanceUntilIdle()
            assertThat(viewModel.screenState.value.isFavorite).isFalse()
            assertThat(viewModel.screenState.value.showSnackBar).isFalse()
        }

    @Test
    fun `should show error snackbar when add to favorite fails`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        viewModel.updateState { it.copy(isFavorite = false) }
        coEvery { accountUseCase.addSeriesToFavorite(seriesId) } throws RuntimeException()
        viewModel.onFavoriteClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
        assertThat(viewModel.screenState.value.isProcessSuccess).isFalse()
    }

    @Test
    fun `should show error snackbar when remove from favorite fails`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        viewModel.updateState { it.copy(isFavorite = true) }
        coEvery { accountUseCase.removeSeriesFromFavorite(seriesId) } throws RuntimeException()
        viewModel.onFavoriteClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
        assertThat(viewModel.screenState.value.isProcessSuccess).isFalse()
    }

	@Test
	fun `should show login bottom sheet when rate clicked and not logged in`() = runTest {
		coEvery { loginUseCase.isUserLoggedIn() } returns false
		viewModel.onRateClicked()
		advanceUntilIdle()
		assertThat(viewModel.screenState.value.showLoginBottomSheet).isTrue()
	}

    @Test
    fun `should open rate bottom sheet when rate clicked and logged in`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        viewModel.onRateClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.showRateBottomSheet).isTrue()
    }

    @Test
    fun `should update state when create list clicked`() = runTest {
        viewModel.onCreateListClicked()
        assertThat(viewModel.screenState.value.showCreateListBottomSheet).isTrue()
        assertThat(viewModel.screenState.value.showAddToListBottomSheet).isFalse()
    }

    @Test
    fun `should hide create list bottom sheet when dismiss create list bottom sheet`() = runTest {
        viewModel.updateState { it.copy(showCreateListBottomSheet = true) }
        viewModel.onDismissCreateListBottomSheet()
        assertThat(viewModel.screenState.value.showCreateListBottomSheet).isFalse()
    }

    @Test
    fun `should hide login bottom sheet when dismiss login bottom sheet`() = runTest {
        viewModel.updateState { it.copy(showLoginBottomSheet = true) }
        viewModel.onDismissLoginBottomSheet()
        assertThat(viewModel.screenState.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `should hide rate bottom sheet when dismiss rate bottom sheet`() = runTest {
        viewModel.updateState { it.copy(showRateBottomSheet = true) }
        viewModel.onDismissRateBottomSheet()
        assertThat(viewModel.screenState.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `should hide add to list bottom sheet when dismiss add to list bottom sheet`() = runTest {
        viewModel.updateState { it.copy(showAddToListBottomSheet = true) }
        viewModel.onDismissAddToListBottomSheet()
        assertThat(viewModel.screenState.value.showAddToListBottomSheet).isFalse()
    }

    @Test
    fun `should update list name when value changed`() = runTest {
        viewModel.onValueChange("New List")
        assertThat(viewModel.screenState.value.newListName).isEqualTo("New List")
    }

    @Test
    fun `should update rating when rate changed`() = runTest {
        viewModel.onRateChange(8)
        assertThat(viewModel.screenState.value.rating).isEqualTo(8)
    }

    @Test
    fun `should close bottom sheet and show success when submit rate clicked`() = runTest {
        coEvery { manageSeriesUseCase.addSeriesRating(seriesId, 5f) } returns RatingResult(
            1,
            "Rated successfully"
        )
        viewModel.onSubmitRateClicked(5)
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isRated).isTrue()
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
    }

    @Test
    fun `should reload all data when refresh triggered`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesById(seriesId) } returns mockSeries
        viewModel.onRefresh()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.basicDetailsSectionState).isEqualTo(SectionStatus.SUCCESS)
    }

    @Test
    fun `should update error status when setError called`() = runTest {
        val exception = InternetConnectionException()
        viewModel.updateState { it.copy(basicDetailsSectionState = SectionStatus.LOADING) }
        viewModel.handleDetailsException(exception)
        val status = viewModel.handleDetailsException(exception)
        assertThat(status).isEqualTo(ErrorStatus.NO_INTERNET)
    }

    @Test
    fun `should set SUCCESS and map series when get series by id succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesById(seriesId) } returns mockSeries
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.basicDetailsSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.series?.id).isEqualTo(mockSeries.id)
    }

    @Test
    fun `should set SUCCESS and map cast when get top cast succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesTopCast(seriesId, 1) } returns listOf(artist)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.castSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.cast).isNotEmpty()
    }

    @Test
    fun `should set SUCCESS and map seasons when get seasons succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesSeasons(seriesId) } returns listOf(season)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.seasonsSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.seasons).isNotEmpty()
    }

    @Test
    fun `should set SUCCESS and map reviews when get reviews succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesReviews(seriesId, 1) } returns listOf(review)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.reviewsSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.reviews).isNotEmpty()
    }

    @Test
    fun `should set SUCCESS and map similar when get similar succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } returns listOf(mockSeries)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.similarSeriesSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.similarSeries).isNotEmpty()
    }

    @Test
    fun `should show success rated bottom sheet when rate clicked and already rated`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns true
        viewModel.updateState { it.copy(isRated = true) }
        viewModel.onRateClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.showSuccessRatedBottomSheet).isTrue()
        assertThat(viewModel.screenState.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `should hide success rated bottom sheet when dismissed`() = runTest {
        viewModel.updateState { it.copy(showSuccessRatedBottomSheet = true) }
        viewModel.onDismissSuccessRatedBottomSheet()
        assertThat(viewModel.screenState.value.showSuccessRatedBottomSheet).isFalse()
    }

    @Test
    fun `should open login bottom sheet when add to list clicked and not logged in`() = runTest {
        coEvery { loginUseCase.isUserLoggedIn() } returns false
        viewModel.onAddToListClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.showLoginBottomSheet).isTrue()
        assertThat(viewModel.screenState.value.showSnackBar).isFalse()
    }

    @Test
    fun `should show error snackbar when submit rate fails`() = runTest {
        coEvery { manageSeriesUseCase.addSeriesRating(seriesId, any()) } throws RuntimeException()
        viewModel.onSubmitRateClicked(4)
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isProcessSuccess).isFalse()
    }

    @Test
    fun `should set SUCCESS state and map series when get series by id succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesById(seriesId) } returns mockSeries
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.basicDetailsSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.series?.id).isEqualTo(mockSeries.id)
    }

    @Test
    fun `should set SUCCESS state and map cast when get top cast succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesTopCast(seriesId, 1) } returns listOf(artist)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.castSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.cast).isNotEmpty()
    }

    @Test
    fun `should set SUCCESS state and map seasons when get seasons succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesSeasons(seriesId) } returns listOf(season)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.seasonsSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.seasons).isNotEmpty()
    }

    @Test
    fun `should set SUCCESS state and map reviews when get reviews succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSeriesReviews(seriesId, 1) } returns listOf(review)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.reviewsSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.reviews).isNotEmpty()
    }

    @Test
    fun `should set SUCCESS state and map similar series when get similar succeeds`() = runTest {
        coEvery { manageSeriesUseCase.getSimilarSeries(seriesId, 1) } returns listOf(mockSeries)
        val vm = SeriesDetailsViewModel(
            manageSeriesUseCase, loginUseCase, accountUseCase, ratedItemsUseCase, seriesId
        )
        advanceUntilIdle()
        assertThat(vm.screenState.value.similarSeriesSectionState).isEqualTo(SectionStatus.SUCCESS)
        assertThat(vm.screenState.value.similarSeries).isNotEmpty()
    }

    companion object {
        private const val seriesId = 123L
        private val mockSeries = Series(
            id = 123,
            title = "Test Series",
            rating = 8.7f,
            posterPath = "/poster.jpg",
            genres = emptyList(),
            seasonsCount = 2,
            releaseDate = 0L,
            overview = "Overview",
            trailerPath = ""
        )
        private val season = Season(
            seasonNumber = 1,
            seasonName = "season",
            seriesId = 1,
            episodesCount = 25,
            rating = 6.5f,
            posterPath = "/poster.png",
            overview = "overVIew",
            airDate = 321L
        )
        private val artist = Artist(
            id = 303L,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
        private val review = Review(
            id = "1",
            author = "Author",
            authorPhotoPath = "",
            rating = 5f,
            date = 0L,
            description = "content"
        )
    }

    class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}