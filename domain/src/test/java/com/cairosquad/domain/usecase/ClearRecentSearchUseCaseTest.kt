package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.usecase.ClearSearchHistoryUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearRecentSearchUseCaseTest {

    private lateinit var recentSearchRepository: SearchHistoryRepository
    private lateinit var clearRecentSearchUseCase: ClearSearchHistoryUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        clearRecentSearchUseCase = ClearSearchHistoryUseCase(recentSearchRepository)
    }

    @Test
    fun `clearAll should call repository's clearAll`() = runTest {
        clearRecentSearchUseCase.clearAllHistory()
        coVerify(exactly = 1) { recentSearchRepository.clearAll() }
    }

    @Test
    fun `removeQuery should call repository's removeQuery with correct argument`() = runTest {
        val testQuery = "Movie"
        clearRecentSearchUseCase.removeQueryFromHistory(testQuery)
        coVerify(exactly = 1) { recentSearchRepository.removeQuery(testQuery) }
    }
}
