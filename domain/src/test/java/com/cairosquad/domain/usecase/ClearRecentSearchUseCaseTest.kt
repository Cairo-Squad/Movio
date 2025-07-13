package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.domain.search.usecase.ClearRecentSearchUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearRecentSearchUseCaseTest {

    private lateinit var recentSearchRepository: RecentSearchRepository
    private lateinit var clearRecentSearchUseCase: ClearRecentSearchUseCase

    @Before
    fun setUp() {
        recentSearchRepository = mockk(relaxed = true)
        clearRecentSearchUseCase = ClearRecentSearchUseCase(recentSearchRepository)
    }

    @Test
    fun `should call clearAll on repository when clearAll is invoked`() = runTest {
        // When
        clearRecentSearchUseCase.clearAll()

        // Then
        coVerify(exactly = 1) { recentSearchRepository.clearAll() }
    }

    @Test
    fun `should call removeQuery on repository with correct argument when removeQuery is invoked`() = runTest {
        // Given
        val testQuery = "Movie"

        // When
        clearRecentSearchUseCase.removeQuery(testQuery)

        // Then
        coVerify(exactly = 1) { recentSearchRepository.removeQuery(testQuery) }
    }
}

