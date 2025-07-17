package com.cairosquad.viewmodel.searchviewmodel

import androidx.paging.PagingData
import com.cairosquad.domain.search.exception.InternetConnectionException
import com.cairosquad.domain.search.exception.NetworkException
import com.cairosquad.domain.search.exception.UnknownException
import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetSuggestedMoviesUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState
import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.search.paging.SearchPager
import com.cairosquad.viewmodel.search.toUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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

    private lateinit var searchPager: SearchPager
    private lateinit var getRecentSearchUseCase: GetLocalSearchHistoryUseCase
    private lateinit var clearRecentSearchUseCase: ClearSearchHistoryUseCase
    private lateinit var getSuggestedMoviesUseCase: GetSuggestedMoviesUseCase
    private lateinit var getPersonalizedMoviesUseCase: GetPersonalizedMoviesUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        searchPager = mockk(relaxed = true)
        getRecentSearchUseCase = mockk(relaxed = true)
        clearRecentSearchUseCase = mockk(relaxed = true)
        getSuggestedMoviesUseCase = mockk(relaxed = true)
        getPersonalizedMoviesUseCase = mockk(relaxed = true)

        viewModel = SearchViewModel(
            searchPager,
            getRecentSearchUseCase,
            clearRecentSearchUseCase,
            getSuggestedMoviesUseCase,
            getPersonalizedMoviesUseCase
        )
    }

    @Test
    fun `should load discover movies when loadDiscoverMovies is called`() = runBlocking {
        val forYouList = listOf(movie1)
        val exploreMoreList = listOf(movie2)

        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies() } returns listOf(movie1)
        coEvery { getSuggestedMoviesUseCase.getSuggestedMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.screenState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun `should set error status when discover movies loading fails`() = runBlocking {
        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies() } throws IOException()
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

        viewModel.onSearch(query)
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should not transition to RESULT status when query is blank`() = runBlocking {
        viewModel.onSearch("    ")
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isNotEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should clear recent search list when clear history is triggered`() = runBlocking {
        coEvery { clearRecentSearchUseCase.clearAllHistory() } returns Unit

        viewModel.onClearHistory()

        delay(400)

        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should remove item from recent search when history item is removed`() = runBlocking {
        val newList = listOf("New1", "New2")
        coEvery { clearRecentSearchUseCase.removeQueryFromHistory(any()) } returns Unit
        coEvery { getRecentSearchUseCase.getAll() } returns newList

        viewModel.onRemoveHistoryItem("Old")

        delay(400)

        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(newList)
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
    fun `should transition to EXPLORE when back is clicked from SEARCH`() = runBlocking {
        viewModel.onClickSearchTextField()
        delay(50)
        viewModel.onBackClicked()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `should transition to SEARCH when back is clicked from RESULT`() = runBlocking {
        coEvery { searchPager.movies("") } returns flowOf(PagingData.empty())
        coEvery { searchPager.series("") } returns flowOf(PagingData.empty())
        coEvery { searchPager.artists("") } returns flowOf(PagingData.empty())
        viewModel.onSearch("test")
        delay(200)
        viewModel.onBackClicked()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `should handle error gracefully when search field is clicked`() = runBlocking {
        coEvery { getRecentSearchUseCase.getAll() } throws IOException()
        viewModel.onClickSearchTextField()
        delay(50)
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
        viewModel.onSearch(query)
        delay(50)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `should set UNKNOWN_ERROR when clear history fails`() = runBlocking {
        coEvery { clearRecentSearchUseCase.clearAllHistory() } throws IOException()
        viewModel.onClearHistory()
        delay(50)
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
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
        delay(50)
        val jobBefore = viewModel.run {
            javaClass.getDeclaredField("searchJob").apply { isAccessible = true }.get(this) as Job?
        }
        require(jobBefore != null && jobBefore.isActive)
        viewModel.onCancelSearch()
        delay(50)
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
        delay(50)
        val firstJob =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
                .get(viewModel) as Job
        assertThat(firstJob.isActive).isTrue()
        coEvery { getRecentSearchUseCase.getByQuery("b") } returns emptyList()
        viewModel.onQueryTextChanged("b")
        delay(50)
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

        viewModel.onSearch(query)

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
    fun `should not clear recentSearch when onClickSearchTextField fails`() = runTest {
        val previous = listOf("a", "b")
        viewModel.updateState { it.copy(recentSearch = previous) }

        coEvery { getRecentSearchUseCase.getAll() } throws IOException()

        viewModel.onClickSearchTextField()
        advanceUntilIdle()

        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(previous)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `should refresh and call loadDiscoverMovies when query is blank`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies() } returns listOf(movie1)
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

    private companion object {
        val movie1 = Movie(
            id = 1,
            title = "The dark knight",
            rating = 4.0f,
            posterPath = "/img.jpg"
        )

        val movie2 = Movie(
            id = 2,
            title = "Girl",
            rating = 4.5f,
            posterPath = "/img.jpg"
        )

        val series = Series(
            id = 1,
            title = "Series",
            rating = 3.5f,
            posterPath = "/img.jpg"
        )

        val artist = Artist(
            id = 1,
            name = "Artist",
            photoPath = "/img.jpg"
        )
    }
}