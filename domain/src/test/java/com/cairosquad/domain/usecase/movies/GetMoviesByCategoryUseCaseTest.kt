import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.usecase.movies.GetMoviesByCategoryUseCase
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

class GetMoviesByCategoryUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetMoviesByCategoryUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetMoviesByCategoryUseCase(moviesRepository)
    }

    @Test
    fun testReturnsMoviesForGivenCategory() = runTest {
        val page = 1
        val categoryId = "878"
        coEvery { moviesRepository.getMoviesByCategory(page, categoryId) } returns expectedMoviesSciFi

        val result = useCase.getMoviesByCategory(page, categoryId)

        assertEquals(expectedMoviesSciFi, result)
        coVerify { moviesRepository.getMoviesByCategory(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 2
        val categoryId = "10749"
        val exception = RuntimeException("Database error")

        coEvery { moviesRepository.getMoviesByCategory(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoviesByCategory(page, categoryId)
        }

        assertEquals("Database error", thrown.message)
        coVerify { moviesRepository.getMoviesByCategory(page, categoryId) }
    }

    companion object {
        val expectedMoviesSciFi = listOf(
            Movie(
                id = 30,
                title = "Galactic War",
                rating = 7.9f,
                posterPath = "/sci-fi1.jpg",
                genres = listOf(Genre(878, "Science Fiction")),
                overview = "A space epic battle.",
                releaseDate = 1709164800000L,
                runtimeMinutes = 125,
                trailerPath = "https://youtube.com/galacticwar"
            ),
            Movie(
                id = 31,
                title = "Alien Arrival",
                rating = 8.3f,
                posterPath = "/sci-fi2.jpg",
                genres = listOf(Genre(878, "Science Fiction"), Genre(53, "Thriller")),
                overview = "Mysterious signals from space.",
                releaseDate = 1711756800000L,
                runtimeMinutes = 115,
                trailerPath = "https://youtube.com/alienarrival"
            )
        )
    }
}
