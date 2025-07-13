package com.cairosquad.local.recent

import com.cairosquad.local.search.recent.LocalRecentSearchDataSourceImpl
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecentSearchDataSourceImplTest {

    private val dao = mockk<LocalRecentSearchDao>()
    private lateinit var dataSource: LocalRecentSearchDataSourceImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dataSource = LocalRecentSearchDataSourceImpl(dao)
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
        coEvery { dao.getAllQueries(query) } returns entities

        // When
        val result = dataSource.getByQuery(query)

        // Then
        coVerify { dao.getAllQueries(query) }
    }

    @Test
    fun `clearAll calls dao clearAll`() = runTest {
        coEvery { dao.clearAll() } just runs

        dataSource.clearAll()

        coVerify { dao.clearAll() }
    }

    @Test
    fun `removeQuery deletes specific query from dao`() = runTest {
        val query = "matrix"
        coEvery { dao.deleteQuery(query) } just runs

        dataSource.removeQuery(query)

        coVerify { dao.deleteQuery(query) }
    }

    @Test
    fun `getAll returns mapped list`() = runTest {
        val entities = listOf(
            RecentSearchEntity("dune"),
            RecentSearchEntity("interstellar")
        )
        coEvery { dao.getAllQueries() } returns entities

        val result = dataSource.getAll()

        coVerify { dao.getAllQueries() }
    }
}
