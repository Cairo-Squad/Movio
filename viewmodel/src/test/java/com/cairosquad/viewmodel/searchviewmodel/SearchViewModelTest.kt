package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.domain.search.exception.InternetConnectionException
import com.cairosquad.domain.search.exception.NetworkException
import com.cairosquad.domain.search.exception.UnknownException
import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.domain.search.usecase.GetRecentSearchUseCase
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.search.SearchScreenState
import com.cairosquad.viewmodel.search.SearchViewModel
import com.cairosquad.viewmodel.search.toUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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
        id = 1, title = "The dark knight", rating = 4.0f, posterPath = "/img.jpg"
    )

    val movie2 = Movie(
        id = 2, title = "Girl", rating = 4.5f, posterPath = "/img.jpg"
    )

    val series = Series(
        id = 1, title = "Series", rating = 3.5f, posterPath = "/img.jpg"
    )

    val artist = Artist(
        id = 1, name = "Artist", photoPath = "/img.jpg"
    )

    @Test
    fun loadDiscoverMoviesLoadsDataSuccessfully() = runBlocking {

        val forYouList = listOf(movie1)
        val exploreMoreList = listOf(movie2)
        coEvery { getForYouUseCase.getForYouMovies() } returns listOf(movie1)
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns listOf(movie2)

        viewModel.loadDiscoverMovies()

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(viewModel.screenState.value.forYou).isEqualTo(forYouList.map { it.toUiState() })
        assertThat(viewModel.screenState.value.exploreMore).isEqualTo(exploreMoreList.map { it.toUiState() })
    }

    @Test
    fun loadDiscoverMoviesHandlesExceptionAndUpdatesErrorMessage() = runBlocking {
        coEvery { getForYouUseCase.getForYouMovies() } throws IOException()
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns emptyList()

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
        assertThat(viewModel.screenState.value.errorStatus).isIn(
            listOf(
                ErrorStatus.NO_INTERNET,
                ErrorStatus.NETWORK_ERROR,
                ErrorStatus.UNKNOWN_ERROR
            )
        )
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
        coEvery { clearRecentSearchUseCase.clearAll() } returns Unit

        viewModel.onClearHistory()

        delay(400)

        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `onRemoveHistoryItem removes item and updates list`() = runBlocking {
        val newList = listOf("New1", "New2")
        coEvery { clearRecentSearchUseCase.removeQuery(any()) } returns Unit
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


    @Test
    fun `onBackClicked from SEARCH returns to EXPLORE`() = runBlocking {
        //Given && When
        viewModel.onClickSearchTextField()
        delay(50)
        viewModel.onBackClicked()
        //Then
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `onBackClicked from RESULT returns to SEARCH`() = runBlocking {
        //Given
        coEvery { searchUseCase.getMovies("") } returns emptyList()
        coEvery { searchUseCase.getSeries("") } returns emptyList()
        coEvery { searchUseCase.getArtists("") } returns emptyList()
        //When
        viewModel.onSearch("test")
        delay(50)
        viewModel.onBackClicked()
        //Then
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `onClickSearchTextField handles error gracefully`() = runBlocking {
        //Given
        coEvery { getRecentSearchUseCase.getAll() } throws IOException()
        //When
        viewModel.onClickSearchTextField()
        delay(50)
        //Then
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `onQueryTextChanged handles suggestion error`() = runBlocking {
        //Given
        coEvery { getRecentSearchUseCase.getByQuery(any()) } throws IOException()
        //When
        viewModel.onQueryTextChanged("x")
        delay(350)
        //Then
        assertThat(viewModel.screenState.value.recentSearch).isEmpty()
    }

    @Test
    fun `onSearch with only artists still returns RESULT`() = runBlocking {
        //Given
        val query = "Painter"
        coEvery { searchUseCase.getMovies(query) } returns emptyList()
        coEvery { searchUseCase.getSeries(query) } returns emptyList()
        coEvery { searchUseCase.getArtists(query) } returns listOf(artist)
        //When
        viewModel.onSearch(query)
        delay(50)
        //Then
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.RESULT)
        assertThat(viewModel.screenState.value.movies).isEmpty()
        assertThat(viewModel.screenState.value.series).isEmpty()
        assertThat(viewModel.screenState.value.artists).hasSize(1)
    }

    @Test
    fun `onClearHistory sets UNKNOWN_ERROR on failure`() = runBlocking {
        //Given
        coEvery { clearRecentSearchUseCase.clearAll() } throws IOException()
        //When
        viewModel.onClearHistory()
        delay(50)
        //Then
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)   // غيّر القيمة
    }

    @Test
    fun `onRemoveHistoryItem sets UNKNOWN_ERROR when dao fails`() = runBlocking {
        //Given
        coEvery { clearRecentSearchUseCase.removeQuery(any()) } throws IllegalStateException()
        //When
        viewModel.onRemoveHistoryItem("dummy")
        delay(50)
        //Then
        assertThat(viewModel.screenState.value.errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `onBackClicked leaves state unchanged when status is EXPLORE`() = runTest {
        //Given
        viewModel.updateState {
            it.copy(
                screenStatus = SearchScreenState.ScreenStatus.EXPLORE,
                query = "whatever",
                recentSearch = listOf("a", "b")
            )
        }
        //When
        viewModel.onBackClicked()
        //Then
        val state = viewModel.screenState.value
        assertThat(state.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        assertThat(state.query).isEqualTo("whatever")
        assertThat(state.recentSearch).containsExactly("a", "b")
    }

    @Test
    fun `onCancelSearch cancels running searchJob and resets state`() = runBlocking {
        //Given
        val query = "test"
        coEvery { getRecentSearchUseCase.getByQuery(query) } returns emptyList()
        viewModel.onQueryTextChanged(query)
        delay(50)
        val jobBefore = viewModel.run {
            javaClass.getDeclaredField("searchJob").apply { isAccessible = true }.get(this) as Job?
        }
        require(jobBefore != null && jobBefore.isActive)
        //When
        viewModel.onCancelSearch()
        delay(50)
        //Then
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
        val jobAfter = viewModel.run {
            javaClass.getDeclaredField("searchJob").apply { isAccessible = true }.get(this) as Job?
        }
        assertThat(jobAfter?.isCancelled).isTrue()
    }

    @Test
    fun `onCancelSearch works when searchJob is already null`() = runBlocking {
        //Given
        val jobField =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
        jobField.set(viewModel, null)

        //When
        viewModel.onCancelSearch()

        //Then
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.EXPLORE)
    }

    @Test
    fun `onSearch cancels or completes previous searchJob before launching new one`() =
        runBlocking {
            //Given
            coEvery { searchUseCase.getMovies(any()) } returns emptyList()
            coEvery { searchUseCase.getSeries(any()) } returns emptyList()
            coEvery { searchUseCase.getArtists(any()) } returns emptyList()
            //When
            viewModel.onSearch("query1")
            delay(20)
            val firstJob =
                viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
                    .get(viewModel) as Job
            assertThat(firstJob).isNotNull()
            viewModel.onSearch("query2")
            delay(20)
            val secondJob =
                viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
                    .get(viewModel) as Job
            //Then
            assertThat(firstJob.isCancelled || firstJob.isCompleted).isTrue()
            assertThat(secondJob).isNotEqualTo(firstJob)
        }

    @Test
    fun `onQueryTextChanged cancels previous searchJob before starting new one`() = runBlocking {
        //Given
        coEvery { getRecentSearchUseCase.getByQuery("a") } returns emptyList()
        viewModel.onQueryTextChanged("a")
        delay(50)
        //When
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
        //Then
        assertThat(firstJob.isCancelled).isTrue()
        assertThat(secondJob).isNotEqualTo(firstJob)
        assertThat(secondJob.isActive).isTrue()
    }

    @Test
    fun `onClickSearchTextField cancels running searchJob and sets SEARCH status`() = runBlocking {
        coEvery { getRecentSearchUseCase.getByQuery("seed") } returns emptyList()
        viewModel.onQueryTextChanged("seed")
        delay(50)
        val jobField =
            viewModel.javaClass.getDeclaredField("searchJob").apply { isAccessible = true }
        val jobBefore = jobField.get(viewModel) as Job
        assertThat(jobBefore).isNotNull()
        coEvery { getRecentSearchUseCase.getAll() } returns emptyList()
        viewModel.onClickSearchTextField()
        delay(50)
        assertThat(jobBefore.isCancelled || jobBefore.isCompleted).isTrue()
        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(SearchScreenState.ScreenStatus.SEARCH)
    }

    @Test
    fun `handleSearchException maps MovioException to enum value`() {
        //Given
        val movioEx = NetworkException()
        // When
        val status = viewModel.run {
            javaClass.getDeclaredMethod("handleSearchException", Throwable::class.java)
                .apply { isAccessible = true }.invoke(this, movioEx)
        } as ErrorStatus
        // Then
        assertThat(status).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `onSearch failure triggers onError and sets NETWORK_ERROR`() = runBlocking {
        //Given
        coEvery { searchUseCase.getMovies("boom") } throws NetworkException()
        coEvery { searchUseCase.getSeries("boom") } returns emptyList()
        coEvery { searchUseCase.getArtists("boom") } returns emptyList()
        //When
        viewModel.onSearch("boom")
        delay(50)
        //Then
        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        }
    }

    @Test
    fun `loadDiscoverMovies failure sets FAILED and NETWORK_ERROR`() = runBlocking {
        // Given
        coEvery { getForYouUseCase.getForYouMovies() } throws NetworkException()
        coEvery { getExploreMoreUseCase.getExploreMoreMovies() } returns emptyList()
        // When
        viewModel.loadDiscoverMovies()
        delay(50)
        // Then
        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(SearchScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        }
    }

    @Test
    fun `handleSearchException returns UNKNOWN_ERROR for generic exception`() {
        //Given
        val genericEx = IllegalStateException("boom")
        //When
        val status = viewModel.run {
            javaClass.getDeclaredMethod("handleSearchException", Throwable::class.java)
                .apply { isAccessible = true }.invoke(this, genericEx)
        } as ErrorStatus
        //Then
        assertThat(status).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `NetworkException maps to NETWORK_ERROR`() {
        //Given
        val status = exceptionToErrorStatus(NetworkException())
        //When && Then
        assertThat(status).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `UnknownException maps to UNKNOWN_ERROR`() {
        //Given
        val status = exceptionToErrorStatus(UnknownException())
        //When && Then
        assertThat(status).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `InternetConnectionException maps to NO_INTERNET`() {
        //Given
        val status = exceptionToErrorStatus(InternetConnectionException())
        //When && Then
        assertThat(status).isEqualTo(ErrorStatus.NO_INTERNET)
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