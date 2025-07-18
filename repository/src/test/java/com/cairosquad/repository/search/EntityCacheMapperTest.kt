package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import kotlin.math.absoluteValue

class EntityCacheMapperTest {

    private val tolerance = 180L

    @Test
    fun `Should Series maps to SeriesCacheDto correctly`() {
        val query = "crime series"
        val before = Instant.now().toEpochMilli()

        val cache = series.toCacheDto()

        assertThat(cache).isEqualTo(
            SeriesCacheDto(
                id = series.id.toInt(),
                name = series.title,
                posterPath = series.posterPath,
                voteAverage = series.rating.toDouble(),
                timestamp = cache.timestamp
            )
        )

        val delta = (Instant.now().toEpochMilli() - cache.timestamp).absoluteValue
        assertThat(cache.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `Should SeriesCacheDto maps to Series correctly`() {
        val expected = Series(
            id = 7, title = "", posterPath = "", rating = 0f,
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 0
        )
        assertThat(seriesCacheWithNulls.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should Movie maps to MovieCacheDto correctly`() {
        val before = Instant.now().toEpochMilli()

        val cache = movie.toCacheDto()

        assertThat(cache.id).isEqualTo(movie.id.toInt())
        assertThat(cache.title).isEqualTo(movie.title)
        assertThat(cache.posterPath).isEqualTo(movie.posterPath)
        assertThat(cache.voteAverage).isWithin(0.001).of(movie.rating.toDouble())

        val delta = (Instant.now().toEpochMilli() - cache.timestamp).absoluteValue
        assertThat(cache.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(150)
    }

    @Test
    fun `Should MovieCacheDto maps to Movie correctly`() {
        val expected = Movie(id = 0L, title = "", posterPath = "", rating = 0f)
        assertThat(movieCacheWithNulls.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should Artist maps to ArtistCacheDto correctly`() {
        val before = Instant.now().toEpochMilli()

        val cache = artist.toCacheDto()

        assertThat(cache).isEqualTo(
            ArtistCacheDto(
                id = artist.id.toInt(),
                name = artist.name,
                photoPath = artist.photoPath,
                timestamp = cache.timestamp
            )
        )

        val delta = (Instant.now().toEpochMilli() - cache.timestamp).absoluteValue
        assertThat(cache.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `Should ArtistCacheDto maps to Artist correctly`() {
        val expected = Artist(id = 5L, name = "", photoPath = "")
        assertThat(artistCacheWithNulls.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should Movie with exact float maps correctly to MovieCacheDto`() {
        val result = Movie(id = 1L, title = "Test", posterPath = " ", rating = 9.0f)
            .toCacheDto()

        assertThat(result.voteAverage).isWithin(0.001).of(9.0)
    }

    @Test
    fun `Should Movie with max float rating maps correctly`() {
        val result = Movie(id = 1000L, title = "Max Float", posterPath = "/max.jpg", rating = Float.MAX_VALUE)
            .toCacheDto()

        assertThat(result.voteAverage).isWithin(0.001).of(Float.MAX_VALUE.toDouble())
    }

    private companion object {
        val movie = Movie(
            id = 40L,
            title = "Inception",
            posterPath = "/inc.jpg",
            rating = 8.8f
        )

        val series = Series(
            id = 100L,
            title = "Breaking Bad",
            posterPath = "/bb.jpg",
            rating = 9.5f,
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 1
        )

        val artist = Artist(
            id = 3L,
            name = "Emma Watson",
            photoPath = "/emma.jpg"
        )

        val movieCacheWithNulls = MovieCacheDto(
            id = 0,
            title = null,
            voteAverage = null,
            posterPath = null,
            timestamp = 0L
        )

        val seriesCacheWithNulls = SeriesCacheDto(
            id = 7,
            name = null,
            posterPath = null,
            voteAverage = null,
            timestamp = 1L
        )

        val artistCacheWithNulls = ArtistCacheDto(
            id = 5,
            name = null,
            photoPath = null,
            timestamp = 123L
        )
    }
}