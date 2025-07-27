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

class GetNowPlayingMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetNowPlayingMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetNowPlayingMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsNowPlayingWithCategory() = runTest {
        val page = 1
        val categoryId = "28"
        coEvery { moviesRepository.getNowPlayingMovies(page, categoryId) } returns expectedNowPlayingWithCategory

        val result = useCase.getNowPlayingMovies(page, categoryId)

        assertEquals(expectedNowPlayingWithCategory, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, categoryId) }
    }

    @Test
    fun testReturnsNowPlayingWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { moviesRepository.getNowPlayingMovies(page, categoryId) } returns expectedNowPlayingWithoutCategory

        val result = useCase.getNowPlayingMovies(page, categoryId)

        assertEquals(expectedNowPlayingWithoutCategory, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "16"
        val exception = RuntimeException("Now playing fetch failed")

        coEvery { moviesRepository.getNowPlayingMovies(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getNowPlayingMovies(page, categoryId)
        }

        assertEquals("Now playing fetch failed", thrown.message)
        coVerify { moviesRepository.getNowPlayingMovies(page, categoryId) }
    }

    companion object {
        val expectedNowPlayingWithCategory = listOf(
            Movie(
                id = 100,
                title = "Action Reloaded",
                rating = 7.5f,
                posterPath = "/now1.jpg",
                genres = listOf(Genre(28, "Action")),
                overview = "Explosive action-packed movie currently in theaters.",
                releaseDate = 1720310400000L,
                runtimeMinutes = 115,
                trailerPath = "https://youtube.com/actionreloaded"
            )
        )

        val expectedNowPlayingWithoutCategory = listOf(
            Movie(
                id = 101,
                title = "Cinema Vibes",
                rating = 6.9f,
                posterPath = "/now2.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "Emotional drama now in theaters.",
                releaseDate = 1720396800000L,
                runtimeMinutes = 102,
                trailerPath = "https://youtube.com/cinemavibes"
            )
        )
    }
}
