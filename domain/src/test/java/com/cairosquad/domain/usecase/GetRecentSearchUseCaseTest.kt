package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.domain.search.usecase.GetRecentSearchUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetRecentSearchUseCaseTest {

    private lateinit var searchHistoryUseCase: GetRecentSearchUseCase
    private lateinit var recentSearchRepository: RecentSearchRepository

    @Before
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        searchHistoryUseCase = GetRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call repository getByQuery when getByQuery is invoked`() = runTest {
        // Given
        val searchQuery = "A"

        // When
        searchHistoryUseCase.getByQuery(searchQuery)

        // Then
        coVerify { recentSearchRepository.getByQuery(searchQuery) }
    }

    @Test
    fun `should return matching results when query is provided`() = runTest {
        //Given
        val searchQuery = searchQueyWhenReturnMatches
        val expectedResults = expectedResultsForMatchingSearchQuery
        coEvery { recentSearchRepository.getByQuery(searchQuery) } returns expectedResults

        //When
        val result = searchHistoryUseCase.getByQuery(searchQuery)

        //Then
        assertEquals(expectedResults, result)
        coVerify{ recentSearchRepository.getByQuery(searchQuery) }
    }

    @Test
    fun `should return all recent search results when getAll is invoked`() = runTest {
        val expectedResults = expectedResultGetAll
        coEvery { recentSearchRepository.getAll() } returns expectedResults

        //When
        val result = searchHistoryUseCase.getAll()

        //Then
        assertEquals(expectedResults, result)
        coVerify { recentSearchRepository.getAll() }
    }

    companion object {
        const val searchQueyWhenReturnMatches = "A"
        val expectedResultsForMatchingSearchQuery = listOf("Ana messa", "Arkan")
        val expectedResultGetAll = listOf("Ana messa", "Arkan","Movie","Art")

    }
}
