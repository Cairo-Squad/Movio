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
    fun testReturnsNowPlayingWithGenre() = runTest {
        val page = 1
        val genreId = "28"
        coEvery { moviesRepository.getNowPlayingMovies(page, genreId) } returns expectedNowPlayingWithGenre

        val result = useCase.getNowPlayingMovies(page, genreId)

        assertEquals(expectedNowPlayingWithGenre, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, genreId) }
    }

    @Test
    fun testReturnsNowPlayingWithoutGenre() = runTest {
        val page = 2
        val genreId: String? = null
        coEvery { moviesRepository.getNowPlayingMovies(page, genreId) } returns expectedNowPlayingWithoutGenre

        val result = useCase.getNowPlayingMovies(page, genreId)

        assertEquals(expectedNowPlayingWithoutGenre, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, genreId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val genreId = "16"
        val exception = RuntimeException("Now playing fetch failed")

        coEvery { moviesRepository.getNowPlayingMovies(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getNowPlayingMovies(page, genreId)
        }

        assertEquals("Now playing fetch failed", thrown.message)
        coVerify { moviesRepository.getNowPlayingMovies(page, genreId) }
    }

    companion object {
        val expectedNowPlayingWithGenre = listOf(
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

        val expectedNowPlayingWithoutGenre = listOf(
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
