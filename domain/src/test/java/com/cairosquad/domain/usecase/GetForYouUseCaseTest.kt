package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.domain.search.usecase.GetForYouUseCase
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetForYouUseCaseTest {

    private val recommendationRepository = mockk<RecommendationRepository>()
    private lateinit var useCase: GetForYouUseCase
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = GetForYouUseCase(recommendationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return a list of Personalized movies from the repository when getPersonalizedMovies is called`() = runTest {
        // Given
        val expectedMovies = listOf(
            Movie(id = 101, title = "Dune", rating = 8.1f, posterPath = "/dune.jpg"),
            Movie(id = 102, title = "Blade Runner", rating = 8.0f, posterPath = "/blade.jpg")
        )
        coEvery { recommendationRepository.getForYouMovies() } returns expectedMovies

        // When
        val result = useCase.getForYouMovies()

        // Then
        coVerify(exactly = 1) { recommendationRepository.getForYouMovies() }
        assertEquals(expectedMovies, result)
    }
}
