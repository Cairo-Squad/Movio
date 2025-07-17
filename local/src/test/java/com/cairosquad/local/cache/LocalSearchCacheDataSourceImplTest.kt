package com.cairosquad.local.cache

import com.cairosquad.local.search.cache.cacheDataSourceImpl
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

class LocalSearchCacheDataSourceImplTest {

    private lateinit var cacheDao: CacheDao
    private lateinit var dataSource: cacheDataSourceImpl

    @Before
    fun setup() {
        cacheDao = mockk(relaxed = true)
        dataSource = spyk(cacheDataSourceImpl(cacheDao))
    }

    @Test
    fun `should return mapped movie list when getCachedMovies is called`() = runTest {
        // Given
        coEvery { cacheDao.getCachedMovies(query1) } returns listOf(entity1)
        //When
        val result = dataSource.getCachedMovies(query1)
        //Then
        assertThat(result.first().title).isEqualTo("title")
    }

    @Test
    fun `should insert mapped movie entities into DAO when cacheMovies is called`() = runTest {
        //When
        coEvery { cacheDao.cacheMovies(any()) } just Runs
        //Then
        dataSource.cacheMovies(listOf(dto2))
        coVerify { cacheDao.cacheMovies(match { it.first().title == "poster" }) }
    }

    @Test
    fun `should return mapped series list when getCachedSeries is called`() = runTest {
        // Given
        coEvery { cacheDao.getCachedSeries(query3) } returns listOf(entity3)
        //When
        val result = dataSource.getCachedSeries(query3)
        //Then
        assertThat(result.first().name).isEqualTo("Friends")
    }

    @Test
    fun `should insert mapped series entities into DAO when cacheSeries is called`() = runTest {
        //When
        coEvery { cacheDao.cacheSeries(any()) } just Runs
        dataSource.cacheSeries(listOf(dto4))
        //Then
        coVerify { cacheDao.cacheSeries(match { it.first().name == "Friends" }) }
    }

    @Test
    fun `cacheArtist should map and insert to DAO`() = runTest {
        // Given
        coEvery { cacheDao.cacheArtist(any()) } just Runs
        // When
        dataSource.cacheArtist(listOf(dto5))
        // Then
        coVerify {
            cacheDao.cacheArtist(
                match { list -> list.size == 1 && list.first().name == "Emma" }
            )
        }
    }

    @Test
    fun `should return mapped artist list when getCachedArtist is called`() = runTest {
        // Given
        val query = "emma"
        val entity = ArtistCacheDto(
            id = 0,
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

    private companion object {
        val query1 = "batman"
        val entity1 = MovieCacheDto(
            id = 0,
            timestamp = 0,
            title = "title",
            posterPath = "poster",
            voteAverage = 8.5
        )

        val dto2 = MovieCacheDto(
            id = 1,
            title = "poster",
            voteAverage = 7.5,
            posterPath = null,
            timestamp = Instant.now().toEpochMilli()
        )

        val query3 = "friends"
        val entity3 = SeriesCacheDto(
            id = 0,
            timestamp = 0,
            posterPath = "poster",
            name = "Friends",
            voteAverage = 9.0
        )

        val dto4 = SeriesCacheDto(
            id = 1,
            "Friends", posterPath = null,
            timestamp = Instant.now().toEpochMilli(),
            voteAverage = 9.0
        )

        val dto5 = ArtistCacheDto(
            id = 3,
            name = "Emma",
            photoPath = null,
            timestamp = Instant.now().toEpochMilli()
        )
    }
}

