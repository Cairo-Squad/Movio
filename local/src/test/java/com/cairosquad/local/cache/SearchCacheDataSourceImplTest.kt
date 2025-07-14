package com.cairosquad.local.cache

import com.cairosquad.local.search.cache.LocalSearchCacheDataSourceImpl
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.cache.entity.ArtistCacheEntity
import com.cairosquad.local.search.cache.entity.MovieCacheEntity
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
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
    private lateinit var dataSource: LocalSearchCacheDataSourceImpl

    @Before
    fun setup() {
        cacheDao = mockk(relaxed = true)
        dataSource = spyk(LocalSearchCacheDataSourceImpl(cacheDao))
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
        val dto = MovieCacheDto(
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
        val dto = SeriesCacheDto(
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
        val dto = ArtistCacheDto(
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
        val entity = ArtistCacheEntity(0, query, 0, "Emma", "photo.jpg")
        coEvery { cacheDao.getCachedArtist(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedArtist(query)
        //Then
        coVerify { cacheDao.deleteExpiredArtistCache(any()) }
        assertThat(result.first().name).isEqualTo("Emma")
    }
}

