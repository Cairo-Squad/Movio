package com.cairosquad.local.cache

import com.cairosquad.local.search.cache.LocalSearchCacheDataSourceImpl
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
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
        val entity = MovieCacheDto(
            id = 0,
            query = query,
            timestamp = 0,
            title = "title",
            posterPath = "poster",
            voteAverage = 8.5
        )
        coEvery { cacheDao.getCachedMovies(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedMovies(query)
        //Then
        assertThat(result.first().title).isEqualTo("title")
    }

    @Test
    fun `cacheMovies should map and insert to DAO`() = runTest {
        //Given
        val query = "batman"
        val dto = MovieCacheDto(
            id = 1,
            title = "poster",
            voteAverage = 7.5,
            posterPath = null,
            query = "batman",
            timestamp = Instant.now().toEpochMilli()
        )
        //When
        coEvery { cacheDao.cacheMovies(any()) } just Runs
        //Then
        dataSource.cacheMovies(listOf(dto))
        coVerify { cacheDao.cacheMovies(match { it.first().title == "poster" }) }
    }

    @Test
    fun `getCachedSeries should return mapped results`() = runTest {
        //Given
        val query = "friends"
        val entity = SeriesCacheDto(
            id = 0,
            query = query,
            timestamp = 0,
            posterPath = "poster",
            name = "Friends",
            voteAverage = 9.0
        )
        coEvery { cacheDao.getCachedSeries(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedSeries(query)
        //Then
        assertThat(result.first().name).isEqualTo("Friends")
    }

    @Test
    fun `cacheSeries should map and insert to DAO`() = runTest {
        //Given
        val query = "friends"
        val dto = SeriesCacheDto(
            id = 1,
            "Friends", posterPath = null,
            query = "batman",
            timestamp = Instant.now().toEpochMilli(),
            voteAverage = 9.0
        )
        //When
        coEvery { cacheDao.cacheSeries(any()) } just Runs
        dataSource.cacheSeries(listOf(dto))
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
        dataSource.cacheArtist(listOf(dto))
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
        val entity = ArtistCacheDto(
            id = 0,
            query = query,
            timestamp = 0,
            name = "Emma",
            photoPath = "photo.jpg"
        )
        coEvery { cacheDao.getCachedArtist(query) } returns listOf(entity)
        //When
        val result = dataSource.getCachedArtists(query)
        //Then
        assertThat(result.first().name).isEqualTo("Emma")
    }
}

