package com.cairosquad.viewmodel.library

import app.cash.turbine.test
import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModelTest.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LibraryViewModel
    private val accountUseCase: AccountUseCase = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        viewModel = LibraryViewModel(accountUseCase, loginUseCase)
    }

    @Test
    fun `WHEN onListsViewAllClick SHOULD emit NavigateToLists effect`() = runTest {
        viewModel.effect.test {
            viewModel.onListsViewAllClick()
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToLists)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onFavoritesViewAllClick SHOULD emit NavigateToFavorites effect`() = runTest {
        viewModel.effect.test {
            viewModel.onFavoritesViewAllClick()
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToFavorites)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onHistoryViewAllClick SHOULD emit NavigateToHistory effect`() = runTest {
        viewModel.effect.test {
            viewModel.onHistoryViewAllClick()
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToHistory)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onLoginClicked SHOULD emit NavigateToLogin effect`() = runTest {
        viewModel.effect.test {
            viewModel.onLoginClicked()
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onListClicked SHOULD emit NavigateToListDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onListClicked(1, "name")
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToListDetails(1, "name"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onMovieClicked SHOULD emit NavigateToMovieDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onMovieClicked(1)
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToMovieDetails(1))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN onSeriesClicked SHOULD emit NavigateToSeriesDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClicked(1)
            assertThat(awaitItem()).isEqualTo(LibraryEffect.NavigateToSeriesDetails(1))
            cancelAndIgnoreRemainingEvents()
        }
    }
}