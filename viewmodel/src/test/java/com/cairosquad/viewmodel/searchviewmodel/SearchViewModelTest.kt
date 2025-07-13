package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.GetRecentSearchUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var searchUseCase: SearchUseCase
    private lateinit var getRecentSearchUseCase: GetRecentSearchUseCase
    private lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase
    private lateinit var getExploreMoreUseCase: GetExploreMoreUseCase
    private lateinit var getForYouUseCase: GetForYouUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        searchUseCase = mockk(relaxed = true)
        getRecentSearchUseCase = mockk(relaxed = true)
        clearRecentSearchUseCase = mockk(relaxed = true)
        getExploreMoreUseCase = mockk(relaxed = true)
        getForYouUseCase = mockk(relaxed = true)

        viewModel = SearchViewModel(
            searchUseCase,
            getRecentSearchUseCase,
            clearRecentSearchUseCase,
            getExploreMoreUseCase,
            getForYouUseCase
        )
    }

    @Test
    fun loadDiscoverMoviesLoadsDataSuccessfully() = runBlocking {

        val forYouList = listOf(movie1)
        val exploreMoreList = listOf(movie2)

        coEvery { getForYouUseCase.getForYouMovies() } returns listOf(movie1)
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.uiState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.uiState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun loadDiscoverMoviesHandlesExceptionAndUpdatesErrorMessage() = runBlocking {
        coEvery { getForYouUseCase.getForYouMovies() } throws IOException()
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns emptyList()

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.uiState.value.errorMessage).contains("Network error. Please check your connection")
    }

    @Test
    fun onQueryTextChangedUpdatesQueryAndRecentSearch() = runBlocking {
        val query = "Batman"
        val recent = listOf("Batman", "Batman Begins")
        coEvery { getRecentSearchUseCase.getByQuery(query) } coAnswers { recent }

        viewModel.onQueryTextChanged(query)

        delay(400)
        assertThat(viewModel.uiState.value.query).isEqualTo(query)
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(recent)
    }

    @Test
    fun `onCancelSearch clears query and sets to EXPLORE`() = runBlocking {
        viewModel.onCancelSearch()
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.uiState.value.query).isEmpty()
    }

    @Test
    fun `onSearch loads results successfully`() = runBlocking {
        val query = "Inception"
        coEvery { searchUseCase.getMovies(query) } returns listOf(movie1)
        coEvery { searchUseCase.getSeries(query) } returns listOf(series)
        coEvery { searchUseCase.getArtists(query) } returns listOf(artist)

        viewModel.onSearch(query)
        delay(400)
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
        assertThat(viewModel.uiState.value.movies).hasSize(1)
        assertThat(viewModel.uiState.value.series).hasSize(1)
        assertThat(viewModel.uiState.value.artists).hasSize(1)
    }

    @Test
    fun `onSearch shows error message when failed`() = runBlocking {
        coEvery { searchUseCase.getMovies(any()) } throws IOException()
        coEvery { searchUseCase.getSeries(any()) } returns emptyList()
        coEvery { searchUseCase.getArtists(any()) } returns emptyList()

        viewModel.onSearch("fail")
        delay(400)
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.uiState.value.errorMessage).contains("Network")
    }

    @Test
    fun `onSearch does not go to result screen when query is blank`() = runBlocking {
        viewModel.onSearch("    ")
        delay(400)
        assertThat(viewModel.uiState.value.screenStatus).isNotEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `onRecentSearchItemClicked triggers search`() = runBlocking {
        val query = "Dark"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns emptyList()

        viewModel.onRecentSearchItemClicked(query)
        delay(400)
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `onClearHistory clears all suggestions`() = runBlocking {
        coEvery { clearRecentSearchUseCase.clearAll() } returns Unit

        viewModel.onClearHistory()

        delay(400)

        assertThat(viewModel.uiState.value.recentSearch).isEmpty()
    }

    @Test
    fun `onRemoveHistoryItem removes item and updates list`() = runBlocking {
        val newList = listOf("New1", "New2")
        coEvery { clearRecentSearchUseCase.removeQuery(any()) } returns Unit
        coEvery { getRecentSearchUseCase.getAll() } returns newList

        viewModel.onRemoveHistoryItem("Old")

        delay(400)

        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(newList)
    }

    @Test
    fun `onBackClicked navigates correctly from RESULT to SEARCH`() = runBlocking {
        val query = "Batman"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns emptyList()

        viewModel.onQueryTextChanged(query)
        delay(300)
        viewModel.onBackClicked()
        delay(300)
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `onClickSearchTextField loads recent searches`() = runBlocking {
        val recent = listOf("Query1", "Query2")
        coEvery { getRecentSearchUseCase.getAll() } returns recent

        viewModel.onClickSearchTextField()

        delay(400)

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(recent)
    }
}