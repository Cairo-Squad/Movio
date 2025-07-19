package com.cairosquad.viewmodel.details.similar_movies

import com.cairosquad.domain.usecase.movies.GetMovieDetailsUseCase
import com.cairosquad.entity.Movie
import com.cairosquad.viewmodel.details.similar_movies.SimilarMoviesScreenState.ScreenStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SimilarMoviesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockGetMovieDetailsUseCase = mockk<GetMovieDetailsUseCase>()
    private lateinit var viewModel: SimilarMoviesViewModel

    private val mockMovies = listOf(
        Movie(
            id = 1,
            title = "Similar Movie 1",
            rating = 8.5f,
            posterPath = "poster1.jpg",
            genres = emptyList(),
            overview = "Overview 1",
            releaseDate = 1672531200000L,
            runtimeMinutes = 120,
            trailerPath = "trailer1.mp4"
        ),
        Movie(
            id = 2,
            title = "Similar Movie 2",
            rating = 7.8f,
            posterPath = "poster2.jpg",
            genres = emptyList(),
            overview = "Overview 2",
            releaseDate = 1675209600000L,
            runtimeMinutes = 105,
            trailerPath = "trailer2.mp4"
        ),
        Movie(
            id = 3,
            title = "Similar Movie 3",
            rating = 9.1f,
            posterPath = "poster3.jpg",
            genres = emptyList(),
            overview = "Overview 3",
            releaseDate = 1677628800000L,
            runtimeMinutes = 135,
            trailerPath = "trailer3.mp4"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SimilarMoviesViewModel(mockGetMovieDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have correct default values`() {
        // Given & When
        val initialState = viewModel.screenState.value

        // Then
        assertThat(initialState.movies).isEmpty()
        assertThat(initialState.screenStatus).isEqualTo(ScreenStatus.INITIAL)
        assertThat(initialState.errorStatus).isNull()
    }
}
