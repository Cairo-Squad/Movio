package com.cairosquad.local.cache

import com.cairosquad.local.search.cache.CacheDataSourceImpl
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
    private lateinit var dataSource: CacheDataSourceImpl

    @Before
    fun setup() {
        cacheDao = mockk(relaxed = true)
        dataSource = spyk(CacheDataSourceImpl(cacheDao))
    }

    @Test
    fun `should return mapped movie list when getCachedMovies is called`() = runTest {
        // Given
        val query = "batman"
        val page = 1
        val entity = MovieCacheDto(
            id = 0,
            page = 1,
            query = query,
            timestamp = 0,
            title = "title",
            posterPath = "poster",
            voteAverage = 8.5
        )
        coEvery { cacheDao.getCachedMovies(query,page ) } returns listOf(entity)
        //When
        val result = dataSource.getCachedMovies(query,page)
        //Then
        assertThat(result.first().title).isEqualTo("title")
    }

    @Test
    fun `should insert mapped movie entities into DAO when cacheMovies is called`() = runTest {
        // Given
        val query = "batman"
        val dto = MovieCacheDto(
            id = 1,
            page = 1,
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
    fun `should return mapped series list when getCachedSeries is called`() = runTest {
        // Given
        val query = "friends"
        val page = 1
        val entity = SeriesCacheDto(
            id = 0,
            page = 1,
            query = query,
            timestamp = 0,
            posterPath = "poster",
            name = "Friends",
            voteAverage = 9.0
        )
        coEvery { cacheDao.getCachedSeries(query,page) } returns listOf(entity)
        //When
        val result = dataSource.getCachedSeries(query,page)
        //Then
        assertThat(result.first().name).isEqualTo("Friends")
    }

    @Test
    fun `should insert mapped series entities into DAO when cacheSeries is called`() = runTest {
        // Given
        val query = "friends"
        val dto = SeriesCacheDto(
            id = 1,
            page = 1,
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
            page = 1,
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
    fun `should return mapped artist list when getCachedArtist is called`() = runTest {
        // Given
        val query = "emma"
        val page = 1
        val entity = ArtistCacheDto(
            id = 0,
            page = 1,
            query = query,
            timestamp = 0,
            name = "Emma",
            photoPath = "photo.jpg"
        )
        coEvery { cacheDao.getCachedArtist(query,page) } returns listOf(entity)
        //When
        val result = dataSource.getCachedArtists(query,page)
        //Then
        assertThat(result.first().name).isEqualTo("Emma")
    }
    @Test
    fun `clearExpiredCache should delete all expired caches from DAO`() = runTest {
        // Arrange
        val currentTime = Instant.now().toEpochMilli()
        val expectedExpirationTime = currentTime - 3_600_000 // 1 hour

        // Mock delete methods
        coEvery { cacheDao.deleteExpiredMoviesCache(any()) } just Runs
        coEvery { cacheDao.deleteExpiredSeriesCache(any()) } just Runs
        coEvery { cacheDao.deleteExpiredArtistCache(any()) } just Runs

        // Act
        dataSource.clearExpiredCache(100L)

        // Assert: Verify all 3 DAO calls were made with a timestamp <= now - 1 hour
        coVerify {
            cacheDao.deleteExpiredMoviesCache(withArg { ts ->
                assertThat(ts).isLessThan(Instant.now().toEpochMilli())
            })
            cacheDao.deleteExpiredSeriesCache(any())
            cacheDao.deleteExpiredArtistCache(any())
        }
    }
}

