package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.GetSuggestedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetPersonalizedMoviesUseCase
import com.cairosquad.domain.search.usecase.GetLocalSearchHistoryUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
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
    private lateinit var getRecentSearchUseCase: GetLocalSearchHistoryUseCase
    private lateinit var clearRecentSearchUseCase: ClearSearchHistoryUseCase
    private lateinit var getExploreMoreUseCase: GetSuggestedMoviesUseCase
    private lateinit var getForYouUseCase: GetPersonalizedMoviesUseCase
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

        coEvery { getForYouUseCase.getPersonalizedMovies() } returns listOf(movie1)
        coEvery { getExploreMoreUseCase.getSuggestedMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.screenState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun loadDiscoverMoviesHandlesExceptionAndUpdatesErrorMessage() = runBlocking {
        coEvery { getForYouUseCase.getPersonalizedMovies() } throws IOException()
        coEvery { getExploreMoreUseCase.getSuggestedMovies() } returns emptyList()

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorMessage).contains("Network error. Please check your connection")
    }

    @Test
    fun onQueryTextChangedUpdatesQueryAndRecentSearch() = runBlocking {
        val query = "Batman"
        val recent = listOf("Batman", "Batman Begins")
        coEvery { getRecentSearchUseCase.getByQuery(query) } coAnswers { recent }

        viewModel.onQueryTextChanged(query)

        delay(400)
        assertThat(viewModel.screenState.value.query).isEqualTo(query)
        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(recent)
    }

    @Test
    fun `onCancelSearch clears query and sets to EXPLORE`() = runBlocking {
        viewModel.onCancelSearch()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.query).isEmpty()
    }

    @Test
    fun `onSearch loads results successfully`() = runBlocking {
        val query = "Inception"
        coEvery { searchUseCase.getMovies(query) } returns listOf(movie1)
        coEvery { searchUseCase.getSeries(query) } returns listOf(series)
        coEvery { searchUseCase.getArtists(query) } returns listOf(artist)

        viewModel.onSearch(query)
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
        assertThat(viewModel.screenState.value.movies).hasSize(1)
        assertThat(viewModel.screenState.value.series).hasSize(1)
        assertThat(viewModel.screenState.value.artists).hasSize(1)
    }

    @Test
    fun `onSearch shows error message when failed`() = runBlocking {
        coEvery { searchUseCase.getMovies(any()) } throws IOException()
        coEvery { searchUseCase.getSeries(any()) } returns emptyList()
        coEvery { searchUseCase.getArtists(any()) } returns emptyList()

        viewModel.onSearch("fail")
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorMessage).contains("Network")
    }

    @Test
    fun `onSearch does not go to result screen when query is blank`() = runBlocking {
        viewModel.onSearch("    ")
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isNotEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `onRecentSearchItemClicked triggers search`() = runBlocking {
        val query = "Dark"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns emptyList()

        viewModel.onRecentSearchItemClicked(query)
        delay(400)
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
    }

    @Test
    fun `onClearHistory clears all suggestions`() = runBlocking {
        coEvery { clearRecentSearchUseCase.clearAllHistory() } returns Unit

        viewModel.onClearHistory()

        delay(400)

        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `onRemoveHistoryItem removes item and updates list`() = runBlocking {
        val newList = listOf("New1", "New2")
        coEvery { clearRecentSearchUseCase.removeQueryFromHistory(any()) } returns Unit
        coEvery { getRecentSearchUseCase.getAll() } returns newList

        viewModel.onRemoveHistoryItem("Old")

        delay(400)

        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(newList)
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
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `onClickSearchTextField loads recent searches`() = runBlocking {
        val recent = listOf("Query1", "Query2")
        coEvery { getRecentSearchUseCase.getAll() } returns recent

        viewModel.onClickSearchTextField()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.screenState.value.recentSearch).isEqualTo(recent)
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