package com.cairosquad.repository.artists

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistsRepositoryImplTest {

    private lateinit var repository: ArtistsRepositoryImpl

    @Before
    fun setup() {
        repository = ArtistsRepositoryImpl()
    }

    @Test
    fun `getArtist should return fakeArtist`() = runTest {
        val artistId = 123L
        val result = repository.getArtist(artistId)

        assertNotNull(result)
        assertEquals(ArtistsRepositoryImpl.Companion.fakeArtist, result)
        assertEquals("Joseph Mawle", result.name)
    }

    @Test
    fun `getMoviesOfArtist should return list of 10 fakeMovie`() = runTest {
        val artistId = 456L
        val result = repository.getMoviesOfArtist(artistId)

        assertNotNull(result)
        assertEquals(10, result.size)
        result.forEach { movie ->
            assertEquals(ArtistsRepositoryImpl.Companion.fakeMovie, movie)
        }
    }

    @Test
    fun `getSeriesOfArtist should return list of 10 fakeSeries`() = runTest {
        val artistId = 789L
        val result = repository.getSeriesOfArtist(artistId)

        assertNotNull(result)
        assertEquals(10, result.size)
        result.forEach { series ->
            assertEquals(ArtistsRepositoryImpl.Companion.fakeSeries, series)
        }
    }
}