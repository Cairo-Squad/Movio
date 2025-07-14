package com.cairosquad.repository.search

import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RecentSearchRepositoryImplTest {
    private lateinit var localDataSource: LocalRecentSearchDataSource
    private lateinit var repository: LocalRecentSearchRepositoryImpl

    @Before
    fun setup() {
        localDataSource = mockk()
        repository = LocalRecentSearchRepositoryImpl(localDataSource)
    }

    @Test
    fun `should return all queries when getAll is called`() = runTest {
        // Given
        val expectedQueries = listOf("query1", "query2", "query3")
        coEvery { localDataSource.getAll() } returns expectedQueries

        //When
        val result = repository.getAllHistory()

        // Then
        Assert.assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `should return all queries when getByQuery is called with blank query`() = runTest {
        // Given
        val expectedQueries = emptyList<String>()
        val blankQuery = ""
        coEvery { localDataSource.getAll() } returns expectedQueries
        //When
        val result = repository.getAllHistoryByQuery(blankQuery)

        // Then
        Assert.assertEquals(expectedQueries, result)
    }

    @Test
    fun `should return filtered queries when getByQuery is called with non-blank query`() =
        runTest {
            // Given
            val expectedFilteredQueries = listOf("filteredQuery1", "filteredQuery2")
            val specificQuery = "search term"
            coEvery { localDataSource.getByQuery(specificQuery) } returns expectedFilteredQueries

        // When
        val result = repository.getAllHistoryByQuery(specificQuery)

            // Then
            Assert.assertEquals(expectedFilteredQueries, result)
            coVerify(exactly = 1) { localDataSource.getByQuery(specificQuery) }
            coVerify(exactly = 0) { localDataSource.getAll() }
        }

    @Test
    fun `should clear all queries when clearAll is called`() = runTest {
        // Given
        coEvery { localDataSource.clearAll() } returns Unit

        // When
        repository.clearAll()

        // Then
        coVerify(exactly = 1) { localDataSource.clearAll() }
    }

    @Test
    fun `should remove specific query when removeQuery is called`() = runTest {
        // Given
        val queryToRemove = "old query"
        coEvery { localDataSource.removeQuery(queryToRemove) } returns Unit

        // When
        repository.removeQuery(queryToRemove)

        // Then
        coVerify(exactly = 1) { localDataSource.removeQuery(queryToRemove) }
    }

    @Test
    fun `should add query when addQuery is called`() = runTest {
        // Given
        val queryToAdd = "new query"
        coEvery { localDataSource.addQuery(queryToAdd) } returns Unit

        // When
        repository.addQuery(queryToAdd)

        // Then
        coVerify(exactly = 1) { localDataSource.addQuery(queryToAdd) }
    }

}