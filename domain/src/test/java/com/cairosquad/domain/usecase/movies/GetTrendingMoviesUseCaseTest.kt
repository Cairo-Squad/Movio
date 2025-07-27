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

class GetTrendingMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetTrendingMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetTrendingMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsTrendingMoviesWithCategory() = runTest {
        val page = 1
        val categoryId = "53"
        coEvery { moviesRepository.getTrendingMovies(page, categoryId) } returns expectedTrendingWithCategory

        val result = useCase.getTrendingMovies(page, categoryId)

        assertEquals(expectedTrendingWithCategory, result)
        coVerify { moviesRepository.getTrendingMovies(page, categoryId) }
    }

    @Test
    fun testReturnsTrendingMoviesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getTrendingMovies(page, categoryId) } returns expectedTrendingWithoutCategory

        val result = useCase.getTrendingMovies(page, categoryId)

        assertEquals(expectedTrendingWithoutCategory, result)
        coVerify { moviesRepository.getTrendingMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "27"
        val exception = RuntimeException("Failed to load trending movies")

        coEvery { moviesRepository.getTrendingMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getTrendingMovies(page, categoryId)
        }

        assertEquals("Failed to load trending movies", thrown.message)
        coVerify { moviesRepository.getTrendingMovies(page, categoryId) }
    }

    companion object {
        val expectedTrendingWithCategory = listOf(
            Movie(
                id = 400,
                title = "Thrill Zone",
                rating = 7.6f,
                posterPath = "/trending1.jpg",
                genres = listOf(Genre(53, "Thriller")),
                overview = "Non-stop tension and suspense.",
                releaseDate = 1720828800000L,
                runtimeMinutes = 101,
                trailerPath = "https://youtube.com/thrillzone"
            )
        )

        val expectedTrendingWithoutCategory = listOf(
            Movie(
                id = 401,
                title = "Buzz Hit",
                rating = 8.4f,
                posterPath = "/trending2.jpg",
                genres = listOf(Genre(35, "Comedy")),
                overview = "Everyone's talking about it.",
                releaseDate = 1720915200000L,
                runtimeMinutes = 97,
                trailerPath = "https://youtube.com/buzzhit"
            )
        )
    }
}
