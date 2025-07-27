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

class GetUpcomingMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetUpcomingMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetUpcomingMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsUpcomingMoviesWithCategory() = runTest {
        val page = 1
        val categoryId = "14"
        coEvery { moviesRepository.getUpcomingMovies(page, categoryId) } returns expectedUpcomingWithCategory

        val result = useCase.getUpcomingMovies(page, categoryId)

        assertEquals(expectedUpcomingWithCategory, result)
        coVerify { moviesRepository.getUpcomingMovies(page, categoryId) }
    }

    @Test
    fun testReturnsUpcomingMoviesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getUpcomingMovies(page, categoryId) } returns expectedUpcomingWithoutCategory

        val result = useCase.getUpcomingMovies(page, categoryId)

        assertEquals(expectedUpcomingWithoutCategory, result)
        coVerify { moviesRepository.getUpcomingMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "18"
        val exception = RuntimeException("Upcoming movies fetch failed")

        coEvery { moviesRepository.getUpcomingMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getUpcomingMovies(page, categoryId)
        }

        assertEquals("Upcoming movies fetch failed", thrown.message)
        coVerify { moviesRepository.getUpcomingMovies(page, categoryId) }
    }

    companion object {
        val expectedUpcomingWithCategory = listOf(
            Movie(
                id = 500,
                title = "Fantasy Reborn",
                rating = 7.5f,
                posterPath = "/upcoming1.jpg",
                genres = listOf(Genre(14, "Fantasy")),
                overview = "A magical journey into a mystical world.",
                releaseDate = 1721001600000L,
                runtimeMinutes = 119,
                trailerPath = "https://youtube.com/fantasyreborn"
            )
        )

        val expectedUpcomingWithoutCategory = listOf(
            Movie(
                id = 501,
                title = "New Horizons",
                rating = 8.2f,
                posterPath = "/upcoming2.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "An inspiring story about new beginnings.",
                releaseDate = 1721088000000L,
                runtimeMinutes = 104,
                trailerPath = "https://youtube.com/newhorizons"
            )
        )
    }
}
