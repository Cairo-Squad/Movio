package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRecommendationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSearchRecommendationUseCaseTest {

    private lateinit var useCase: GetSearchRecommendationUseCase
    private val repository: SearchRecommendationRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetSearchRecommendationUseCase(repository)
    }

    @Test
    fun `should return search recommendations when invoke is called`() = runTest {
        // Given
        val query = "action"
        val expectedRecommendations = listOf("action movie", "action drama", "action comedy")
        coEvery { repository.getSearchRecommendation(query) } returns expectedRecommendations

        // When
        val result = useCase(query)

        // Then
        assertEquals(expectedRecommendations, result)
        coVerify(exactly = 1) { repository.getSearchRecommendation(query) }
    }
}
