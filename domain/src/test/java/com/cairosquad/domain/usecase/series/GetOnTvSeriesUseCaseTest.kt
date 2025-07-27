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

class GetOnTvSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetOnTvSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetOnTvSeriesUseCase(seriesRepository)
    }

    @Test
    fun testReturnsOnTvSeriesWithGenre() = runTest {
        val page = 1
        val genreId = "10766"

        coEvery { seriesRepository.getOnTvSeries(page, genreId) } returns expectedWithGenre

        val result = useCase.getOnTvSeries(page, genreId)

        assertEquals(expectedWithGenre, result)
        coVerify { seriesRepository.getOnTvSeries(page, genreId) }
    }

    @Test
    fun testReturnsOnTvSeriesWithoutGenre() = runTest {
        val page = 2
        val genreId: String? = null

        coEvery { seriesRepository.getOnTvSeries(page, genreId) } returns expectedWithoutGenre

        val result = useCase.getOnTvSeries(page, genreId)

        assertEquals(expectedWithoutGenre, result)
        coVerify { seriesRepository.getOnTvSeries(page, genreId) }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val page = 3
        val genreId = "99"
        val exception = RuntimeException("Failed to fetch On TV series")

        coEvery { seriesRepository.getOnTvSeries(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getOnTvSeries(page, genreId)
        }

        assertEquals("Failed to fetch On TV series", thrown.message)
        coVerify { seriesRepository.getOnTvSeries(page, genreId) }
    }

    companion object {
        val expectedWithGenre = listOf(
            Series(
                id = 1000L,
                title = "Daily Drama",
                rating = 7.6f,
                posterPath = "/ontv1.jpg",
                trailerPath = "https://youtube.com/dailydrama",
                genres = listOf(Genre(10766, "Soap")),
                overview = "A gripping soap opera that airs every evening.",
                releaseDate = 1721865600000L,
                seasonsCount = 6
            )
        )

        val expectedWithoutGenre = listOf(
            Series(
                id = 1001L,
                title = "Prime Time Show",
                rating = 8.3f,
                posterPath = "/ontv2.jpg",
                trailerPath = "https://youtube.com/primetimeshow",
                genres = listOf(Genre(35, "Comedy")),
                overview = "The top comedy series airing this season.",
                releaseDate = 1721952000000L,
                seasonsCount = 3
            )
        )
    }
}
