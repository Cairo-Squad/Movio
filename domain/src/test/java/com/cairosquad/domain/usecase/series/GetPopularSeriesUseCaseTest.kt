import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Series
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetPopularSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetPopularSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetPopularSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsPopularSeriesWithCategory() = runTest {
        val page = 1
        val categoryId = "80"

        coEvery { seriesRepository.getPopularSeries(page, categoryId) } returns expectedWithCategory

        val result = useCase.getPopularSeries(page, categoryId)

        assertEquals(expectedWithCategory, result)
        coVerify { seriesRepository.getPopularSeries(page, categoryId) }
    }

    @Test
    fun testReturnsPopularSeriesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null

        coEvery { seriesRepository.getPopularSeries(page, categoryId) } returns expectedWithoutCategory

        val result = useCase.getPopularSeries(page, categoryId)

        assertEquals(expectedWithoutCategory, result)
        coVerify { seriesRepository.getPopularSeries(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "10767"
        val exception = RuntimeException("Failed to fetch popular series")

        coEvery { seriesRepository.getPopularSeries(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getPopularSeries(page, categoryId)
        }

        assertEquals("Failed to fetch popular series", thrown.message)
        coVerify { seriesRepository.getPopularSeries(page, categoryId) }
    }

    companion object {
        val expectedWithCategory = listOf(
            Series(
                id = 1100L,
                title = "City Crime Files",
                rating = 8.7f,
                posterPath = "/popular1.jpg",
                trailerPath = "https://youtube.com/citycrimefiles",
                genres = listOf(Genre(80, "Crime")),
                overview = "Crime never sleeps in the big city.",
                releaseDate = 1722038400000L,
                seasonsCount = 5
            )
        )

        val expectedWithoutCategory = listOf(
            Series(
                id = 1101L,
                title = "The People’s Favorite",
                rating = 8.0f,
                posterPath = "/popular2.jpg",
                trailerPath = "https://youtube.com/thepeoplesfavorite",
                genres = listOf(Genre(35, "Comedy")),
                overview = "The show that everyone is talking about.",
                releaseDate = 1722124800000L,
                seasonsCount = 4
            )
        )
    }
}
