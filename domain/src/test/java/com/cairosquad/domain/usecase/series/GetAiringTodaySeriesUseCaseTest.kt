import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.domain.usecase.series.GetAiringTodaySeriesUseCase
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

class GetAiringTodaySeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetAiringTodaySeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetAiringTodaySeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsAiringTodaySeriesWithCategory() = runTest {
        val page = 1
        val categoryId = "10759"
        coEvery { seriesRepository.getAiringTodaySeries(page, categoryId) } returns expectedSeriesWithCategory

        val result = useCase.getAiringTodaySeries(page, categoryId)

        assertEquals(expectedSeriesWithCategory, result)
        coVerify { seriesRepository.getAiringTodaySeries(page, categoryId) }
    }

    @Test
    fun testReturnsAiringTodaySeriesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null
        coEvery { seriesRepository.getAiringTodaySeries(page, categoryId) } returns expectedSeriesWithoutCategory

        val result = useCase.getAiringTodaySeries(page, categoryId)

        assertEquals(expectedSeriesWithoutCategory, result)
        coVerify { seriesRepository.getAiringTodaySeries(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "18"
        val exception = RuntimeException("Failed to fetch airing today series")

        coEvery { seriesRepository.getAiringTodaySeries(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getAiringTodaySeries(page, categoryId)
        }

        assertEquals("Failed to fetch airing today series", thrown.message)
        coVerify { seriesRepository.getAiringTodaySeries(page, categoryId) }
    }

    companion object {
        val expectedSeriesWithCategory = listOf(
            Series(
                id = 600L,
                title = "Adventures Unfold",
                rating = 8.1f,
                posterPath = "/series1.jpg",
                trailerPath = "https://youtube.com/adventuresunfold",
                genres = listOf(Genre(10759, "Action & Adventure")),
                overview = "Today's explosive episode reveals everything.",
                releaseDate = 1721174400000L,
                seasonsCount = 3
            )
        )

        val expectedSeriesWithoutCategory = listOf(
            Series(
                id = 601L,
                title = "Slice of Life",
                rating = 7.4f,
                posterPath = "/series2.jpg",
                trailerPath = "https://youtube.com/sliceoflife",
                genres = listOf(Genre(18, "Drama")),
                overview = "Ordinary stories, extraordinary feelings.",
                releaseDate = 1721260800000L,
                seasonsCount = 2
            )
        )
    }
}
