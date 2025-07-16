package com.cairosquad.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cairosquad.local.common.MovioDataBase
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MovioDataBase
    private lateinit var cacheDao: CacheDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovioDataBase::class.java
        ).allowMainThreadQueries().build()

        cacheDao = database.cacheDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetCachedMovies_returnsCorrectResults() = runTest {
        cacheDao.cacheMovies(listOf(MOVIE_1, MOVIE_2))
        val result = cacheDao.getCachedMovies()
        assertThat(result).isEqualTo(listOf(MOVIE_1, MOVIE_2))
    }

    @Test
    fun getCachedMovies_withQuery_returnsMatchingResults() = runTest {
        cacheDao.cacheMovies(listOf(MOVIE_1, MOVIE_2))
        val result = cacheDao.getCachedMovies("Avengers")
        assertThat(result).hasSize(2)
    }

    @Test
    fun getCachedMovies_withId_returnsCorrectMovie() = runTest {
        cacheDao.cacheMovies(listOf(MOVIE_1))
        val result = cacheDao.getCachedMovies(1L)
        assertThat(result[0].title).isEqualTo("Avengers: Endgame")
    }

    @Test
    fun deleteExpiredMoviesCache_removesExpiredEntries() = runTest {
        cacheDao.cacheMovies(listOf(MOVIE_EXPIRED, MOVIE_VALID))
        cacheDao.deleteExpiredMoviesCache(TIMESTAMP - 3000000)
        val result = cacheDao.getCachedMovies()
        assertThat(result).hasSize(1)
    }

    @Test
    fun insertAndGetCachedSeries_returnsCorrectResults() = runTest {
        cacheDao.cacheSeries(listOf(SERIES_1, SERIES_2))
        val result = cacheDao.getCachedSeries()
        assertThat(result).isEqualTo(listOf(SERIES_1, SERIES_2))
    }

    @Test
    fun getCachedSeries_withQuery_returnsMatchingResults() = runTest {
        cacheDao.cacheSeries(listOf(SERIES_1, SERIES_2))
        val result = cacheDao.getCachedSeries("Breaking")
        assertThat(result[0].name).isEqualTo("Breaking Bad")
    }

    @Test
    fun deleteExpiredSeriesCache_removesExpiredEntries() = runTest {
        cacheDao.cacheSeries(listOf(SERIES_EXPIRED, SERIES_VALID))
        cacheDao.deleteExpiredSeriesCache(TIMESTAMP - 3000000)
        val result = cacheDao.getCachedSeries("valid")
        assertThat(result).hasSize(1)
    }

    @Test
    fun insertAndGetCachedArtist_returnsCorrectResults() = runTest {
        cacheDao.cacheArtist(listOf(ARTIST_1, ARTIST_2))
        val result = cacheDao.getCachedArtist("taylor swift")
        assertThat(result).isEqualTo(listOf(ARTIST_1, ARTIST_2))
    }

    @Test
    fun deleteExpiredArtistCache_removesExpiredEntries() = runTest {
        cacheDao.cacheArtist(listOf(ARTIST_EXPIRED, ARTIST_VALID))
        cacheDao.deleteExpiredArtistCache(TIMESTAMP - 3000000)
        val result = cacheDao.getCachedArtist("valid")
        assertThat(result).hasSize(1)
    }

    @Test
    fun getCachedSeries_withId_returnsCorrectSeries() = runTest {
        cacheDao.cacheSeries(listOf(SERIES_1))
        val result = cacheDao.getCachedSeries(1L)
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Stranger Things")
    }

    @Test
    fun getCachedArtist_withId_returnsCorrectArtist() = runTest {
        cacheDao.cacheArtist(listOf(ARTIST_1))
        val result = cacheDao.getCachedArtist(1L)
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Taylor Swift")
    }

    @Test
    fun getCachedArtist_returnsAllCachedArtists() = runTest {
        cacheDao.cacheArtist(listOf(ARTIST_1, ARTIST_2))
        val result = cacheDao.getCachedArtist()
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(ARTIST_1, ARTIST_2)
    }

    @Test
    fun getCachedSeries_returnsAllCachedSeries() = runTest {
        cacheDao.cacheSeries(listOf(SERIES_1, SERIES_2))
        val result = cacheDao.getCachedSeries()
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(SERIES_1, SERIES_2)
    }

    @Test
    fun getCachedMovies_returnsAllCachedMovies() = runTest {
        cacheDao.cacheMovies(listOf(MOVIE_1, MOVIE_2))
        val result = cacheDao.getCachedMovies()
        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(MOVIE_1, MOVIE_2)
    }


    companion object {
        val TIMESTAMP = System.currentTimeMillis()
        val TIMESTAMP_OLD = TIMESTAMP - 3600000

        val MOVIE_1 = MovieCacheDto(1, "Avengers: Endgame", "/path1.jpg", 8.4, TIMESTAMP)
        val MOVIE_2 = MovieCacheDto(2, "Avengers: Infinity War", "/path2.jpg", 8.3, TIMESTAMP)
        val MOVIE_EXPIRED = MovieCacheDto(3, "Expired Movie", "/expired.jpg", 6.0, TIMESTAMP_OLD)
        val MOVIE_VALID = MovieCacheDto(4, "Valid Movie", "/valid.jpg", 7.5, TIMESTAMP)

        val SERIES_1 = SeriesCacheDto(1, "Stranger Things", "/path1.jpg", 8.7, TIMESTAMP)
        val SERIES_2 = SeriesCacheDto(2, "Breaking Bad", "/path2.jpg", 9.5, TIMESTAMP)
        val SERIES_EXPIRED = SeriesCacheDto(3, "Expired Series", "expired", 5.0, TIMESTAMP_OLD)
        val SERIES_VALID = SeriesCacheDto(4, "Valid Series", "valid", 5.0, TIMESTAMP)

        val ARTIST_1 = ArtistCacheDto(1, "Taylor Swift", "taylor swift", TIMESTAMP)
        val ARTIST_2 = ArtistCacheDto(2, "Taylor Swift Official", "taylor swift", TIMESTAMP)
        val ARTIST_EXPIRED = ArtistCacheDto(3, "Expired Artist", "expired", TIMESTAMP_OLD)
        val ARTIST_VALID = ArtistCacheDto(4, "Valid Artist", "valid", TIMESTAMP)
    }
}

