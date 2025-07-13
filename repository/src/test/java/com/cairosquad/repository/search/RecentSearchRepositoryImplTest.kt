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
    private lateinit var repository: RecentSearchRepositoryImpl

    @Before
    fun setup() {
        localDataSource = mockk()
        repository = RecentSearchRepositoryImpl(localDataSource)
    }

    @Test
    fun `getAll should return all queries from local data source`() = runTest {
        //Given
        val expectedQueries = listOf("query1", "query2", "query3")
        coEvery { localDataSource.getAll() } returns expectedQueries

        //When
        val result = repository.getAll()

        //Then
        Assert.assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { localDataSource.getAll() }
    }

    @Test
    fun `getByQuery when query is blank should return all queries`() = runTest {
        // Given
        val expectedQueries = listOf("allQuery1", "allQuery2")
        val blankQuery = ""
        coEvery { localDataSource.getAll() } returns expectedQueries
        //When
        val result = repository.getByQuery(blankQuery)

        //Then
        Assert.assertEquals(expectedQueries, result)
        coVerify(exactly = 1) { localDataSource.getAll() }

        coVerify(exactly = 0) { localDataSource.getByQuery(any()) }
    }

    @Test
    fun `getByQuery when query is not blank should return filtered queries`() = runTest {
        //Given
        val expectedFilteredQueries = listOf("filteredQuery1", "filteredQuery2")
        val specificQuery = "search term"
        coEvery { localDataSource.getByQuery(specificQuery) } returns expectedFilteredQueries

        // When
        val result = repository.getByQuery(specificQuery)

        // Then
        Assert.assertEquals(expectedFilteredQueries, result)
        coVerify(exactly = 1) { localDataSource.getByQuery(specificQuery) }
        coVerify(exactly = 0) { localDataSource.getAll() }
    }

    @Test
    fun `clearAll should call clearAll on local data source`() = runTest {
        // Given
        coEvery { localDataSource.clearAll() } returns Unit

        // When
        repository.clearAll()

        //Then
        coVerify(exactly = 1) { localDataSource.clearAll() }
    }

    @Test
    fun `removeQuery should call removeQuery on local data source`() = runTest {
        // Given
        val queryToRemove = "old query"
        coEvery { localDataSource.removeQuery(queryToRemove) } returns Unit

        //When
        repository.removeQuery(queryToRemove)

        // Then
        coVerify(exactly = 1) { localDataSource.removeQuery(queryToRemove) }
    }

    @Test
    fun `addQuery should call addQuery on local data source`() = runTest {
        //Given
        val queryToAdd = "new query"
        coEvery { localDataSource.addQuery(queryToAdd) } returns Unit

        //When
        repository.addQuery(queryToAdd)

        //Then
        coVerify(exactly = 1) { localDataSource.addQuery(queryToAdd) }
    }
}