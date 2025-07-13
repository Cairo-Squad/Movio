package com.cairosquad.local.search.cache.dao

import com.cairosquad.local.search.cache.entity.ArtistCacheEntity
import com.cairosquad.local.search.cache.entity.MovieCacheEntity
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CacheMapperTest {

    /* ---------- Movie ---------- */

    @Test
    fun `Should map MovieCacheDto to MovieCacheEntity correctly`() {
        val input = sampleMovieCacheDto()
        val expected = sampleMovieCacheEntity()
        assertThat(input.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should map MovieCacheEntity to MovieCacheDto correctly`() {
        val input = sampleMovieCacheEntity()
        val expected = sampleMovieCacheDto()
        assertThat(input.toDto()).isEqualTo(expected)
    }

    /* ---------- Series ---------- */

    @Test
    fun `Should map SeriesCacheDto to SeriesCacheEntity correctly`() {
        val input = sampleSeriesCacheDto()
        val expected = sampleSeriesCacheEntity()
        assertThat(input.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should map SeriesCacheEntity to SeriesCacheDto correctly`() {
        val input = sampleSeriesCacheEntity()
        val expected = sampleSeriesCacheDto()
        assertThat(input.toDto()).isEqualTo(expected)
    }

    /* ---------- Artist ---------- */

    @Test
    fun `Should map ArtistCacheDto to ArtistCacheEntity correctly`() {
        val input = sampleArtistCacheDto()
        val expected = sampleArtistCacheEntity()
        assertThat(input.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should map ArtistCacheEntity to ArtistCacheDto correctly`() {
        val input = sampleArtistCacheEntity()
        val expected = sampleArtistCacheDto()
        assertThat(input.toDto()).isEqualTo(expected)
    }

    /* ---------- Private test data helpers ---------- */

    private fun sampleMovieCacheDto() = MovieCacheDto(
        id = 1,
        title = "Inception",
        voteAverage = 8.8,
        posterPath = "/inception.jpg",
        query = "sci-fi",
        timestamp = 123456789L
    )

    private fun sampleMovieCacheEntity() = MovieCacheEntity(
        id = 1,
        title = "Inception",
        posterPath = "/inception.jpg",
        voteAverage = 8.8,
        query = "sci-fi",
        timestamp = 123456789L
    )

    private fun sampleSeriesCacheDto() = SeriesCacheDto(
        id = 2,
        name = "Breaking Bad",
        posterPath = "/breakingbad.jpg",
        voteAverage = 9.5,
        query = "drama",
        timestamp = 987654321L
    )

    private fun sampleSeriesCacheEntity() = SeriesCacheEntity(
        id = 2,
        name = "Breaking Bad",
        posterPath = "/breakingbad.jpg",
        voteAverage = 9.5,
        query = "drama",
        timestamp = 987654321L
    )

    private fun sampleArtistCacheDto() = ArtistCacheDto(
        id = 3,
        name = "Emma Watson",
        photoPath = "/emma.jpg",
        query = "emma",
        timestamp = 1122334455L
    )

    private fun sampleArtistCacheEntity() = ArtistCacheEntity(
        id = 3,
        name = "Emma Watson",
        photoPath = "/emma.jpg",
        query = "emma",
        timestamp = 1122334455L
    )
}
