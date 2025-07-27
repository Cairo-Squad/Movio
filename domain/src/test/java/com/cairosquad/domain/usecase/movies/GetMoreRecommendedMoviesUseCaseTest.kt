package com.cairosquad.domain.usecase.movies
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetMoreRecommendedMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetMoreRecommendedMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetMoreRecommendedMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsMoviesWhenCategoryIdIsNotNull() = runTest {
        val page = 1
        val categoryId = "28"
        coEvery { moviesRepository.getMoreRecommendedMovies(page, categoryId) } returns expectedMoviesCategory28

        val result = useCase.getMoreRecommendedMovies(page, categoryId)

        assertEquals(expectedMoviesCategory28, result)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, categoryId) }
    }

    @Test
    fun testReturnsMoviesWhenCategoryIdIsNull() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getMoreRecommendedMovies(page, categoryId) } returns expectedMoviesNoGenre

        val result = useCase.getMoreRecommendedMovies(page, categoryId)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "35"
        val exception = RuntimeException("Network error")

        coEvery { moviesRepository.getMoreRecommendedMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoreRecommendedMovies(page, categoryId)
        }

        assertEquals("Network error", thrown.message)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, categoryId) }
    }

    companion object {
        val expectedMoviesCategory28 = listOf(
            Movie(
                id = 1,
                title = "Action Hero",
                rating = 7.8f,
                posterPath = "/path1.jpg",
                genres = listOf(Genre(28, "Action")),
                overview = "An action-packed adventure.",
                releaseDate = 1704067200000L,
                runtimeMinutes = 120,
                trailerPath = "https://youtube.com/trailer1"
            ),
            Movie(
                id = 2,
                title = "Thrill Ride",
                rating = 8.2f,
                posterPath = "/path2.jpg",
                genres = listOf(Genre(28, "Action"), Genre(53, "Thriller")),
                overview = "Edge of your seat thriller.",
                releaseDate = 1707523200000L,
                runtimeMinutes = 110,
                trailerPath = "https://youtube.com/trailer2"
            )
        )

        val expectedMoviesNoGenre = listOf(
            Movie(
                id = 3,
                title = "General Movie",
                rating = 6.5f,
                posterPath = "/general.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "A dramatic tale of growth.",
                releaseDate = 1700438400000L,
                runtimeMinutes = 95,
                trailerPath = "https://youtube.com/trailer3"
            )
        )
    }
}
