package com.cairosquad.local.recent

import com.cairosquad.local.search.recent.LocalRecentSearchDataSourceImpl
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.repository.search.data_source.local.dto.RecentSearchEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class LocalRecentSearchDataSourceImplTest {

    private val dao = mockk<LocalRecentSearchDao>()
    private lateinit var dataSource: LocalRecentSearchDataSourceImpl
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dataSource = LocalRecentSearchDataSourceImpl(dao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return mapped strings when getByQuery is called`() = runTest {
        // Given
        val query = "leon"
        val entities = listOf(
            RecentSearchEntity("leonardo"),
            RecentSearchEntity("leon"),
            RecentSearchEntity("leona")
        )
        coEvery { dao.getAllQueries(query) } returns entities
        // When
        val result = dataSource.getByQuery(query)
        // Then
        coVerify { dao.getAllQueries(query) }
    }

    @Test
    fun `clearAll calls dao clearAll`() = runTest {
        // Given
        coEvery { dao.clearAll() } just runs
        // When
        dataSource.clearAll()
        // Then
        coVerify { dao.clearAll() }
    }

    @Test
    fun `removeQuery deletes specific query from dao`() = runTest {
        // Given
        val query = "matrix"
        coEvery { dao.deleteQuery(query) } just runs
        // When
        dataSource.removeQuery(query)
        // Then
        coVerify { dao.deleteQuery(query) }
    }

    @Test
    fun `addQuery inserts entity into dao`() = runTest {
        // Given
        val query = "inception"
        coEvery { dao.insertQuery(any()) } just runs
        // When
        dataSource.addQuery(query)
        // Then
        coVerify {
            dao.insertQuery(match { it.query == query })
        }
    }

    @Test
    fun `should return mapped list when getAll is called`() = runTest {
        // Given
        val entities = listOf(
            RecentSearchEntity("dune"),
            RecentSearchEntity("interstellar")
        )
        coEvery { dao.getAllQueries() } returns entities
        // When
        val result = dataSource.getAll()
        // Then
        coVerify { dao.getAllQueries() }
    }
}
