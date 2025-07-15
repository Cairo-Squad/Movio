package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.DiscoveryRepository
import com.cairosquad.domain.search.usecase.GetPersonalizedMoviesUseCase
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

class GetPersonalizedMoviesUseCaseTest {

    private val recommendationRepository = mockk<DiscoveryRepository>()
    private lateinit var useCase: GetPersonalizedMoviesUseCase

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = GetPersonalizedMoviesUseCase(recommendationRepository)
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
        coEvery { recommendationRepository.getPersonalizedMovies() } returns expectedMovies

        // When
        val result = useCase.getPersonalizedMovies()

        // Then
        coVerify { recommendationRepository.getPersonalizedMovies() }
    }
}
