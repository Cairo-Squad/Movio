package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.domain.search.usecase.GetExploreMoreUseCase
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetExploreMoreUseCaseTest {

    private val recommendationRepository = mockk<RecommendationRepository>()
    private lateinit var useCase: GetExploreMoreUseCase

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = GetExploreMoreUseCase(recommendationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getExploreMoreMovies should return list of movies from repository`() = runTest {
        // Given
        val expectedMovies = listOf(
            Movie(id = 1, title = "Interstellar", rating = 8.6f, posterPath = "/interstellar.jpg"),
            Movie(id = 2, title = "Inception", rating = 8.8f, posterPath = "/inception.jpg")
        )
        coEvery { recommendationRepository.getExploreMoreMovies() } returns expectedMovies

        // When
        val result = useCase.getExploreMoreMovies()

        // Then
        coVerify { recommendationRepository.getExploreMoreMovies() }
    }
}
