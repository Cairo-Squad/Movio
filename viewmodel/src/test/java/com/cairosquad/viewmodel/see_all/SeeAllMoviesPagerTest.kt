package com.cairosquad.viewmodel.see_all

import androidx.paging.testing.asSnapshot
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SeeAllMoviesPagerTest {

    private lateinit var manageMoviesUseCase: ManageMoviesUseCase
    private lateinit var seeAllMoviesPager: SeeAllMoviesPager

    @Before
    fun setUp() {
        manageMoviesUseCase = mockk(relaxUnitFun = true)
        seeAllMoviesPager = SeeAllMoviesPager(manageMoviesUseCase)
    }

    private fun createTestMovie(id: Long, title: String) = Movie(
        id = id,
        title = title,
        rating = 0f,
        posterPath = "",
        genres = emptyList(),
        overview = "",
        releaseDate = 0L,
        runtimeMinutes = 0,
        trailerPath = ""
    )

//    @Test
//    fun `getTopRatingMovies should emit correct movies`() = runTest {
//        // Given
//        val testMovies = listOf(
//            createTestMovie(1, "Movie 1"),
//            createTestMovie(2, "Movie 2")
//        )
//
//        coEvery { manageMoviesUseCase.getTopRatingMovies(1, null) } returns testMovies
//        coEvery { manageMoviesUseCase.getTopRatingMovies(any(), any()) } returns emptyList()
//
//        // When
//        val flow = seeAllMoviesPager.getTopRatingMovies(null)
//        val result = flow.asSnapshot {
//            advanceUntilIdle() // Important for paging to load all data
//        }
//
//        // Then
//        assertEquals(testMovies, result)
//    }
//
//    @Test
//    fun `getTopRatingMovies with genre should emit filtered movies`() = runTest {
//        // Given
//        val genreId = 28L
//        val testMovies = listOf(
//            createTestMovie(1, "Action Movie 1"),
//            createTestMovie(2, "Action Movie 2")
//        )
//
//        coEvery { manageMoviesUseCase.getTopRatingMovies(1, genreId) } returns testMovies
//        coEvery { manageMoviesUseCase.getTopRatingMovies(any(), any()) } returns emptyList()
//
//        // When
//        val flow = seeAllMoviesPager.getTopRatingMovies(genreId)
//        val result = flow.asSnapshot {
//            advanceUntilIdle()
//        }
//
//        // Then
//        assertEquals(testMovies, result)
//    }

    @Test
    fun `should handle empty results`() = runTest {
        // Given
        coEvery { manageMoviesUseCase.getTopRatingMovies(any(), any()) } returns emptyList()

        // When
        val flow = seeAllMoviesPager.getTopRatingMovies(null)
        val result = flow.asSnapshot {
            advanceUntilIdle()
        }

        // Then
        assertTrue(result.isEmpty())
    }
}