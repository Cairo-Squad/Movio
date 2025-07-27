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

class GetFreeToWatchMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetFreeToWatchMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetFreeToWatchMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsMoviesWhenCategoryIdIsNotNull() = runTest {
        val page = 1
        val categoryId = "35"
        coEvery { moviesRepository.getFreeToWatchMovies(page, categoryId) } returns expectedMoviesCategory35

        val result = useCase.getFreeToWatchMovies(page, categoryId)

        assertEquals(expectedMoviesCategory35, result)
        coVerify { moviesRepository.getFreeToWatchMovies(page, categoryId) }
    }

    @Test
    fun testReturnsMoviesWhenCategoryIdIsNull() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getFreeToWatchMovies(page, categoryId) } returns expectedMoviesNoCategory

        val result = useCase.getFreeToWatchMovies(page, categoryId)

        assertEquals(expectedMoviesNoCategory, result)
        coVerify { moviesRepository.getFreeToWatchMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "99"
        val exception = RuntimeException("Service unavailable")

        coEvery { moviesRepository.getFreeToWatchMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getFreeToWatchMovies(page, categoryId)
        }

        assertEquals("Service unavailable", thrown.message)
        coVerify { moviesRepository.getFreeToWatchMovies(page, categoryId) }
    }

    companion object {
        val expectedMoviesCategory35 = listOf(
            Movie(
                id = 10,
                title = "Laugh Out Loud",
                rating = 7.1f,
                posterPath = "/comedy1.jpg",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A hilarious comedy for everyone.",
                releaseDate = 1701475200000L,
                runtimeMinutes = 90,
                trailerPath = "https://youtube.com/loltrailer"
            ),
            Movie(
                id = 11,
                title = "Funny Bones",
                rating = 6.8f,
                posterPath = "/comedy2.jpg",
                genres = listOf(Genre(35, "Comedy"), Genre(18, "Drama")),
                overview = "A heartfelt and funny journey.",
                releaseDate = 1704067200000L,
                runtimeMinutes = 100,
                trailerPath = "https://youtube.com/funnybones"
            )
        )

        val expectedMoviesNoCategory = listOf(
            Movie(
                id = 12,
                title = "Public Access",
                rating = 5.9f,
                posterPath = "/free.jpg",
                genres = listOf(Genre(99, "Documentary")),
                overview = "Free to watch documentary on open networks.",
                releaseDate = 1698883200000L,
                runtimeMinutes = 85,
                trailerPath = "https://youtube.com/publicaccess"
            )
        )
    }
}
