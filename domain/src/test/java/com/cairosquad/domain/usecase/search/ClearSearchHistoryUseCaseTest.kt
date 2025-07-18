package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.SearchRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearSearchHistoryUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var clearRecentSearchUseCase: ClearSearchHistoryUseCase

    @Before
    fun setUp() {
        searchRepository = mockk(relaxed = true)
        clearRecentSearchUseCase = ClearSearchHistoryUseCase(searchRepository)
    }

    @Test
    fun `should call clearAll on repository when clearAll is invoked`() = runTest {
        clearRecentSearchUseCase.clearAllHistory()
        coVerify(exactly = 1) { searchRepository.clearAll() }
    }

    @Test
    fun `should call removeQuery on repository with correct argument when removeQuery is invoked`() = runTest {
        // Given
        val testQuery = "Movie"
        clearRecentSearchUseCase.removeQueryFromHistory(testQuery)
        coVerify(exactly = 1) { searchRepository.removeQuery(testQuery) }
    }
}
