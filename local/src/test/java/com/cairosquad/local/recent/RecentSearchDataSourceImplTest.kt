package com.cairosquad.local.recent

import com.cairosquad.local.search.recent.RecentSearchDataSourceImpl
import com.cairosquad.local.search.recent.dao.RecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
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
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class RecentSearchDataSourceImplTest {

    private val dao = mockk<RecentSearchDao>()
    private lateinit var dataSource: RecentSearchDataSourceImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dataSource = RecentSearchDataSourceImpl(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getByQuery returns mapped strings`() = runTest {
        // Given
        val query = "leon"
        val entities = listOf(
            RecentSearchEntity("leonardo"),
            RecentSearchEntity("leon"),
            RecentSearchEntity("leona")
        )
        coEvery { dao.getAll(query) } returns entities
        // When
        val result = dataSource.getByQuery(query)
        // Then
        coVerify { dao.getAll(query) }
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
            dao.insertQuery(match { it is RecentSearchEntity && it.query == query })
        }
    }

    @Test
    fun `getAll returns mapped list`() = runTest {
        // Given
        val entities = listOf(
            RecentSearchEntity("dune"),
            RecentSearchEntity("interstellar")
        )
        coEvery { dao.getAll() } returns entities
        // When
        val result = dataSource.getAll()
        // Then
        coVerify { dao.getAll() }
    }
}
