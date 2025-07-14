package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.Dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.Dto.SeriesCacheDto
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

        val cache = series.toSeriesCacheDto(query)

        assertThat(cache).isEqualTo(
            SeriesCacheDto(
                id = series.id.toInt(),
                name = series.title,
                posterPath = series.posterPath,
                voteAverage = series.rating.toDouble(),
                query = query,
                timestamp = cache.timestamp
            )
        )

        val delta = (Instant.now().toEpochMilli() - cache.timestamp).absoluteValue
        assertThat(cache.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `Should SeriesCacheDto maps to Series correctly`() {
        val expected = Series(id = 7, title = "", posterPath = "", rating = 0f)
        assertThat(seriesCacheWithNulls.toSeries()).isEqualTo(expected)
    }

    @Test
    fun `Should Movie maps to MovieCacheDto correctly`() {
        val query = "dream"
        val before = Instant.now().toEpochMilli()

        val cache = movie.toMovieCacheDto(query)

        assertThat(cache.id).isEqualTo(movie.id.toInt())
        assertThat(cache.title).isEqualTo(movie.title)
        assertThat(cache.posterPath).isEqualTo(movie.posterPath)
        assertThat(cache.voteAverage).isWithin(0.001).of(movie.rating.toDouble())
        assertThat(cache.query).isEqualTo(query)

        val delta = (Instant.now().toEpochMilli() - cache.timestamp).absoluteValue
        assertThat(cache.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(150)
    }

    @Test
    fun `Should MovieCacheDto maps to Movie correctly`() {
        val expected = Movie(id = 0L, title = "", posterPath = "", rating = 0f)
        assertThat(movieCacheWithNulls.toMovie()).isEqualTo(expected)
    }

    @Test
    fun `Should Artist maps to ArtistCacheDto correctly`() {
        val query = "emma"
        val before = Instant.now().toEpochMilli()

        val cache = artist.toArtistCacheDto(query)

        assertThat(cache).isEqualTo(
            ArtistCacheDto(
                id = artist.id.toInt(),
                name = artist.name,
                photoPath = artist.photoPath,
                query = query,
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
        assertThat(artistCacheWithNulls.toArtist()).isEqualTo(expected)
    }

    @Test
    fun `Should Movie with exact float maps correctly to MovieCacheDto`() {
        val result = Movie(id = 1L, title = "Test", posterPath = " ", rating = 9.0f)
            .toMovieCacheDto("test")

        assertThat(result.voteAverage).isWithin(0.001).of(9.0)
    }

    @Test
    fun `Should Movie with max float rating maps correctly`() {
        val result = Movie(id = 1000L, title = "Max Float", posterPath = "/max.jpg", rating = Float.MAX_VALUE)
            .toMovieCacheDto("max")

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
            rating = 9.5f
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
            query = "",
            timestamp = 0L
        )

        val seriesCacheWithNulls = SeriesCacheDto(
            id = 7,
            name = null,
            posterPath = null,
            voteAverage = null,
            query = "any",
            timestamp = 1L
        )

        val artistCacheWithNulls = ArtistCacheDto(
            id = 5,
            name = null,
            photoPath = null,
            query = "test",
            timestamp = 123L
        )
    }
}