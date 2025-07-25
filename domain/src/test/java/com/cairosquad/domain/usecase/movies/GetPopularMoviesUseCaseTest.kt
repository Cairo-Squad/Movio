import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.usecase.movies.GetPopularMoviesUseCase
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

class GetPopularMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetPopularMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetPopularMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsPopularMoviesWithCategory() = runTest {
        val page = 1
        val categoryId = "35"
        coEvery { moviesRepository.getPopularMovies(page, categoryId) } returns expectedPopularWithCategory

        val result = useCase.getPopularMovies(page, categoryId)

        assertEquals(expectedPopularWithCategory, result)
        coVerify { moviesRepository.getPopularMovies(page, categoryId) }
    }

    @Test
    fun testReturnsPopularMoviesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getPopularMovies(page, categoryId) } returns expectedPopularWithoutCategory

        val result = useCase.getPopularMovies(page, categoryId)

        assertEquals(expectedPopularWithoutCategory, result)
        coVerify { moviesRepository.getPopularMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "18"
        val exception = RuntimeException("Failed to fetch popular movies")

        coEvery { moviesRepository.getPopularMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getPopularMovies(page, categoryId)
        }

        assertEquals("Failed to fetch popular movies", thrown.message)
        coVerify { moviesRepository.getPopularMovies(page, categoryId) }
    }

    companion object {
        val expectedPopularWithCategory = listOf(
            Movie(
                id = 200,
                title = "Comedy King",
                rating = 7.3f,
                posterPath = "/popular1.jpg",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A laugh-out-loud comedy hit.",
                releaseDate = 1720483200000L,
                runtimeMinutes = 98,
                trailerPath = "https://youtube.com/comedyking"
            )
        )

        val expectedPopularWithoutCategory = listOf(
            Movie(
                id = 201,
                title = "Everyone's Favorite",
                rating = 8.1f,
                posterPath = "/popular2.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "The most talked-about film of the year.",
                releaseDate = 1720569600000L,
                runtimeMinutes = 108,
                trailerPath = "https://youtube.com/everyonesfavorite"
            )
        )
    }
}
