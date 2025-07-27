package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ManageSearchHistoryUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var manageSearchHistoryUseCase: ManageSearchHistoryUseCase

    @Before
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        manageSearchHistoryUseCase = ManageSearchHistoryUseCase(searchRepository)
    }

    @Test
    fun `should call clearAll on repository when clearAll is invoked`() = runTest {
        manageSearchHistoryUseCase.clearAllHistory()
        coVerify(exactly = 1) { searchRepository.clearAll() }
    }

    @Test
    fun `should call removeQuery on repository with correct argument when removeQuery is invoked`() =
        runTest {
            // Given
            val testQuery = "Movie"
            manageSearchHistoryUseCase.removeQueryFromHistory(testQuery)
            coVerify(exactly = 1) { searchRepository.removeQuery(testQuery) }
        }

    @Test
    fun `should call repository getByQuery when getByQuery is invoked`() = runTest {
        // Given
        val searchQuery = "A"

        // When
        manageSearchHistoryUseCase.getByQuery(searchQuery)

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
        val result = manageSearchHistoryUseCase.getByQuery(searchQuery)

        //Then
        assertEquals(expectedResults, result)
        coVerify(exactly = 1) { searchRepository.getAllHistoryByQuery(searchQuery) }
    }

    @Test
    fun `getAll should return all results of search history`() = runTest {
        val expectedResults = expectedResultForGetAll
        coEvery { searchRepository.getAllHistory() } returns expectedResults

        //When
        val result = manageSearchHistoryUseCase.getAll()

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