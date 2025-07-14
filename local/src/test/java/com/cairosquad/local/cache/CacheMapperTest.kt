package com.cairosquad.local.search.cache.dao

import com.cairosquad.local.search.cache.entity.*
import com.cairosquad.repository.search.data_source.local.Dto.*
import com.cairosquad.repository.search.data_source.local.dto.CachedArtistDto
import com.cairosquad.repository.search.data_source.local.dto.CachedMovieDto
import com.cairosquad.repository.search.data_source.local.dto.CachedSeriesDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CacheMapperTest {

    @Test
    fun `Should MovieCacheDto correctly map to MovieCacheEntity`() {
        assertThat(movieDto).isEqualTo(movieEntity)
    }

    @Test
    fun `Should MovieCacheEntity correctly map to MovieCacheDto`() {
        assertThat(movieEntity).isEqualTo(movieDto)
    }

    @Test
    fun `Should SeriesCacheDto map correctly to SeriesCacheEntity`() {
        assertThat(seriesDto.toEntity()).isEqualTo(seriesEntity)
    }

    @Test
    fun `Should SeriesCacheEntity map correctly to SeriesCacheDto`() {
        assertThat(seriesEntity.toDto()).isEqualTo(seriesDto)
    }

    @Test
    fun `Should ArtistCacheDto map correctly to ArtistCacheEntity`() {
        assertThat(artistDto).isEqualTo(artistEntity)
    }

    @Test
    fun `Should ArtistCacheEntity map correctly to ArtistCacheDto`() {
        assertThat(artistEntity).isEqualTo(artistDto)
    }

    private companion object {
        val movieDto = CachedMovieDto(
            id = 1,
            title = "Inception",
            voteAverage = 8.8,
            posterPath = "/inception.jpg",
            query = "sci-fi",
            timestamp = 123_456_789L
        )

        val movieEntity = CachedMovieDto(
            id = 1,
            title = "Inception",
            posterPath = "/inception.jpg",
            voteAverage = 8.8,
            query = "sci-fi",
            timestamp = 123_456_789L
        )

        val seriesDto = CachedSeriesDto(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            query = "drama",
            timestamp = 987_654_321L
        )

        val seriesEntity = SeriesCacheEntity(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/breakingbad.jpg",
            voteAverage = 9.5,
            query = "drama",
            timestamp = 987_654_321L
        )

        val artistDto = CachedArtistDto(
            id = 3,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            query = "emma",
            timestamp = 1_122_334_455L
        )

        val artistEntity = CachedArtistDto(
            id = 3,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            query = "emma",
            timestamp = 1_122_334_455L
        )
    }
}
