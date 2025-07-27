import com.cairosquad.domain.model.SortType
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

class GetAllSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetAllSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetAllSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsAllSeriesWithCategoryAndSort() = runTest {
        val page = 1
        val categoryId = "10765"
        val sortType = SortType.LATEST

        coEvery { seriesRepository.getAllSeries(page, categoryId, sortType) } returns expectedSeriesSorted

        val result = useCase.getAllSeries(page, categoryId, sortType)

        assertEquals(expectedSeriesSorted, result)
        coVerify { seriesRepository.getAllSeries(page, categoryId, sortType) }
    }

    @Test
    fun testReturnsAllSeriesWithoutCategoryAndSort() = runTest {
        val page = 2
        val categoryId: String? = null
        val sortType: SortType? = null

        coEvery { seriesRepository.getAllSeries(page, categoryId, sortType) } returns expectedSeriesUnfiltered

        val result = useCase.getAllSeries(page, categoryId, sortType)

        assertEquals(expectedSeriesUnfiltered, result)
        coVerify { seriesRepository.getAllSeries(page, categoryId, sortType) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "18"
        val sortType = SortType.POPULAR
        val exception = RuntimeException("Failed to fetch series")

        coEvery { seriesRepository.getAllSeries(page, categoryId, sortType) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getAllSeries(page, categoryId, sortType)
        }

        assertEquals("Failed to fetch series", thrown.message)
        coVerify { seriesRepository.getAllSeries(page, categoryId, sortType) }
    }

    companion object {
        val expectedSeriesSorted = listOf(
            Series(
                id = 700L,
                title = "Future Chronicles",
                rating = 9.0f,
                posterPath = "/sorted1.jpg",
                trailerPath = "https://youtube.com/futurechronicles",
                genres = listOf(Genre(10765, "Sci-Fi & Fantasy")),
                overview = "Top-rated science fiction series.",
                releaseDate = 1721347200000L,
                seasonsCount = 4
            )
        )

        val expectedSeriesUnfiltered = listOf(
            Series(
                id = 701L,
                title = "General Series",
                rating = 7.2f,
                posterPath = "/unfiltered1.jpg",
                trailerPath = "https://youtube.com/generalseries",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A light-hearted show for everyone.",
                releaseDate = 1721433600000L,
                seasonsCount = 2
            )
        )
    }
}
