package com.cairosquad.local.cache

import com.cairosquad.local.search.cache.SearchCacheDataSourceImpl
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.repository.search.data_source.local.dto.CachedArtistDto
import com.cairosquad.repository.search.data_source.local.dto.CachedMovieDto
import com.cairosquad.repository.search.data_source.local.dto.CachedSeriesDto
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
        //Given
        val query = "batman"
        val entity = MovieCacheEntity(0, query, 0, "title", "poster", 8.5)
        coEvery { cacheDao.getCachedMovies(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedMovies(query)
        //Then
        coVerify { cacheDao.deleteExpiredMoviesCache(any()) }
        assertThat(result.first().title).isEqualTo("title")
    }

    @Test
    fun `cacheMovies should map and insert to DAO`() = runTest {
        //Given
        val query = "batman"
        val dto = CachedMovieDto(
            1, "poster", 7.5, posterPath = null,
            query = "batman",
            timestamp = Instant.now().toEpochMilli()
        )
        //When
        coEvery { cacheDao.cacheMovies(any()) } just Runs
        //Then
        dataSource.cacheMovies(query, listOf(dto))
        coVerify { cacheDao.cacheMovies(match { it.first().title == "poster" }) }
    }

    @Test
    fun `getCachedSeries should return mapped results`() = runTest {
        //Given
        val query = "friends"
        val entity = SeriesCacheEntity(0, query, 0, "poster", "Friends", 9.0)
        coEvery { cacheDao.getCachedSeries(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedSeries(query)
        //Then
        coVerify { cacheDao.deleteExpiredSeriesCache(any()) }
        assertThat(result.first().name).isEqualTo("Friends")
    }

    @Test
    fun `cacheSeries should map and insert to DAO`() = runTest {
        //Given
        val query = "friends"
        val dto = CachedSeriesDto(
            1, "Friends", posterPath = null,
            query = "batman",
            timestamp = Instant.now().toEpochMilli()
        )
        //When
        coEvery { cacheDao.cacheSeries(any()) } just Runs
        dataSource.cacheSeries(query, listOf(dto))
        //Then
        coVerify { cacheDao.cacheSeries(match { it.first().name == "Friends" }) }
    }

    @Test
    fun `cacheArtist should map and insert to DAO`() = runTest {
        // Given
        val query = "emma"
        val dto = CachedArtistDto(
            id = 3,
            name = "Emma",
            photoPath = null,
            query = query,
            timestamp = Instant.now().toEpochMilli()
        )
        coEvery { cacheDao.cacheArtist(any()) } just Runs
        // When
        dataSource.cacheArtist(query, listOf(dto))
        // Then
        coVerify {
            cacheDao.cacheArtist(
                match { list -> list.size == 1 && list.first().name == "Emma" }
            )
        }
    }

    @Test
    fun `getCachedArtist should return mapped results`() = runTest {
        //Given
        val query = "emma"
        val entity = CachedArtistDto(0, query, 0, "Emma", "photo.jpg")
        coEvery { cacheDao.getCachedArtist(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedArtists(query)
        //Then
        coVerify { cacheDao.deleteExpiredArtistCache(any()) }
        assertThat(result.first().name).isEqualTo("Emma")
    }
}

