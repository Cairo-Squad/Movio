package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.GetRecentSearchUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException


class SearchViewModelTest {

    private lateinit var searchUseCase: SearchUseCase
    private lateinit var getRecentSearchUseCase: GetRecentSearchUseCase
    private lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase
    private lateinit var getExploreMoreUseCase: GetExploreMoreUseCase
    private lateinit var getForYouUseCase: GetForYouUseCase
    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setUp() {
        searchUseCase = mockk()
        getRecentSearchUseCase = mockk()
        clearRecentSearchUseCase = mockk()
        getExploreMoreUseCase = mockk()
        getForYouUseCase = mockk()

        viewModel = SearchViewModel(
            searchUseCase,
            getRecentSearchUseCase,
            clearRecentSearchUseCase,
            getExploreMoreUseCase,
            getForYouUseCase
        )
    }

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

    @Test
    fun `loadDiscoverMovies loads data successfully`() = runTest {

        val forYouList = listOf(movie1).map { it.toUiState() }
        val exploreMoreList = listOf(movie2).map { it.toUiState() }

        coEvery { getForYouUseCase.getForYouMovies() } returns listOf(movie1)
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.EXPLORE)
        assertThat(viewModel.uiState.value.forYou).isEqualTo(forYouList)
        assertThat(viewModel.uiState.value.exploreMore).isEqualTo(exploreMoreList)
    }

    @Test
    fun `loadDiscoverMovies handles exception and updates errorMessage`() = runTest {
        coEvery { getForYouUseCase.getForYouMovies() } throws IOException("No Internet")
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns emptyList()

        viewModel.loadDiscoverMovies()

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.FAILED)
        assertThat(viewModel.uiState.value.errorMessage).contains("Network error")
    }

    @Test
    fun `onQueryTextChanged updates query and recentSearch`() = runTest {
        val query = "Batman"
        val recent = listOf("Batman", "Batman Begins")
        coEvery { getRecentSearchUseCase.getByQuery(query) } coAnswers { delay(300); recent }

        viewModel.onQueryTextChanged(query)

        delay(300)
        assertThat(viewModel.uiState.value.query).isEqualTo(query)
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(recent)
    }

    @Test
    fun `onCancelSearch clears query and sets to EXPLORE`() = runTest {
        viewModel.onCancelSearch()
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.EXPLORE)
        assertThat(viewModel.uiState.value.query).isEmpty()
    }

    @Test
    fun `onSearch loads results successfully`() = runTest {
        val query = "Inception"
        coEvery { searchUseCase.getMovies(query) } returns listOf(movie1)
        coEvery { searchUseCase.getSeries(query) } returns listOf(series)
        coEvery { searchUseCase.getArtists(query) } returns listOf(artist)

        viewModel.onSearch(query)

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.RESULT)
        assertThat(viewModel.uiState.value.movies).hasSize(1)
        assertThat(viewModel.uiState.value.series).hasSize(1)
        assertThat(viewModel.uiState.value.artists).hasSize(1)
    }

    @Test
    fun `onSearch shows error message when failed`() = runTest {
        coEvery { searchUseCase.getMovies(any()) } throws IOException()
        coEvery { searchUseCase.getSeries(any()) } returns emptyList()
        coEvery { searchUseCase.getArtists(any()) } returns emptyList()

        viewModel.onSearch("fail")

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.FAILED)
        assertThat(viewModel.uiState.value.errorMessage).contains("Network")
    }

    @Test
    fun `onRecentSearchItemClicked triggers search`() = runTest {
        val query = "Dark"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns emptyList()

        viewModel.onRecentSearchItemClicked(query)

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.RESULT)
    }

    @Test
    fun `onClearHistory clears all suggestions`() = runTest {
        coEvery { clearRecentSearchUseCase.clearAll() } returns Unit

        viewModel.onClearHistory()

        assertThat(viewModel.uiState.value.recentSearch).isEmpty()
    }

    @Test
    fun `onRemoveHistoryItem removes item and updates list`() = runTest {
        val newList = listOf("New1", "New2")
        coEvery { clearRecentSearchUseCase.removeQuery(any()) } returns Unit
        coEvery { getRecentSearchUseCase.getAll() } returns newList

        viewModel.onRemoveHistoryItem("Old")

        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(newList)
    }

    @Test
    fun `onBackClicked navigates correctly from RESULT to SEARCH`() = runTest {
        viewModel.updateState { it.copy(screenStatus = SearchUiState.ScreenStatus.RESULT) }

        viewModel.onBackClicked()

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.SEARCH)
    }

    @Test
    fun `onClickSearchTextField loads recent searches`() = runTest {
        val recent = listOf("Query1", "Query2")
        coEvery { getRecentSearchUseCase.getAll() } returns recent

        viewModel.onClickSearchTextField()

        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.SEARCH)
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(recent)
    }

}