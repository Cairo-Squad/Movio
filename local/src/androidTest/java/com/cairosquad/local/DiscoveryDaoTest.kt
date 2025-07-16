package com.cairosquad.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cairosquad.local.common.MovioDataBase
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.discovery.dao.DiscoveryDao
import com.cairosquad.local.search.discovery.dto.PersonalizedMoviesIdsDto
import com.cairosquad.local.search.discovery.dto.SuggestedMoviesIdsDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscoveryDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MovioDataBase
    private lateinit var discoveryDao: DiscoveryDao
    private lateinit var cacheDao: CacheDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovioDataBase::class.java
        ).allowMainThreadQueries().build()

        discoveryDao = database.discoveryDao()
        cacheDao = database.cacheDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun cacheAndRetrievePersonalizedMoviesIds_returnsCorrectMovies() = runTest {
        // Given
        cacheDao.cacheMovies(listOf(MOVIE_1, MOVIE_2))
        discoveryDao.cachePersonalizedMoviesIds(listOf(ID_1, ID_2))

        // When
        val result = discoveryDao.getPersonalizedMoviesIds()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(MOVIE_1, MOVIE_2)
    }

    @Test
    fun deleteExpiredPersonalizedMoviesId_removesExpiredOnesOnly() = runTest {
        // Given
        cacheDao.cacheMovies(listOf(MOVIE_EXPIRED, MOVIE_VALID))
        discoveryDao.cachePersonalizedMoviesIds(
            listOf(
                PersonalizedMoviesIdsDto(MOVIE_EXPIRED.id),
                PersonalizedMoviesIdsDto(MOVIE_VALID.id)
            )
        )

        // When
        discoveryDao.deleteExpiredPersonalizedMoviesId(TIMESTAMP - 3000000)

        // Then
        val result = discoveryDao.getPersonalizedMoviesIds()
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(MOVIE_VALID)
    }

    @Test
    fun cacheAndRetrieveSuggestedMoviesIds_returnsCorrectMovies() = runTest {
        // Given
        cacheDao.cacheMovies(listOf(MOVIE_1))
        discoveryDao.cacheSuggestedMovies(listOf(SUGGESTED_ID_1))

        // When
        val result = discoveryDao.getSuggestedMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(MOVIE_1)
    }

    @Test
    fun deleteExpiredSuggestedMoviesId_removesExpiredOnesOnly() = runTest {
        // Given
        cacheDao.cacheMovies(listOf(MOVIE_EXPIRED, MOVIE_VALID))
        discoveryDao.cacheSuggestedMovies(
            listOf(
                SuggestedMoviesIdsDto(MOVIE_EXPIRED.id),
                SuggestedMoviesIdsDto(MOVIE_VALID.id)
            )
        )

        // When
        discoveryDao.deleteExpiredSuggestedMoviesId(TIMESTAMP - 3000000)

        // Then
        val result = discoveryDao.getSuggestedMovies()
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(MOVIE_VALID)
    }

    companion object {
        val TIMESTAMP = System.currentTimeMillis()
        val OLD_TIMESTAMP = TIMESTAMP - 3600000

        val MOVIE_1 = MovieCacheDto(1, "Movie One", "/poster1.jpg", 7.5, TIMESTAMP)
        val MOVIE_2 = MovieCacheDto(2, "Movie Two", "/poster2.jpg", 8.0, TIMESTAMP)
        val MOVIE_EXPIRED = MovieCacheDto(3, "Expired Movie", "/expired.jpg", 6.0, OLD_TIMESTAMP)
        val MOVIE_VALID = MovieCacheDto(4, "Valid Movie", "/valid.jpg", 7.2, TIMESTAMP)

        val SUGGESTED_ID_1 = SuggestedMoviesIdsDto(1)
        val ID_1 = PersonalizedMoviesIdsDto(1)
        val ID_2 = PersonalizedMoviesIdsDto(2)
    }
}

