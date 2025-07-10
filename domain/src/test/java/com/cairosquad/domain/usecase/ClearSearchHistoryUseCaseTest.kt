package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchHistoryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearSearchHistoryUseCaseTest {

    private lateinit var searchHistoryRepository: SearchHistoryRepository
    private lateinit var clearSearchHistoryUseCase: ClearSearchHistoryUseCase

    @Before
    fun setUp() {
        searchHistoryRepository = mockk(relaxed = true)
        clearSearchHistoryUseCase = ClearSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `clearAll should call repository's clearAll`() = runTest {
        clearSearchHistoryUseCase.clearAll()
        coVerify(exactly = 1) { searchHistoryRepository.clearAll() }
    }

    @Test
    fun `removeQuery should call repository's removeQuery with correct argument`() = runTest {
        val testQuery = "Movie"
        clearSearchHistoryUseCase.removeQuery(testQuery)
        coVerify(exactly = 1) { searchHistoryRepository.removeQuery(testQuery) }
    }
}
