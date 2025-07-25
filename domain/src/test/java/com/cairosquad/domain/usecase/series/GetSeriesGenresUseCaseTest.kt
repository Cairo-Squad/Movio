import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.domain.usecase.series.GetSeriesGenresUseCase
import com.cairosquad.entity.Genre
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetSeriesGenresUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var useCase: GetSeriesGenresUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        useCase = GetSeriesGenresUseCase(seriesRepository)
    }

    @Test
    fun testReturnsSeriesGenresSuccessfully() = runTest {
        coEvery { seriesRepository.getSeriesGenres() } returns expectedGenres

        val result = useCase.getSeriesGenres()

        assertEquals(expectedGenres, result)
        coVerify { seriesRepository.getSeriesGenres() }
    }

    @Test
    fun testThrowsExceptionWhenRepositoryFails() = runTest {
        val exception = RuntimeException("Failed to load genres")
        coEvery { seriesRepository.getSeriesGenres() } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getSeriesGenres()
        }

        assertEquals("Failed to load genres", thrown.message)
        coVerify { seriesRepository.getSeriesGenres() }
    }

    companion object {
        val expectedGenres = listOf(
            Genre(id = 18, name = "Drama"),
            Genre(id = 10759, name = "Action & Adventure"),
            Genre(id = 35, name = "Comedy")
        )
    }
}
