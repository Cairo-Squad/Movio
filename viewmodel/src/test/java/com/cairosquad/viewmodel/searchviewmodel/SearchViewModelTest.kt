package com.cairosquad.viewmodel.searchviewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSearchHistoryUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.search.SearchEffect
import com.cairosquad.viewmodel.search.SearchScreenState
import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.search.paging.SearchPager
import com.cairosquad.viewmodel.search.toUiState
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var manageSearchHistoryUseCase: ManageSearchHistoryUseCase
    private lateinit var searchPager: SearchPager
    private lateinit var manageMoviesUseCase: ManageMoviesUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        searchPager = mockk(relaxed = true)
        manageSearchHistoryUseCase = mockk(relaxed = true)
        manageSearchHistoryUseCase = mockk(relaxed = true)
        manageSearchHistoryUseCase = mockk(relaxed = true)
        manageMoviesUseCase = mockk(relaxed = true)

        viewModel = SearchViewModel(
            searchPager,
            manageSearchHistoryUseCase,
            manageMoviesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should remove item from recent search on successful deletion`() = runTest {
        val query = "test"
        val initialState = SearchScreenState(
            recentSearch = listOf("test", "other"),
            screenStatus = SearchScreenState.ScreenStatus.SEARCH
        )
        viewModel.updateState { initialState }

        coEvery { manageSearchHistoryUseCase.removeQueryFromHistory(query) } just Runs

        viewModel.onRemoveHistoryItem(query)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.recentSearch).doesNotContain(query)
        assertThat(state.recentSearch).containsExactly("other")
        assertThat(state.errorStatus).isNull()
    }

    @Test
    fun `should map Movie to MovieUiState correctly`() {
        val movie = movie1
        val uiState = movie.toUiState()
        assertThat(uiState.id).isEqualTo(movie.id)
        assertThat(uiState.title).isEqualTo(movie.title)
        assertThat(uiState.rating).isEqualTo(movie.rating.roundToFirstDecimalPlace())
        assertThat(uiState.posterPath).isEqualTo(movie.posterPath)
    }

    @Test
    fun `should not call searchPager when query is blank`() = runTest {
        viewModel.updateState { it.copy(query = "") }

        viewModel.onSearch()
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isNotEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should set FAILED and UNKNOWN_ERROR when deletion fails`() = runTest {
        val query = "test"
        viewModel.updateState {
            it.copy(
                recentSearch = listOf("test"),
                screenStatus = SearchScreenState.ScreenStatus.SEARCH
            )
        }

        coEvery { manageSearchHistoryUseCase.removeQueryFromHistory(query) } throws IOException()

        viewModel.onRemoveHistoryItem(query)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should remove query from recentSearch when removal succeeds`() = runTest {
        val initial = listOf("Batman", "Spiderman", "Superman")
        coEvery { manageSearchHistoryUseCase.getAll() } returns initial
        coEvery { manageSearchHistoryUseCase.removeQueryFromHistory("Batman") } returns Unit

        viewModel.onClickSearchTextField()
        advanceUntilIdle()

        viewModel.onRemoveHistoryItem("Batman")
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.recentSearch).doesNotContain("Batman")
    }

    @Test
    fun `should not modify recentSearch if query does not exist`() = runTest {
        val query = "Ironman"
        coEvery { manageSearchHistoryUseCase.removeQueryFromHistory(query) } just Runs

        val initialSearchList = listOf("Batman", "Spiderman")
        viewModel.updateState {
            it.copy(recentSearch = initialSearchList)
        }

        viewModel.onRemoveHistoryItem(query)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(initialSearchList)
    }

    @Test
    fun `should load discover movies when loadDiscoverMovies is called`() = runTest {
        val forYouList = listOf(movie1)
        val exploreMoreList = listOf(movie2)

        coEvery { manageMoviesUseCase.getPersonalizedMovies(1) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getSuggestedMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.screenState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun `should set error status when personalized movies loading fails`() = runTest {
        coEvery { manageMoviesUseCase.getPersonalizedMovies(1) } throws IOException()

        viewModel.getPersonalizedMovies()

        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isIn(
            listOf(
                ErrorStatus.NO_INTERNET,
                ErrorStatus.NETWORK_ERROR,
                ErrorStatus.UNKNOWN_ERROR
            )
        )
    }

    @Test
    fun `should set error status when discover movies loading fails`() = runTest {
        coEvery { manageMoviesUseCase.getPersonalizedMovies(1) } throws IOException()
        coEvery { manageMoviesUseCase.getSuggestedMovies() } throws IOException() // Make both fail

        viewModel.loadDiscoverMovies()

        // Wait for both operations to complete
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should clear query and set EXPLORE status when search is cancelled`() = runTest {
        viewModel.onCancelSearch()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.query).isEmpty()
    }

    @Test
    fun `should load search results when valid query is submitted`() = runTest {
        val query = "Inception"

        coEvery { searchPager.movies(query) } returns flowOf(PagingData.from(listOf(movie1)))
        coEvery { searchPager.series(query) } returns flowOf(PagingData.from(listOf(series)))
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.from(listOf(artist)))

        viewModel.onQueryTextChanged(query)

        viewModel.onSearch()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should not transition to RESULT status when query is blank`() = runTest {
        viewModel.onSearch()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.screenStatus).isNotEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should clear recent search list when clear history is triggered`() = runTest {
        coEvery { manageSearchHistoryUseCase.clearAllHistory() } returns Unit

        viewModel.onClearHistory()

        advanceUntilIdle()

        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should transition to EXPLORE when back is clicked from RESULT`() = runTest {
        val query = "Batman"
        coEvery { searchPager.movies(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.series(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.empty())

        viewModel.onQueryTextChanged(query)
        advanceUntilIdle()
        viewModel.onBackClicked()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `should load recent searches when search field is clicked`() = runTest {
        val recent = listOf("Query1", "Query2")
        coEvery { manageSearchHistoryUseCase.getAll() } returns recent

        viewModel.onClickSearchTextField()

        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(recent)
    }

    @Test
    fun `should transition to SEARCH when back is clicked from RESULT`() = runTest {
        coEvery { searchPager.movies(any()) } returns flowOf(PagingData.empty())
        coEvery { searchPager.series(any()) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(any()) } returns flowOf(PagingData.empty())

        viewModel.onQueryTextChanged("test")
        advanceUntilIdle()
        viewModel.onSearch()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)

        viewModel.onBackClicked()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `should handle error gracefully when search field is clicked`() = runTest {
        coEvery { manageSearchHistoryUseCase.getAll() } throws IOException()
        viewModel.onClickSearchTextField()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should clear recent search when query text change fails`() = runTest {
        coEvery { manageSearchHistoryUseCase.getByQuery(any()) } throws IOException()
        viewModel.onQueryTextChanged("x")
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should transition to RESULT when search returns only artists`() = runTest {
        val query = "Painter"

        coEvery { searchPager.movies(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.series(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.from(listOf(artist)))

        viewModel.onQueryTextChanged(query)

        viewModel.onSearch()

        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should maintain EXPLORE state when back is clicked from EXPLORE`() = runTest {
        viewModel.updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.EXPLORE,
                query = "whatever",
                recentSearch = listOf("a", "b")
            )
        }
        viewModel.onBackClicked()
        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(state.query).isEqualTo("whatever")
        assertThat(state.recentSearch).containsExactly("a", "b")
    }

    @Test
    fun `should reset state when search is cancelled with null search job`() = runTest {
        val jobField =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
        jobField.set(viewModel, null)
        viewModel.onCancelSearch()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `should map MovioException to correct error status when handling search exception`() {
        val movioEx = NetworkException()
        val status = viewModel.run {
            javaClass.getDeclaredMethod("handleSearchException", Throwable::class.java)
                .apply { isAccessible = true }.invoke(this, movioEx)
        } as ErrorStatus
        assertThat(status).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `should map generic exception to UNKNOWN_ERROR when handling search exception`() {
        val genericEx = IllegalStateException("boom")
        val status = viewModel.run {
            javaClass.getDeclaredMethod("handleSearchException", Throwable::class.java)
                .apply { isAccessible = true }.invoke(this, genericEx)
        } as ErrorStatus
        assertThat(status).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should map NetworkException to NETWORK_ERROR when converting exception`() {
        val status = exceptionToErrorStatus(NetworkException())
        assertThat(status).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `should map UnknownException to UNKNOWN_ERROR when converting exception`() {
        val status = exceptionToErrorStatus(UnknownException())
        assertThat(status).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should map InternetConnectionException to NO_INTERNET when converting exception`() {
        val status = exceptionToErrorStatus(InternetConnectionException())
        assertThat(status).isEqualTo(ErrorStatus.NO_INTERNET)
    }

    @Test
    fun `should show loading then result when performing search`() = runTest {
        val query = "Inception"
        coEvery { searchPager.movies(query) } returns flowOf(PagingData.from(listOf(movie1)))
        coEvery { searchPager.series(query) } returns flowOf(PagingData.from(listOf(series)))
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.from(listOf(artist)))

        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel.onQueryTextChanged(query)
        advanceUntilIdle()

        viewModel.onSearch()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus)
            .isEqualTo(SearchScreenState.ScreenStatus.RESULT)

        unmockkStatic(Dispatchers::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `should not crash if suggestions use case throws inside debounce`() = runTest {
        val query = "Error"
        coEvery { manageSearchHistoryUseCase.getByQuery(query) } throws RuntimeException()

        viewModel.onQueryTextChanged(query)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `should refresh and call loadDiscoverMovies when query is blank`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        coEvery { manageMoviesUseCase.getPersonalizedMovies(1) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getSuggestedMovies() } returns listOf(movie2)

        viewModel.updateState { it.copy(query = "") }

        viewModel.onRefresh()

        advanceTimeBy(500)
        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.isRefreshing).isFalse()
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(state.forYou).isNotEmpty()
        assertThat(state.exploreMore).isNotEmpty()

        unmockkStatic(Dispatchers::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `should navigate to movie details when movie is clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onMovieClicked(123)
            assertThat(awaitItem()).isEqualTo(SearchEffect.NavigateToMovieDetails(123))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to series details when series is clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeriesClicked(123)
            assertThat(awaitItem()).isEqualTo(SearchEffect.NavigateToSeriesDetails(123))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to artist details when artist is clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onArtistClicked(123)
            assertThat(awaitItem()).isEqualTo(SearchEffect.NavigateToArtistDetails(123))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to see all for you movies when see all is clicked`() = runTest {
        viewModel.effect.test {
            viewModel.onSeeAllForYouClicked()
            assertThat(awaitItem()).isEqualTo(SearchEffect.NavigateToSeeAllForYouScreen)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should update selected tab index when tab is selected`() = runTest {
        viewModel.onTabSelected(2)
        assertThat(viewModel.screenState.value.selectedTabIndex).isEqualTo(2)
    }

    @Test
    fun `should set FAILED and UNKNOWN_ERROR when search fails with exception`() = runTest {
        val query = "test query"

        coEvery { searchPager.movies(query) } throws IOException()
        coEvery { searchPager.series(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.empty())

        viewModel.onQueryTextChanged(query)
        advanceUntilIdle()

        viewModel.onSearch()
        advanceUntilIdle()

        waitUntil {
            viewModel.screenState.value.screenStatus == SearchScreenState.ScreenStatus.FAILED
        }

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `onRecentSearchItemClicked should update query and trigger search`() = runTest {

        val testQuery = "batman"

        every { searchPager.movies(any()) } returns flowOf(PagingData.empty())
        every { searchPager.series(any()) } returns flowOf(PagingData.empty())
        every { searchPager.artists(any()) } returns flowOf(PagingData.empty())

        viewModel.onRecentSearchItemClicked(testQuery)
        advanceUntilIdle()
        waitUntil {
            viewModel.screenState.value.screenStatus == SearchScreenState.ScreenStatus.RESULT
        }

        val state = viewModel.screenState.value
        assertThat(state.query).isEqualTo(testQuery)
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)

        assertThat(state.errorStatus).isNull()
    }

    @Test
    fun `onClearHistory should set FAILED and UNKNOWN_ERROR when exception occurs`() = runTest {

        coEvery { manageSearchHistoryUseCase.clearAllHistory() }  throws IOException()

        viewModel.onClearHistory()
        advanceUntilIdle()
        waitUntil {
            viewModel.screenState.value.screenStatus == SearchScreenState.ScreenStatus.FAILED
        }

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should show failed status when both personalized and suggested fail`() = runTest {
        coEvery { manageMoviesUseCase.getPersonalizedMovies(1) } throws IOException()
        coEvery { manageMoviesUseCase.getSuggestedMovies() } throws IOException()

        viewModel.loadDiscoverMovies()

        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
    }

    @Test
    fun `should show explore screen when both personalized and suggested succeed`() = runTest {
        coEvery { manageMoviesUseCase.getPersonalizedMovies(1) } returns listOf(movie1)
        coEvery { manageMoviesUseCase.getSuggestedMovies() } returns listOf(movie1)

        viewModel.loadDiscoverMovies()

        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(state.forYou).isNotEmpty()
        assertThat(state.exploreMore).isNotEmpty()
    }

    suspend fun waitUntil(
        timeoutMillis: Long = 3000,
        intervalMillis: Long = 50,
        condition: () -> Boolean
    ) {
        val start = System.currentTimeMillis()
        while (!condition()) {
            if (System.currentTimeMillis() - start > timeoutMillis) {
                throw AssertionError("Condition wasn't met within timeout")
            }
            delay(intervalMillis)
        }
    }

    private companion object {
        val movie1 = Movie(
            id = 1,
            title = "The dark knight",
            rating = 4.0f,
            posterPath = "/img.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )

        val movie2 = Movie(
            id = 2,
            title = "Girl",
            rating = 4.5f,
            posterPath = "/img.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )

        val series = Series(
            id = 1,
            title = "Series",
            rating = 3.5f,
            posterPath = "/img.jpg",
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 1
        )

        val artist = Artist(
            id = 1,
            name = "Artist",
            photoPath = "/img.jpg",
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
    }
}