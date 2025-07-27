import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Genre
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetMoviesGenresUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetMoviesGenresUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetMoviesGenresUseCase(moviesRepository)
    }

    @Test
    fun testReturnsGenresSuccessfully() = runTest {
        coEvery { moviesRepository.getMoviesGenres() } returns expectedGenres

        val result = useCase.getMoviesGenres()

        assertEquals(expectedGenres, result)
        coVerify { moviesRepository.getMoviesGenres() }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val exception = RuntimeException("Genres fetch failed")

        coEvery { moviesRepository.getMoviesGenres() } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoviesGenres()
        }

        assertEquals("Genres fetch failed", thrown.message)
        coVerify { moviesRepository.getMoviesGenres() }
    }

    companion object {
        val expectedGenres = listOf(
            Genre(28, "Action"),
            Genre(35, "Comedy"),
            Genre(18, "Drama"),
            Genre(878, "Science Fiction"),
            Genre(10749, "Romance")
        )
    }
}
