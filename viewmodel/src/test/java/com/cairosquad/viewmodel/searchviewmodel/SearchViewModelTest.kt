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
    fun `should load discover movies when called`() = runBlocking {
        // Given
        val forYouList = listOf(movie1)
        val exploreMoreList = listOf(movie2)
        coEvery { getForYouUseCase.getForYouMovies() } returns forYouList
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns exploreMoreList

        // When
        viewModel.loadDiscoverMovies()
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.EXPLORE)
        assertThat(viewModel.uiState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.uiState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun `should update error message when discover movies fail`() = runBlocking {
        // Given
        coEvery { getForYouUseCase.getForYouMovies() } throws IOException()
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns emptyList()

        // When
        viewModel.loadDiscoverMovies()
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.FAILED)
        assertThat(viewModel.uiState.value.errorMessage).contains("Network error. Please check your connection")
    }

    @Test
    fun `should update query and recent search when query text changes`() = runBlocking {
        // Given
        val query = "Batman"
        val recent = listOf("Batman", "Batman Begins")
        coEvery { getRecentSearchUseCase.getByQuery(query) } returns recent

        // When
        viewModel.onQueryTextChanged(query)
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.query).isEqualTo(query)
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(recent)
    }

    @Test
    fun `should clear query and go to EXPLORE when search is cancelled`() = runBlocking {
        // When
        viewModel.onCancelSearch()

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.EXPLORE)
        assertThat(viewModel.uiState.value.query).isEmpty()
    }

    @Test
    fun `should load results when query is valid`() = runBlocking {
        // Given
        val query = "Inception"
        coEvery { searchUseCase.getMovies(query) } returns listOf(movie1)
        coEvery { searchUseCase.getSeries(query) } returns listOf(series)
        coEvery { searchUseCase.getArtists(query) } returns listOf(artist)

        // When
        viewModel.onSearch(query)
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.RESULT)
        assertThat(viewModel.uiState.value.movies).hasSize(1)
        assertThat(viewModel.uiState.value.series).hasSize(1)
        assertThat(viewModel.uiState.value.artists).hasSize(1)
    }

    @Test
    fun `should show error message when search fails`() = runBlocking {
        // Given
        coEvery { searchUseCase.getMovies(any()) } throws IOException()
        coEvery { searchUseCase.getSeries(any()) } returns emptyList()
        coEvery { searchUseCase.getArtists(any()) } returns emptyList()

        // When
        viewModel.onSearch("fail")
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.FAILED)
        assertThat(viewModel.uiState.value.errorMessage).contains("Network")
    }

    @Test
    fun `should not go to result screen when query is blank`() = runBlocking {
        // When
        viewModel.onSearch("    ")
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isNotEqualTo(SearchUiState.ScreenStatus.RESULT)
    }

    @Test
    fun `should trigger search when recent item is clicked`() = runBlocking {
        // Given
        val query = "Dark"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns emptyList()

        // When
        viewModel.onRecentSearchItemClicked(query)
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.RESULT)
    }

    @Test
    fun `should clear recent search list when clear history is triggered`() = runBlocking {
        // Given
        coEvery { clearRecentSearchUseCase.clearAll() } returns Unit

        // When
        viewModel.onClearHistory()
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.recentSearch).isEmpty()
    }

    @Test
    fun `should remove item from recent search when remove history item triggered`() = runBlocking {
        // Given
        val newList = listOf("New1", "New2")
        coEvery { clearRecentSearchUseCase.removeQuery(any()) } returns Unit
        coEvery { getRecentSearchUseCase.getAll() } returns newList

        // When
        viewModel.onRemoveHistoryItem("Old")
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(newList)
    }

    @Test
    fun `should navigate to EXPLORE when back clicked from RESULT`() = runBlocking {
        // Given
        val query = "Batman"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns emptyList()

        viewModel.onQueryTextChanged(query)
        delay(300)

        // When
        viewModel.onBackClicked()
        delay(300)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `should load recent searches when search field clicked`() = runBlocking {
        // Given
        val recent = listOf("Query1", "Query2")
        coEvery { getRecentSearchUseCase.getAll() } returns recent

        // When
        viewModel.onClickSearchTextField()
        delay(400)

        // Then
        assertThat(viewModel.uiState.value.screenStatus).isEqualTo(SearchUiState.ScreenStatus.SEARCH)
        assertThat(viewModel.uiState.value.recentSearch).isEqualTo(recent)
    }

}