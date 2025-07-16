package com.cairosquad.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cairosquad.local.common.MovioDataBase
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalRecentSearchDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MovioDataBase
    private lateinit var recentSearchDao: LocalRecentSearchDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovioDataBase::class.java
        ).allowMainThreadQueries().build()

        recentSearchDao = database.recentSearchDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertQuery_andGetAllQueries_returnsCorrectlyOrderedList() = runTest {
        // Given
        recentSearchDao.insertQuery(QUERY_1)
        delay(10) // Ensure timestamp difference
        recentSearchDao.insertQuery(QUERY_2)

        // When
        val result = recentSearchDao.getAllQueries()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(QUERY_2) // newer timestamp
        assertThat(result[1]).isEqualTo(QUERY_1)
    }

    @Test
    fun insertQuery_andSearchByPartialQuery_returnsMatchingResults() = runTest {
        // Given
        recentSearchDao.insertQuery(QUERY_1)
        recentSearchDao.insertQuery(QUERY_2)

        // When
        val result = recentSearchDao.getAllQueries("swift")

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].query).contains("swift")
    }

    @Test
    fun deleteSpecificQuery_removesCorrectItem() = runTest {
        // Given
        recentSearchDao.insertQuery(QUERY_1)
        recentSearchDao.insertQuery(QUERY_2)

        // When
        recentSearchDao.deleteQuery(QUERY_1.query)
        val result = recentSearchDao.getAllQueries()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].query).isEqualTo(QUERY_2.query)
    }

    @Test
    fun clearAllQueries_removesAllData() = runTest {
        // Given
        recentSearchDao.insertQuery(QUERY_1)
        recentSearchDao.insertQuery(QUERY_2)

        // When
        recentSearchDao.clearAll()
        val result = recentSearchDao.getAllQueries()

        // Then
        assertThat(result).isEmpty()
    }

    companion object {
        private val TIMESTAMP_1 = System.currentTimeMillis()
        private val TIMESTAMP_2 = TIMESTAMP_1 + 1000

        val QUERY_1 = RecentSearchEntity(
            query = "taylor swift",
            timestamp = TIMESTAMP_1
        )

        val QUERY_2 = RecentSearchEntity(
            query = "eminem",
            timestamp = TIMESTAMP_2
        )
    }
}

