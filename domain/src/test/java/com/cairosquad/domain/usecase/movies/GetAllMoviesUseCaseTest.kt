import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.usecase.movies.GetAllMoviesUseCase
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

class GetAllMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var useCase: GetAllMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        useCase = GetAllMoviesUseCase(moviesRepository)
    }

    @Test
    fun testReturnsMoviesWithCategoryAndSortType() = runTest {
        val page = 1
        val categoryId = "12"
        val sortType = SortType.POPULAR
        coEvery { moviesRepository.getAllMovies(page, categoryId, sortType) } returns expectedMoviesAdventurePopular

        val result = useCase.getAllMovies(page, categoryId, sortType)

        assertEquals(expectedMoviesAdventurePopular, result)
        coVerify { moviesRepository.getAllMovies(page, categoryId, sortType) }
    }

    @Test
    fun testReturnsMoviesWithNullCategoryAndSort() = runTest {
        val page = 2
        val categoryId: String? = null
        val sortType: SortType? = null
        coEvery { moviesRepository.getAllMovies(page, categoryId, sortType) } returns expectedMoviesNoFilter

        val result = useCase.getAllMovies(page, categoryId, sortType)

        assertEquals(expectedMoviesNoFilter, result)
        coVerify { moviesRepository.getAllMovies(page, categoryId, sortType) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "16"
        val sortType = SortType.LATEST
        val exception = RuntimeException("API failure")

        coEvery { moviesRepository.getAllMovies(page, categoryId, sortType) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getAllMovies(page, categoryId, sortType)
        }

        assertEquals("API failure", thrown.message)
        coVerify { moviesRepository.getAllMovies(page, categoryId, sortType) }
    }

    companion object {
        val expectedMoviesAdventurePopular = listOf(
            Movie(
                id = 20,
                title = "Jungle Quest",
                rating = 8.0f,
                posterPath = "/adventure1.jpg",
                genres = listOf(Genre(12, "Adventure")),
                overview = "An epic jungle journey.",
                releaseDate = 1706745600000L,
                runtimeMinutes = 130,
                trailerPath = "https://youtube.com/junglequest"
            )
        )

        val expectedMoviesNoFilter = listOf(
            Movie(
                id = 21,
                title = "Open Film",
                rating = 6.0f,
                posterPath = "/nofilter.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "A movie without restrictions.",
                releaseDate = 1700438400000L,
                runtimeMinutes = 100,
                trailerPath = "https://youtube.com/openfilm"
            )
        )
    }
}
