package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.usecase.GetLocalSearchHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetRecentSearchUseCaseTest {

    private lateinit var searchHistoryUseCase: GetLocalSearchHistoryUseCase
    private lateinit var recentSearchRepository: SearchHistoryRepository

    @Before
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        searchHistoryUseCase = GetLocalSearchHistoryUseCase(recentSearchRepository)
    }

    @Test
    fun `searchHistoryUseCase should call repository and get Search History`() = runTest {
        // Given
        val searchQuery = "A"

        // When
        searchHistoryUseCase.getByQuery(searchQuery)

        // Then
        coVerify(exactly = 1) { recentSearchRepository.getAllHistoryByQuery(searchQuery) }
    }

    @Test
    fun `getByQuery should return results that match the search query`() = runTest {
        //Given
        val searchQuery = searchQueyWhenReturnMatches
        val expectedResults = expectedResultsForMatchingSearchQuery
        coEvery { recentSearchRepository.getAllHistoryByQuery(searchQuery) } returns expectedResults

        //When
        val result = searchHistoryUseCase.getByQuery(searchQuery)

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { recentSearchRepository.getAllHistoryByQuery(searchQuery) }
    }

    @Test
    fun `getAll should return all results of search history`() = runTest {
        val expectedResults = expectedResultforGetAll
        coEvery { recentSearchRepository.getAllHistory() } returns expectedResults

        //When
        val result = searchHistoryUseCase.getAll()

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { recentSearchRepository.getAllHistory() }
    }

    companion object {
        const val searchQueyWhenReturnMatches = "A"
        val expectedResultsForMatchingSearchQuery = listOf("Ana messa", "Arkan")
        val expectedResultforGetAll = listOf("Ana messa", "Arkan","Movie","Art")

    }
}
