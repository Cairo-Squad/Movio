package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetPersonalizedMoviesUseCaseTest {

    private val moviesRepository = mockk<MoviesRepository>()
    private lateinit var useCase: GetPersonalizedMoviesUseCase

    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = GetPersonalizedMoviesUseCase(moviesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
        coEvery { moviesRepository.getPersonalizedMovies() } returns expectedMovies

        // When
        val result = useCase.getPersonalizedMovies()

        // Then
        coVerify { moviesRepository.getPersonalizedMovies() }
    }
}
