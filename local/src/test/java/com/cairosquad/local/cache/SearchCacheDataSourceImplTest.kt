package com.cairosquad.local.cache

import com.cairosquad.local.search.cache.SearchCacheDataSourceImpl
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.cache.entity.ArtistCacheEntity
import com.cairosquad.local.search.cache.entity.MovieCacheEntity
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.Instant

class SearchCacheDataSourceImplTest {

    private lateinit var cacheDao: CacheDao
    private lateinit var dataSource: SearchCacheDataSourceImpl

    @Before
    fun setup() {
        cacheDao = mockk(relaxed = true)
        dataSource = spyk(SearchCacheDataSourceImpl(cacheDao))
    }

    @Test
    fun `getCachedMovies should return mapped results`() = runTest {
        val query = "batman"
        val entity = MovieCacheEntity(0, query, 0, "title", "poster", 8.5)
        coEvery { cacheDao.getCachedMovies(query) } returns listOf(entity)

        val result = dataSource.getCachedMovies(query)

        coVerify { cacheDao.deleteExpiredMoviesCache(any()) }
        assertThat(result.first().title).isEqualTo("title")
    }

    @Test
    fun `cacheMovies should map and insert to DAO`() = runTest {
        val query = "batman"
        val dto = MovieCacheDto(
            1, "poster", 7.5, posterPath = null,
            query = "batman",
            timestamp = Instant.now().toEpochMilli()
        )

        coEvery { cacheDao.cacheMovies(any()) } just Runs

        dataSource.cacheMovies(query, listOf(dto))

        coVerify { cacheDao.cacheMovies(match { it.first().title == "poster" }) }
    }

    @Test
    fun `getCachedSeries should return mapped results`() = runTest {
        val query = "friends"
        val entity = SeriesCacheEntity(0, query, 0, "poster", "Friends", 9.0)
        coEvery { cacheDao.getCachedSeries(query) } returns listOf(entity)

        val result = dataSource.getCachedSeries(query)

        coVerify { cacheDao.deleteExpiredSeriesCache(any()) }
        assertThat(result.first().name).isEqualTo("Friends")
    }

    @Test
    fun `cacheSeries should map and insert to DAO`() = runTest {
        val query = "friends"
        val dto = SeriesCacheDto(1, "Friends", posterPath = null,
            query = "batman",
            timestamp = Instant.now().toEpochMilli())

        coEvery { cacheDao.cacheSeries(any()) } just Runs

        dataSource.cacheSeries(query, listOf(dto))

        coVerify { cacheDao.cacheSeries(match { it.first().name == "Friends" }) }
    }

    @Test
    fun `getCachedArtist should return mapped results`() = runTest {
        val query = "emma"
        val entity = ArtistCacheEntity(0, query, 0, "Emma", "photo.jpg")
        coEvery { cacheDao.getCachedArtist(query) } returns listOf(entity)

        val result = dataSource.getCachedArtist(query)

        coVerify { cacheDao.deleteExpiredArtistCache(any()) }
        assertThat(result.first().name).isEqualTo("Emma")
    }
}

