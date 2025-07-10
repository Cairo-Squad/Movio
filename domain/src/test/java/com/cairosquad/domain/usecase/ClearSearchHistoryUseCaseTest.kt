package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearSearchHistoryUseCaseTest {

    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        clearRecentSearchUseCase = ClearRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `clearAll should call repository's clearAll`() = runTest {
        clearRecentSearchUseCase.clearAll()
        coVerify(exactly = 1) { recentSearchRepository.clearAll() }
    }

    @Test
    fun `removeQuery should call repository's removeQuery with correct argument`() = runTest {
        val testQuery = "Movie"
        clearRecentSearchUseCase.removeQuery(testQuery)
        coVerify(exactly = 1) { recentSearchRepository.removeQuery(testQuery) }
    }
}
