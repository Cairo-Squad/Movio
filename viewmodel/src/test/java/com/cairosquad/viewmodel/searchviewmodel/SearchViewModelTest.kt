package com.cairosquad.viewmodel.searchviewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.domain.usecase.movies.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.usecase.movies.GetSuggestedMoviesUseCase
import com.cairosquad.domain.usecase.search.ClearSearchHistoryUseCase
import com.cairosquad.domain.usecase.search.GetLocalSearchHistoryUseCase
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
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var getLocalSearchHistoryUseCase: GetLocalSearchHistoryUseCase
    private lateinit var searchPager: SearchPager
    private lateinit var getRecentSearchUseCase: GetLocalSearchHistoryUseCase
    private lateinit var clearSearchHistoryUseCase: ClearSearchHistoryUseCase
    private lateinit var getSuggestedMoviesUseCase: GetSuggestedMoviesUseCase
    private lateinit var getPersonalizedMoviesUseCase: GetPersonalizedMoviesUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchPager = mockk(relaxed = true)
        getLocalSearchHistoryUseCase = mockk(relaxed = true)
        getRecentSearchUseCase = mockk(relaxed = true)
        clearSearchHistoryUseCase = mockk(relaxed = true)
        getSuggestedMoviesUseCase = mockk(relaxed = true)
        getPersonalizedMoviesUseCase = mockk(relaxed = true)

        viewModel = SearchViewModel(
            searchPager,
            getRecentSearchUseCase,
            clearSearchHistoryUseCase,
            getSuggestedMoviesUseCase,
            getPersonalizedMoviesUseCase
        )
    }

    @Test
    fun `Should update query and trigger search when recent search item clicked`() = runTest {
        val query = "Batman"
        val moviesFlow = emptyFlow<PagingData<Movie>>()
        val seriesFlow = emptyFlow<PagingData<Series>>()
        val artistsFlow = emptyFlow<PagingData<Artist>>()

        coEvery { searchPager.series(query) } returns seriesFlow
        coEvery { searchPager.movies(query) } returns moviesFlow
        coEvery { searchPager.artists(query) } returns artistsFlow

        viewModel.onRecentSearchItemClicked(query)

        advanceUntilIdle()

        val state = viewModel.screenState.value
        assertThat(state.query).isEqualTo(query)

        assertThat(state.screenStatus).isIn(
            listOf(
                SearchScreenState.ScreenStatus.LOADING,
                SearchScreenState.ScreenStatus.SEARCH,
                SearchScreenState.ScreenStatus.RESULT,
                SearchScreenState.ScreenStatus.FAILED
            )
        )
        coVerify(exactly = 1) { searchPager.series(query) }
        coVerify(exactly = 1) { searchPager.movies(query) }
        coVerify(exactly = 1) { searchPager.artists(query) }
    }

    @Test
    fun `should remove item from recent search on successful deletion`() = runBlocking {
        val query = "test"
        val initialState = SearchScreenState(
            recentSearch = listOf("test", "other"),
            screenStatus = SearchScreenState.ScreenStatus.SEARCH
        )
        viewModel.updateState { initialState }

        coEvery { clearSearchHistoryUseCase.removeQueryFromHistory(query) } just Runs

        viewModel.onRemoveHistoryItem(query)
        delay(50)

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
        assertThat(uiState.rating).isEqualTo(movie.rating / 2)
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
    fun `should set FAILED and UNKNOWN_ERROR when deletion fails`() = runBlocking {
        val query = "test"
        viewModel.updateState {
            it.copy(
                recentSearch = listOf("test"),
                screenStatus = SearchScreenState.ScreenStatus.SEARCH
            )
        }

        coEvery { clearSearchHistoryUseCase.removeQueryFromHistory(query) } throws IOException()

        viewModel.onRemoveHistoryItem(query)
        delay(50)

        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should remove query from recentSearch when removal succeeds`() = runTest {
        val initial = listOf("Batman", "Spiderman", "Superman")
        coEvery { getLocalSearchHistoryUseCase.getAll() } returns initial
        coEvery { clearSearchHistoryUseCase.removeQueryFromHistory("Batman") } returns Unit

        viewModel.onClickSearchTextField()
        advanceUntilIdle()

        viewModel.onRemoveHistoryItem("Batman")
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.recentSearch).doesNotContain("Batman")
    }

    @Test
    fun `should not modify recentSearch if query does not exist`() = runTest {
        val query = "Ironman"
        coEvery { clearSearchHistoryUseCase.removeQueryFromHistory(query) } just Runs

        val initialSearchList = listOf("Batman", "Spiderman")
        viewModel.updateState {
            it.copy(recentSearch = initialSearchList)
        }

        viewModel.onRemoveHistoryItem(query)
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(initialSearchList)
    }

    @Test
    fun `should load discover movies when loadDiscoverMovies is called`() = runBlocking {
        val forYouList = listOf(movie1)
        val exploreMoreList = listOf(movie2)

        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(1) } returns listOf(movie1)
        coEvery { getSuggestedMoviesUseCase.getSuggestedMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.screenState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun `should set error status when discover movies loading fails`() = runBlocking {
        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(1) } throws IOException()
        coEvery { getSuggestedMoviesUseCase.getSuggestedMovies() } returns emptyList()

        viewModel.loadDiscoverMovies()

        delay(400)

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
    fun `should clear query and set EXPLORE status when search is cancelled`() = runBlocking {
        viewModel.onCancelSearch()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.query).isEmpty()
    }

    @Test
    fun `should load search results when valid query is submitted`() = runBlocking {
        val query = "Inception"

        coEvery { searchPager.movies(query) } returns flowOf(PagingData.from(listOf(movie1)))
        coEvery { searchPager.series(query) } returns flowOf(PagingData.from(listOf(series)))
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.from(listOf(artist)))

        viewModel.onQueryTextChanged(query)
        delay(350)

        viewModel.onSearch()
        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should not transition to RESULT status when query is blank`() = runBlocking {
        viewModel.onSearch()
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isNotEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should clear recent search list when clear history is triggered`() = runBlocking {
        coEvery { clearSearchHistoryUseCase.clearAllHistory() } returns Unit

        viewModel.onClearHistory()

        delay(400)

        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should transition to EXPLORE when back is clicked from RESULT`() = runBlocking {
        val query = "Batman"
        coEvery { searchPager.movies(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.series(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.empty())

        viewModel.onQueryTextChanged(query)
        delay(300)
        viewModel.onBackClicked()
        delay(300)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `should load recent searches when search field is clicked`() = runBlocking {
        val recent = listOf("Query1", "Query2")
        coEvery { getRecentSearchUseCase.getAll() } returns recent

        viewModel.onClickSearchTextField()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(recent)
    }

    @Test
    fun `should transition to SEARCH when back is clicked from RESULT`() = runBlocking {
        coEvery { searchPager.movies(any()) } returns flowOf(PagingData.empty())
        coEvery { searchPager.series(any()) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(any()) } returns flowOf(PagingData.empty())

        viewModel.onQueryTextChanged("test")
        delay(100)
        viewModel.onSearch()
        delay(300)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)

        viewModel.onBackClicked()

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `should handle error gracefully when search field is clicked`() = runBlocking {
        coEvery { getRecentSearchUseCase.getAll() } throws IOException()
        viewModel.onClickSearchTextField()
        delay(200)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should clear recent search when query text change fails`() = runBlocking {
        coEvery { getRecentSearchUseCase.getByQuery(any()) } throws IOException()
        viewModel.onQueryTextChanged("x")
        delay(350)
        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should transition to RESULT when search returns only artists`() = runBlocking {
        val query = "Painter"

        coEvery { searchPager.movies(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.series(query) } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists(query) } returns flowOf(PagingData.from(listOf(artist)))

        viewModel.onQueryTextChanged(query)

        viewModel.onSearch()

        delay(100)

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
    fun `should cancel search job and reset state when search is cancelled`() = runBlocking {
        val query = "test"
        coEvery { getRecentSearchUseCase.getByQuery(query) } returns emptyList()
        viewModel.onQueryTextChanged(query)
        delay(200)
        val jobBefore = viewModel.run {
            javaClass.getDeclaredField("searchJob").apply { isAccessible = true }.get(this) as Job?
        }
        require(jobBefore != null && jobBefore.isActive)
        viewModel.onCancelSearch()
        delay(200)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        val jobAfter = viewModel.run {
            javaClass.getDeclaredField("searchJob").apply { isAccessible = true }.get(this) as Job?
        }
        assertThat(jobAfter?.isCancelled).isTrue()
    }

    @Test
    fun `should reset state when search is cancelled with null search job`() = runBlocking {
        val jobField =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
        jobField.set(viewModel, null)
        viewModel.onCancelSearch()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `should cancel previous search job when query text changes`() = runBlocking {
        coEvery { getRecentSearchUseCase.getByQuery("a") } returns emptyList()
        viewModel

            .onQueryTextChanged("a")
        delay(200)
        val firstJob =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
                .get(viewModel) as Job
        assertThat(firstJob.isActive).isTrue()
        coEvery { getRecentSearchUseCase.getByQuery("b") } returns emptyList()
        viewModel.onQueryTextChanged("b")
        delay(200)
        val secondJob =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
                .get(viewModel) as Job
        assertThat(firstJob.isCancelled).isTrue()
        assertThat(secondJob).isNotEqualTo(firstJob)
        assertThat(secondJob.isActive).isTrue()
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
        coEvery { getRecentSearchUseCase.getByQuery(query) } throws RuntimeException()

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

        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(1) } returns listOf(movie1)
        coEvery { getSuggestedMoviesUseCase.getSuggestedMovies() } returns listOf(movie2)

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
            photoPath = "/img.jpg"
        )
    }
}