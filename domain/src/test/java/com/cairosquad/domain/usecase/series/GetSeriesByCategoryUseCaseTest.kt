import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.domain.usecase.series.GetSeriesByCategoryUseCase
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

class GetSeriesByCategoryUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetSeriesByCategoryUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetSeriesByCategoryUseCase(seriesRepository)
    }

    @Test
    fun testReturnsSeriesByCategorySuccessfully() = runTest {
        val categoryId = "10768"
        val page = 1

        coEvery { seriesRepository.getSeriesByCategory(categoryId, page) } returns expectedSeries

        val result = useCase.getSeriesByCategory(categoryId, page)

        assertEquals(expectedSeries, result)
        coVerify { seriesRepository.getSeriesByCategory(categoryId, page) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val categoryId = "99"
        val page = 2
        val exception = RuntimeException("Failed to load series by category")

        coEvery { seriesRepository.getSeriesByCategory(categoryId, page) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getSeriesByCategory(categoryId, page)
        }

        assertEquals("Failed to load series by category", thrown.message)
        coVerify { seriesRepository.getSeriesByCategory(categoryId, page) }
    }

    companion object {
        val expectedSeries = listOf(
            Series(
                id = 1200L,
                title = "Global Conflicts",
                rating = 8.6f,
                posterPath = "/seriesbycat1.jpg",
                trailerPath = "https://youtube.com/globalconflicts",
                genres = listOf(Genre(10768, "War & Politics")),
                overview = "A deep dive into international tensions and politics.",
                releaseDate = 1722211200000L,
                seasonsCount = 2
            )
        )
    }
}
