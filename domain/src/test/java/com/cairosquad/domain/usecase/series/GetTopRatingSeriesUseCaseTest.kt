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

class GetTopRatingSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetTopRatingSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetTopRatingSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsTopRatedSeriesWithCategory() = runTest {
        val page = 1
        val categoryId = "10765"

        coEvery { seriesRepository.getTopRatingSeries(page, categoryId) } returns expectedWithCategory

        val result = useCase.getTopRatingSeries(page, categoryId)

        assertEquals(expectedWithCategory, result)
        coVerify { seriesRepository.getTopRatingSeries(page, categoryId) }
    }

    @Test
    fun testReturnsTopRatedSeriesWithoutCategory() = runTest {
        val page = 2
        val categoryId: String? = null

        coEvery { seriesRepository.getTopRatingSeries(page, categoryId) } returns expectedWithoutCategory

        val result = useCase.getTopRatingSeries(page, categoryId)

        assertEquals(expectedWithoutCategory, result)
        coVerify { seriesRepository.getTopRatingSeries(page, categoryId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val categoryId = "80"
        val exception = RuntimeException("Failed to fetch top-rated series")

        coEvery { seriesRepository.getTopRatingSeries(page, categoryId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getTopRatingSeries(page, categoryId)
        }

        assertEquals("Failed to fetch top-rated series", thrown.message)
        coVerify { seriesRepository.getTopRatingSeries(page, categoryId) }
    }

    companion object {
        val expectedWithCategory = listOf(
            Series(
                id = 1300L,
                title = "Galaxy Rulers",
                rating = 9.2f,
                posterPath = "/toprated1.jpg",
                trailerPath = "https://youtube.com/galaxyrulers",
                genres = listOf(Genre(10765, "Sci-Fi & Fantasy")),
                overview = "An interstellar saga of power and rebellion.",
                releaseDate = 1722297600000L,
                seasonsCount = 5
            )
        )

        val expectedWithoutCategory = listOf(
            Series(
                id = 1301L,
                title = "Legacy of Legends",
                rating = 8.8f,
                posterPath = "/toprated2.jpg",
                trailerPath = "https://youtube.com/legacyoflegends",
                genres = listOf(Genre(18, "Drama")),
                overview = "Chronicles of legendary heroes and their downfall.",
                releaseDate = 1722384000000L,
                seasonsCount = 4
            )
        )
    }
}
