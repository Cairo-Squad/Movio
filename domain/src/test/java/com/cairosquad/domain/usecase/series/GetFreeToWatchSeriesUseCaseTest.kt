import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.domain.usecase.series.GetFreeToWatchSeriesUseCase
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

class GetFreeToWatchSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetFreeToWatchSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetFreeToWatchSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsFreeToWatchSeriesWithCategory() = runTest {
        val page = 1
        val categoryId = "16" // Animation

        coEvery { seriesRepository.getFreeToWatchSeries(page, categoryId) } returns expectedWithCategory

        val result = useCase.getFreeToWatchSeries(page, categoryId)

        assertEquals(expectedWithCategory, result)
        coVerify { seriesRepository.getFreeToWatchSeries(page, categoryId) }
    }

    @Test
    fun testReturnsFreeToWatchSeriesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null

        coEvery { seriesRepository.getFreeToWatchSeries(page, categoryId) } returns expectedWithoutCategory

        val result = useCase.getFreeToWatchSeries(page, categoryId)

        assertEquals(expectedWithoutCategory, result)
        coVerify { seriesRepository.getFreeToWatchSeries(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "35"
        val exception = RuntimeException("Failed to fetch free-to-watch series")

        coEvery { seriesRepository.getFreeToWatchSeries(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getFreeToWatchSeries(page, categoryId)
        }

        assertEquals("Failed to fetch free-to-watch series", thrown.message)
        coVerify { seriesRepository.getFreeToWatchSeries(page, categoryId) }
    }

    companion object {
        val expectedWithCategory = listOf(
            Series(
                id = 800L,
                title = "Animated Dreams",
                rating = 7.8f,
                posterPath = "/free1.jpg",
                trailerPath = "https://youtube.com/animateddreams",
                genres = listOf(Genre(16, "Animation")),
                overview = "A fun animated series for all ages.",
                releaseDate = 1721520000000L,
                seasonsCount = 5
            )
        )

        val expectedWithoutCategory = listOf(
            Series(
                id = 801L,
                title = "Unscripted Journey",
                rating = 8.0f,
                posterPath = "/free2.jpg",
                trailerPath = "https://youtube.com/unscriptedjourney",
                genres = listOf(Genre(99, "Documentary")),
                overview = "A powerful documentary series available freely.",
                releaseDate = 1721606400000L,
                seasonsCount = 1
            )
        )
    }
}
