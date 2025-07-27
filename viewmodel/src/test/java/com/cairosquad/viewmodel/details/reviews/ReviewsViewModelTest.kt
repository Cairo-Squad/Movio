package com.cairosquad.viewmodel.details.reviews

import com.cairosquad.entity.Review
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var getSeriesDetailsUseCase: GetSeriesDetailsUseCase
    private lateinit var viewModel: ReviewsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
        getMovieDetailsUseCase = mockk(relaxed = true)
        getSeriesDetailsUseCase = mockk(relaxed = true)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getReviews should return reviews state when the movie use case return reviews`() =
        testScope.runTest {
            // Given
            coEvery { getMovieDetailsUseCase.getMovieReviews(MOVIE_ID) } returns testReviews
            viewModel = ReviewsViewModel(
                mediaId = MOVIE_ID,
                isMovie = true,
                manageMoviesUseCase = getMovieDetailsUseCase,
                manageSeriesUseCase = getSeriesDetailsUseCase,
                dispatcher = testDispatcher
            )

            // When
            viewModel.getReviews()
            advanceUntilIdle()

            // Then
            val state = viewModel.screenState.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.reviews).hasSize(1)
            assertThat(state.reviews.first().reviewerName).isEqualTo(AUTHOR_NAME)
            assertThat(state.reviews.first().reviewText).isEqualTo(DESCRIPTION)
            assertThat(state.error).isNull()
        }

    @Test
    fun `getReviews should return reviews state when the series use case return reviews`() =
        testScope.runTest {
            // Given
            coEvery { getSeriesDetailsUseCase.getSeriesReviews(SERIES_ID, 1) } returns testReviews
            viewModel = ReviewsViewModel(
                mediaId = SERIES_ID,
                isMovie = false,
                manageMoviesUseCase = getMovieDetailsUseCase,
                manageSeriesUseCase = getSeriesDetailsUseCase,
                dispatcher = testDispatcher
            )
            // When
            viewModel.getReviews()
            advanceUntilIdle()

            // Then
            val state = viewModel.screenState.value
            assertThat(state.reviews).hasSize(1)
            assertThat(state.reviews.first().reviewerName).isEqualTo(AUTHOR_NAME)
            assertThat(state.reviews.first().reviewText).isEqualTo(DESCRIPTION)
            assertThat(state.error).isNull()
        }

    @Test
    fun `getReviews should return error state when the movie use case throws an exception`() =
        testScope.runTest {
            // Given
            coEvery { getMovieDetailsUseCase.getMovieReviews(MOVIE_ID) } throws Exception(
                ERROR_MESSAGE
            )
            viewModel = ReviewsViewModel(
                mediaId = MOVIE_ID,
                isMovie = true,
                manageMoviesUseCase = getMovieDetailsUseCase,
                manageSeriesUseCase = getSeriesDetailsUseCase,
                dispatcher = testDispatcher
            )

            // When
            viewModel.getReviews()
            advanceUntilIdle()

            // Then
            val state = viewModel.screenState.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.reviews).isEmpty()
            assertThat(state.error).isEqualTo(ERROR_MESSAGE)
        }

    companion object {
        private const val MOVIE_ID = 101L
        private const val SERIES_ID = 202L
        private const val AUTHOR_NAME = "lmao7"
        private const val AUTHOR_PHOTO_PATH = "/ekmYOUU4tfx9zGGadjRdE7UPce.jpg"
        private const val RATING = 9.0
        private const val DATE = 1487569648872L
        private const val DESCRIPTION = "good"
        private const val ERROR_MESSAGE = "Network error"

        private val testReviews = listOf(
            Review(
                id = "",
                author = AUTHOR_NAME,
                authorPhotoPath = AUTHOR_PHOTO_PATH,
                rating = RATING,
                date = DATE,
                description = DESCRIPTION
            )
        )
    }
}