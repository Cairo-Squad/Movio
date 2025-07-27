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

class GetMoreRecommendedSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetMoreRecommendedSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetMoreRecommendedSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsMoreRecommendedSeriesWithCategory() = runTest {
        val page = 1
        val categoryId = "9648"

        coEvery { seriesRepository.getMoreRecommendedSeries(page, categoryId) } returns expectedRecommendedWithCategory

        val result = useCase.getMoreRecommendedSeries(page, categoryId)

        assertEquals(expectedRecommendedWithCategory, result)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, categoryId) }
    }

    @Test
    fun testReturnsMoreRecommendedSeriesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null

        coEvery { seriesRepository.getMoreRecommendedSeries(page, categoryId) } returns expectedRecommendedWithoutCategory

        val result = useCase.getMoreRecommendedSeries(page, categoryId)

        assertEquals(expectedRecommendedWithoutCategory, result)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "10751"
        val exception = RuntimeException("Failed to fetch recommended series")

        coEvery { seriesRepository.getMoreRecommendedSeries(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoreRecommendedSeries(page, categoryId)
        }

        assertEquals("Failed to fetch recommended series", thrown.message)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, categoryId) }
    }

    companion object {
        val expectedRecommendedWithCategory = listOf(
            Series(
                id = 900L,
                title = "Mystery Echoes",
                rating = 8.5f,
                posterPath = "/recommended1.jpg",
                trailerPath = "https://youtube.com/mysteryechoes",
                genres = listOf(Genre(9648, "Mystery")),
                overview = "Unravel the secrets one clue at a time.",
                releaseDate = 1721692800000L,
                seasonsCount = 3
            )
        )

        val expectedRecommendedWithoutCategory = listOf(
            Series(
                id = 901L,
                title = "Global Picks",
                rating = 7.9f,
                posterPath = "/recommended2.jpg",
                trailerPath = "https://youtube.com/globalpicks",
                genres = listOf(Genre(10751, "Family")),
                overview = "Loved by millions across the world.",
                releaseDate = 1721779200000L,
                seasonsCount = 4
            )
        )
    }
}
