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

class GetTrendingSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetTrendingSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetTrendingSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsTrendingSeriesWithCategory() = runTest {
        val page = 1
        val categoryId = "10762"

        coEvery { seriesRepository.getTrendingSeries(page, categoryId) } returns expectedWithCategory

        val result = useCase.getTrendingSeries(page, categoryId)

        assertEquals(expectedWithCategory, result)
        coVerify { seriesRepository.getTrendingSeries(page, categoryId) }
    }

    @Test
    fun testReturnsTrendingSeriesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null

        coEvery { seriesRepository.getTrendingSeries(page, categoryId) } returns expectedWithoutCategory

        val result = useCase.getTrendingSeries(page, categoryId)

        assertEquals(expectedWithoutCategory, result)
        coVerify { seriesRepository.getTrendingSeries(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "35"
        val exception = RuntimeException("Failed to fetch trending series")

        coEvery { seriesRepository.getTrendingSeries(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getTrendingSeries(page, categoryId)
        }

        assertEquals("Failed to fetch trending series", thrown.message)
        coVerify { seriesRepository.getTrendingSeries(page, categoryId) }
    }

    companion object {
        val expectedWithCategory = listOf(
            Series(
                id = 1400L,
                title = "Magic Kids Club",
                rating = 7.4f,
                posterPath = "/trending1.jpg",
                trailerPath = "https://youtube.com/magickidsclub",
                genres = listOf(Genre(10762, "Kids")),
                overview = "Whimsical adventures for children of all ages.",
                releaseDate = 1722470400000L,
                seasonsCount = 3
            )
        )

        val expectedWithoutCategory = listOf(
            Series(
                id = 1401L,
                title = "World Pulse",
                rating = 8.2f,
                posterPath = "/trending2.jpg",
                trailerPath = "https://youtube.com/worldpulse",
                genres = listOf(Genre(99, "Documentary")),
                overview = "A trending documentary following global change.",
                releaseDate = 1722556800000L,
                seasonsCount = 2
            )
        )
    }
}
