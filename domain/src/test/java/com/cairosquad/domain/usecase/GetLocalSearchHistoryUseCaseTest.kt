package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.usecase.search.GetLocalSearchHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetLocalSearchHistoryUseCaseTest {

    private lateinit var searchHistoryUseCase: GetLocalSearchHistoryUseCase
    private lateinit var searchRepository: SearchRepository

    @Before
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        searchHistoryUseCase = GetLocalSearchHistoryUseCase(searchRepository)
    }

    @Test
    fun `should call repository getByQuery when getByQuery is invoked`() = runTest {
        // Given
        val searchQuery = "A"

        // When
        searchHistoryUseCase.getByQuery(searchQuery)

        // Then
        coVerify(exactly = 1) { searchRepository.getAllHistoryByQuery(searchQuery) }
    }

    @Test
    fun `should return matching results when query is provided`() = runTest {
        //Given
        val searchQuery = SEARCH_QUERY_WHEN_RETURN_MATCHES
        val expectedResults = expectedResultsForMatchingSearchQuery
        coEvery { searchRepository.getAllHistoryByQuery(searchQuery) } returns expectedResults

        //When
        val result = searchHistoryUseCase.getByQuery(searchQuery)

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { searchRepository.getAllHistoryByQuery(searchQuery) }
    }

    @Test
    fun `getAll should return all results of search history`() = runTest {
        val expectedResults = expectedResultForGetAll
        coEvery { searchRepository.getAllHistory() } returns expectedResults

        //When
        val result = searchHistoryUseCase.getAll()

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { searchRepository.getAllHistory() }
    }

    companion object {
        const val SEARCH_QUERY_WHEN_RETURN_MATCHES = "A"
        val expectedResultsForMatchingSearchQuery = listOf("Ana messa", "Arkan")
        val expectedResultForGetAll = listOf("Ana messa", "Arkan", "Movie", "Art")

    }
}
