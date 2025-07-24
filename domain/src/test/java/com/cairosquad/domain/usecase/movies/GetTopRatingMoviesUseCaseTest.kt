import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.usecase.movies.GetTopRatingMoviesUseCase
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

class GetTopRatingMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetTopRatingMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetTopRatingMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsTopRatedMoviesWithCategory() = runTest {
        val page = 1
        val categoryId = "80"
        coEvery { moviesRepository.getTopRatingMovies(page, categoryId) } returns expectedTopRatedWithCategory

        val result = useCase.getTopRatingMovies(page, categoryId)

        assertEquals(expectedTopRatedWithCategory, result)
        coVerify { moviesRepository.getTopRatingMovies(page, categoryId) }
    }

    @Test
    fun testReturnsTopRatedMoviesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getTopRatingMovies(page, categoryId) } returns expectedTopRatedWithoutCategory

        val result = useCase.getTopRatingMovies(page, categoryId)

        assertEquals(expectedTopRatedWithoutCategory, result)
        coVerify { moviesRepository.getTopRatingMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "18"
        val exception = RuntimeException("Failed to fetch top-rated movies")

        coEvery { moviesRepository.getTopRatingMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getTopRatingMovies(page, categoryId)
        }

        assertEquals("Failed to fetch top-rated movies", thrown.message)
        coVerify { moviesRepository.getTopRatingMovies(page, categoryId) }
    }

    companion object {
        val expectedTopRatedWithCategory = listOf(
            Movie(
                id = 300,
                title = "Crime Master",
                rating = 8.7f,
                posterPath = "/toprated1.jpg",
                genres = listOf(Genre(80, "Crime")),
                overview = "A mind-bending crime thriller.",
                releaseDate = 1720656000000L,
                runtimeMinutes = 122,
                trailerPath = "https://youtube.com/crimemaster"
            )
        )

        val expectedTopRatedWithoutCategory = listOf(
            Movie(
                id = 301,
                title = "Cinematic Legend",
                rating = 9.1f,
                posterPath = "/toprated2.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "One of the greatest films ever made.",
                releaseDate = 1720742400000L,
                runtimeMinutes = 130,
                trailerPath = "https://youtube.com/cinematiclegend"
            )
        )
    }
}
