package com.cairosquad.viewmodel.episodesDetailsViewModel

import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsViewModel
import com.cairosquad.viewmodel.details.movie.MovieViewModelTest.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EpisodesDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: EpisodesDetailsViewModel
    private val useCase: GetSeriesDetailsUseCase = mockk()
    private val seriesId = 123L
    private val initialSeason = 1

    @Before
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        coEvery { useCase.getSeriesSeasons(seriesId) } returns listOf(
            Season(
                seasonNumber = 1, episodesCount = 2, posterPath = "url1",
                seriesId = 123L, seasonName = "Season 1",
                rating = 8.5f, overview = "Overview", airDate = 1234567890
            ),
            Season(
                seasonNumber = 2, episodesCount = 3, posterPath = "url2",
                seriesId = 123L, seasonName = "Season 2",
                rating = 8.2f, overview = "Overview", airDate = 1234567890
            )
        )

        coEvery { useCase.getEpisodes(seriesId, initialSeason) } returns listOf(
            Episode(
                id = 1, episodeName = "Ep1", episodeNumber = 1, photoPath = "img1",
                runtimeMinutes = 45, rating = 9.0f, seasonNumber = 1,
                seriesId = 123L
            ),
            Episode(
                id = 2, episodeName = "Ep2", episodeNumber = 2, photoPath = "img2",
                runtimeMinutes = 44, rating = 8.5f, seasonNumber = 1,
                seriesId = 123L
            )
        )

        viewModel = EpisodesDetailsViewModel(useCase, seriesId, initialSeason)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load seasons and episodes`() = runTest {

        val state = viewModel.screenState.value
        advanceUntilIdle()
        assertEquals(ScreenStatus.SUCCESS, state.episodesSectionState)
        assertEquals(2, state.episodes.size)
        assertEquals(2, state.seasons.size)
        assertEquals(initialSeason, state.selectedSeasonNumber)
        assertEquals(initialSeason, state.currentSeasonNumber)
    }

    @Test
    fun `onSeasonsDropdownClick should toggle dropdown state`() = runTest(testDispatcher) {
        advanceUntilIdle()
        val initial = viewModel.screenState.value.isSeasonDropdownExpanded
        viewModel.onSeasonsDropdownClick()
        val toggled = viewModel.screenState.value.isSeasonDropdownExpanded
        assertEquals(!initial, toggled)
    }

//    @Test
//    fun `onBackClick should emit NavigateBack effect`() = runTest {
//        viewModel.effect.test {
//            viewModel.onBackClick()
//            assertEquals(EpisodesDetailEffect.NavigateBack, awaitItem())
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `init should handle failure when loading seasons`() = runTest(testDispatcher) {
//        coEvery { useCase.getSeriesSeasons(seriesId) } throws RuntimeException("Network error")
//
//        viewModel = EpisodesDetailsViewModel(useCase, seriesId, initialSeason)
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        assertEquals(ScreenStatus.ERROR, state.episodesSectionState)
//        assertTrue(state.seasons.isEmpty())
//    }
//
//    @Test
//    fun `onSeasonSelected should handle failure and set error state`() = runTest(testDispatcher) {
//        advanceUntilIdle()
//
//        val newSeason = initialSeason + 1
//
//        coEvery { useCase.getEpisodes(seriesId, newSeason) } throws RuntimeException("Failed to fetch episodes")
//
//        viewModel.onSeasonSelected(seriesId, newSeason)
//        advanceUntilIdle()
//
//        val state = viewModel.screenState.value
//        assertEquals(ScreenStatus.ERROR, state.episodesSectionState)
//        assertTrue(state.episodes.isEmpty())
//    }

    @Test
    fun `onSeasonSelected with same season number should do nothing`() = runTest(testDispatcher) {
        advanceUntilIdle()

        val stateBefore = viewModel.screenState.value

        // Same season selected again
        viewModel.onSeasonSelected(seriesId, stateBefore.selectedSeasonNumber)
        advanceUntilIdle()

        val stateAfter = viewModel.screenState.value

        assertEquals(stateBefore, stateAfter) // No change
    }

    @Test
    fun `onSeasonSelected should collapse dropdown`() = runTest(testDispatcher) {
        advanceUntilIdle()

        viewModel.onSeasonsDropdownClick()
        assertTrue(viewModel.screenState.value.isSeasonDropdownExpanded)

        val newSeason = initialSeason + 1
        coEvery { useCase.getEpisodes(seriesId, newSeason) } returns emptyList()

        viewModel.onSeasonSelected(seriesId, newSeason)
        advanceUntilIdle()

        assertFalse(viewModel.screenState.value.isSeasonDropdownExpanded)
    }

    @Test
    fun `onSeasonSelected should set loading state before fetching episodes`() = runTest(testDispatcher) {
        advanceUntilIdle()

        val newSeason = initialSeason + 1
        coEvery { useCase.getEpisodes(seriesId, newSeason) } coAnswers {
            assertEquals(ScreenStatus.LOADING, viewModel.screenState.value.episodesSectionState)
            emptyList()
        }

        viewModel.onSeasonSelected(seriesId, newSeason)
        advanceUntilIdle()
    }

//    @Test
//    fun `onBackClick emits only NavigateBack effect`() = runTest {
//        viewModel.effect.test {
//            viewModel.onBackClick()
//            assertEquals(EpisodesDetailEffect.NavigateBack, awaitItem())
//            expectNoEvents()
//        }
//    }

}

