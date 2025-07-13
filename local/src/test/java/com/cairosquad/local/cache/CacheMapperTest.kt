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

    @Test
    fun `MovieCacheDto maps correctly to MovieCacheEntity`() {
        val dto = MovieCacheDto(
            id = 1,
            title = "Inception",
            voteAverage = 8.8,
            posterPath = "/inception.jpg",
            query = "sci-fi",
            timestamp = 123456789L
        )

        val expectedEntity = MovieCacheEntity(
            id = 1,
            title = "Inception",
            posterPath = "/inception.jpg",
            voteAverage = 8.8,
            query = "sci-fi",
            timestamp = 123456789L
        )

        assertThat(dto.toEntity()).isEqualTo(expectedEntity)
    }

    @Test
    fun `MovieCacheEntity maps correctly to MovieCacheDto`() {
        val entity = MovieCacheEntity(
            id = 1,
            title = "Inception",
            posterPath = "/inception.jpg",
            voteAverage = 8.8,
            query = "sci-fi",
            timestamp = 123456789L
        )

        val expectedDto = MovieCacheDto(
            id = 1,
            title = "Inception",
            voteAverage = 8.8,
            posterPath = "/inception.jpg",
            query = "sci-fi",
            timestamp = 123456789L
        )

        assertThat(entity.toDto()).isEqualTo(expectedDto)
    }

    @Test
    fun `SeriesCacheDto maps correctly to SeriesCacheEntity`() {
        val dto = SeriesCacheDto(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            query = "drama",
            timestamp = 987654321L
        )

        val expectedEntity = SeriesCacheEntity(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            query = "drama",
            timestamp = 987654321L
        )

        assertThat(dto.toEntity()).isEqualTo(expectedEntity)
    }

    @Test
    fun `SeriesCacheEntity maps correctly to SeriesCacheDto`() {
        val entity = SeriesCacheEntity(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            query = "drama",
            timestamp = 987654321L
        )

        val expectedDto = SeriesCacheDto(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            query = "drama",
            timestamp = 987654321L
        )

        assertThat(entity.toDto()).isEqualTo(expectedDto)
    }

    @Test
    fun `ArtistCacheDto maps correctly to ArtistCacheEntity`() {
        val dto = ArtistCacheDto(
            id = 3,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            query = "emma",
            timestamp = 1122334455L
        )

        val expectedEntity = ArtistCacheEntity(
            id = 3,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            query = "emma",
            timestamp = 1122334455L
        )

        assertThat(dto.toEntity()).isEqualTo(expectedEntity)
    }

    @Test
    fun `ArtistCacheEntity maps correctly to ArtistCacheDto`() {
        val entity = ArtistCacheEntity(
            id = 3,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            query = "emma",
            timestamp = 1122334455L
        )

        val expectedDto = ArtistCacheDto(
            id = 3,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            query = "emma",
            timestamp = 1122334455L
        )

        assertThat(entity.toDto()).isEqualTo(expectedDto)
    }
}
