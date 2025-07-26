package com.cairosquad.repository.search

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
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
        val page = 1
        val before = Instant.now().toEpochMilli()

        val cache = series.toCacheDto(query, page)

        assertThat(cache).isEqualTo(
            SeriesCacheDto(
                id = series.id.toInt(),
                page = page,
                query = query,
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
            seasonsCount = 1
        )
        assertThat(seriesCacheWithNulls.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should Movie maps to MovieCacheDto correctly`() {
        val before = Instant.now().toEpochMilli()
        val query = "crime series"
        val page = 1
        val cache = movie.toCacheDto(query, page)

        assertThat(cache.id).isEqualTo(movie.id.toInt())
        assertThat(cache.title).isEqualTo(movie.title)
        assertThat(cache.posterPath).isEqualTo(movie.posterPath)
        assertThat(cache.voteAverage).isWithin(0.001).of(movie.rating.toDouble())

        assertThat(cache.timestamp).isAtLeast(before)
    }

    @Test
    fun `Should MovieCacheDto maps to Movie correctly`() {
        val expected = Movie(
            id = 0L, title = "", posterPath = "", rating = 0f,
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )
        assertThat(movieCacheWithNulls.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should Artist maps to ArtistCacheDto correctly`() {
        val before = Instant.now().toEpochMilli()
        val query = "crime series"
        val page = 1
        val tolerance = 200L

        val cache = artist.toCacheDto(query, page)

        assertThat(cache.id).isEqualTo(artist.id.toInt())
        assertThat(cache.name).isEqualTo(artist.name)
        assertThat(cache.query).isEqualTo(query)
        assertThat(cache.page).isEqualTo(page)
        assertThat(cache.photoPath).isEqualTo(artist.photoPath)

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
        val result = Movie(
            id = 1L, title = "Test", posterPath = " ", rating = 9.0f,
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )
            .toCacheDto(query, page)

        assertThat(result.voteAverage).isWithin(0.001).of(9.0)
    }

    @Test
    fun `Should Movie with max float rating maps correctly`() {
        val result = Movie(
            id = 1000L, title = "Max Float", posterPath = "/max.jpg", rating = Float.MAX_VALUE,
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )
            .toCacheDto(query, page)

        assertThat(result.voteAverage).isWithin(0.001).of(Float.MAX_VALUE.toDouble())
    }

    private companion object {
        val query = "crime series"
        val page = 1
        val movie = Movie(
            id = 40L,
            title = "Inception",
            posterPath = "/inc.jpg",
            rating = 8.8f,
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
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
            query = query,
            page = page,
            voteAverage = null,
            posterPath = null,
            timestamp = 0L
        )

        val seriesCacheWithNulls = SeriesCacheDto(
            id = 7,
            name = null,
            query = query,
            page = page,
            posterPath = null,
            voteAverage = null,
            timestamp = 1L
        )

        val artistCacheWithNulls = ArtistCacheDto(
            id = 5,
            page = page,
            query = query,
            name = null,
            photoPath = null,
            timestamp = 123L,
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
    }
}