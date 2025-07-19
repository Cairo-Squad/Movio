package com.cairosquad.viewmodel.foryou

import androidx.paging.testing.asSnapshot
import com.cairosquad.domain.usecase.movies.GetPersonalizedMoviesUseCase
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ForYouPagerTest {

    private lateinit var getPersonalizedMoviesUseCase: GetPersonalizedMoviesUseCase
    private lateinit var forYouPager: ForYouPager

    @Before
    fun setUp() {
        getPersonalizedMoviesUseCase = mockk()
        forYouPager = ForYouPager(getPersonalizedMoviesUseCase)
    }

    @Test
    fun `movies should emit PagingData with correct movie items`() = runTest {
        val expected = listOf(
            Movie(
                id = 1, title = "Inception", rating = 8.8f, posterPath = "/inception.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = ""
            ),
            Movie(id = 2, title = "Interstellar", rating = 8.6f, posterPath = "/interstellar.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                runtimeMinutes = 0,
                trailerPath = "")
        )
        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(1) } returns expected
        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(match { it != 1 }) } returns emptyList()

        val flow = forYouPager.movies()
        val result = flow.asSnapshot()

        assertEquals(expected, result)
    }


    @Test
    fun `movies should handle empty results`() = runTest {
        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(any()) } returns emptyList()

        val flow = forYouPager.movies()
        val result = flow.asSnapshot()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `movies should propagate errors from use case`() = runTest {
        val testException = RuntimeException("Test error")
        coEvery { getPersonalizedMoviesUseCase.getPersonalizedMovies(any()) } throws testException

        val flow = forYouPager.movies()
        val result = runCatching { flow.asSnapshot() }

        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull()?.cause)
    }

}