package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchHistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetSearchHistoryUseCaseTest {

    private lateinit var searchHistoryUseCase: GetSearchHistoryUseCase
    private lateinit var searchHistoryRepository: SearchHistoryRepository

    @Before
    fun setUp() {
        searchHistoryRepository = mockk(relaxed = true)
        searchHistoryUseCase = GetSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `searchHistoryUseCase should call repository and get Search History`() = runTest {
        // Given
        val searchQuery = "A"

        // When
        searchHistoryUseCase.getByQuery(searchQuery)

        // Then
        coVerify(exactly = 1) { searchHistoryRepository.getByQuery(searchQuery) }
    }

    @Test
    fun `getByQuery should return results that match the search query`() = runTest {
        //Given
        val searchQuery = searchQueyWhenReturnMatches
        val expectedResults = expectedResultsForMatchingSearchQuery
        coEvery { searchHistoryRepository.getByQuery(searchQuery) } returns expectedResults

        //When
        val result = searchHistoryUseCase.getByQuery(searchQuery)

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { searchHistoryRepository.getByQuery(searchQuery) }
    }

    @Test
    fun `getAll should return all results of search history`() = runTest {
        val expectedResults = expectedResultforGetAll
        coEvery { searchHistoryRepository.getAll() } returns expectedResults

        //When
        val result = searchHistoryUseCase.getAll()

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { searchHistoryRepository.getAll() }
    }

    companion object {
        const val searchQueyWhenReturnMatches = "A"
        val expectedResultsForMatchingSearchQuery = listOf("Ana messa", "Arkan")
        val expectedResultforGetAll = listOf("Ana messa", "Arkan","Movie","Art")

    }
}
