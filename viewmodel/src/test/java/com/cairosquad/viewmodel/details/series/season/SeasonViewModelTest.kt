package com.cairosquad.viewmodel.details.series.season

import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.details.series.season.SeasonDetailsScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SeasonViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var getSeriesDetailsUseCase: GetSeriesDetailsUseCase
    private lateinit var viewModel: SeasonsViewModel

    private val seriesId = 1L
    private val seasonNumber = 2

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        getSeriesDetailsUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load season and episodes successfully`() = runTest {
        // Arrange
        val mockSeasons = listOf(
            Season(1, 2, "S2", 10, 8.0f, "path.jpg", "overview", 2020L)
        )
        val mockEpisodes = listOf(
            Episode(1, 1, "e1.jpg", "Ep1", 45, 8.0f, 2, 1)
        )
        coEvery { getSeriesDetailsUseCase.getSeriesSeasons(any()) } returns mockSeasons
        coEvery { getSeriesDetailsUseCase.getEpisodes(any(), any()) } returns mockEpisodes

        // Act
        viewModel = SeasonsViewModel(getSeriesDetailsUseCase, dispatcher, seriesId, seasonNumber)

        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertThat(state.seasonSectionState).isEqualTo(ScreenStatus.SUCCESS)
        assertThat(state.episodesSectionState).isEqualTo(ScreenStatus.SUCCESS)
    }

    @Test
    fun `should show error state when getSeriesSeasons throws exception`() = runTest {
        // Arrange
        coEvery { getSeriesDetailsUseCase.getSeriesSeasons(any()) } throws IOException()
        coEvery { getSeriesDetailsUseCase.getEpisodes(any(), any()) } returns emptyList()

        // Act
        viewModel = SeasonsViewModel(getSeriesDetailsUseCase, dispatcher, seriesId, seasonNumber)
        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertThat(state.seasonSectionState).isEqualTo(ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should show error state when getEpisodes throws exception`() = runTest {
        // Arrange
        coEvery { getSeriesDetailsUseCase.getSeriesSeasons(any()) } returns emptyList()
        coEvery { getSeriesDetailsUseCase.getEpisodes(any(), any()) } throws IOException()

        // Act
        viewModel = SeasonsViewModel(getSeriesDetailsUseCase, dispatcher, seriesId, seasonNumber)
        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertThat(state.episodesSectionState).isEqualTo(ScreenStatus.ERROR)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should emit NavigateBack effect manually`() = runTest {
        viewModel = SeasonsViewModel(getSeriesDetailsUseCase, dispatcher, seriesId, seasonNumber)
        advanceUntilIdle()

        var emittedEffect: SeasonDetailEffect? = null
        val job = launch {
            viewModel.effect.collect {
                emittedEffect = it
            }
        }

        viewModel.onBackClicked()
        advanceUntilIdle()

        assertThat(emittedEffect).isEqualTo(SeasonDetailEffect.NavigateBack)

        job.cancel()
    }


    @Test
    fun `should map season and episodes to ui state`() = runTest {
        // Arrange
        val mockSeason = Season(1, 2, "S2", 10, 8.0f, "path.jpg", "overview", 2020L)
        val mockEpisode = Episode(1, 1, "e1.jpg", "Ep1", 45, 8.0f, 2, 1)

        coEvery { getSeriesDetailsUseCase.getSeriesSeasons(any()) } returns listOf(mockSeason)
        coEvery { getSeriesDetailsUseCase.getEpisodes(any(), any()) } returns listOf(mockEpisode)

        // Act
        viewModel = SeasonsViewModel(getSeriesDetailsUseCase, dispatcher, seriesId, seasonNumber)
        advanceUntilIdle()

        // Assert
        val state = viewModel.screenState.value
        assertThat(state.season).hasSize(1)
        assertThat(state.episodes).hasSize(1)

        val seasonUi = state.season.first()
        assertThat(seasonUi.name).isEqualTo("S2")

        val episodeUi = state.episodes.first()
        assertThat(episodeUi.episodeName).isEqualTo("Ep1")
    }

    @Test
    fun `should emit NavigateToEpisodeDetails effect manually`() = runTest {
        // Arrange
        val episodeId = 123L
        val seasonNumber = 1
        viewModel = SeasonsViewModel(getSeriesDetailsUseCase, dispatcher, seriesId, seasonNumber)
        advanceUntilIdle()

        var emittedEffect: SeasonDetailEffect? = null
        val job = launch {
            viewModel.effect.collect {
                emittedEffect = it
            }
        }

        // Act
        viewModel.onSeasonClicked(episodeId, seasonNumber)
        advanceUntilIdle()

        // Assert
        assertThat(emittedEffect).isEqualTo(SeasonDetailEffect.NavigateToEpisodesScreen(episodeId, seasonNumber))

        job.cancel()
    }

}
