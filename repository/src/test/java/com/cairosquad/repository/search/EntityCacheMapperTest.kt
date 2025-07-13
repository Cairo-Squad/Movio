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

class DomainCacheMapperTest {

    private val tolerance = 180L

    @Test
    fun `Series maps to SeriesCacheDto correctly`() {
        val entity = Series(
            id = 100L,
            title = "Breaking Bad",
            posterPath = "/bb.jpg",
            rating = 9.5f
        )
        val query = "crime series"
        val before = Instant.now().toEpochMilli()

        val dto = entity.toSeriesCacheDto(query)

        assertThat(dto).isEqualTo(
            SeriesCacheDto(
                id = 100,
                name = "Breaking Bad",
                posterPath = "/bb.jpg",
                voteAverage = 9.5,
                query = query,
                timestamp = dto.timestamp
            )
        )
        val delta = (Instant.now().toEpochMilli() - dto.timestamp).absoluteValue
        assertThat(dto.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `SeriesCacheDto maps to Series correctly`() {
        val dto = SeriesCacheDto(
            id = 7,
            name = null,
            posterPath = null,
            voteAverage = null,
            query = "any",
            timestamp = 1L
        )

        val series = dto.toSeries()

        assertThat(series).isEqualTo(
            Series(
                id = 7L,
                title = "",
                posterPath = "",
                rating = 0f
            )
        )
    }

    @Test
    fun `Movie maps to MovieCacheDto correctly`() {
        val movie = Movie(
            id = 40L,
            title = "Inception",
            posterPath = "/inc.jpg",
            rating = 8.8f
        )
        val query = "dream"
        val before = Instant.now().toEpochMilli()

        val dto = movie.toMovieCacheDto(query)

        assertThat(dto.id).isEqualTo(40)
        assertThat(dto.title).isEqualTo("Inception")
        assertThat(dto.posterPath).isEqualTo("/inc.jpg")

        assertThat(dto.voteAverage).isNotNull()
        assertThat(dto.voteAverage!!).isWithin(0.001).of(8.8)

        assertThat(dto.query).isEqualTo(query)

        val now = Instant.now().toEpochMilli()
        val delta = (now - dto.timestamp).absoluteValue

        assertThat(dto.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(150)
    }


    @Test
    fun `MovieCacheDto maps to Movie correctly`() {
        val dto = MovieCacheDto(
            id = 0,
            title = null,
            voteAverage = null,
            posterPath = null,
            query = "",
            timestamp = 0L
        )

        val movie = dto.toMovie()

        assertThat(movie).isEqualTo(
            Movie(
                id = 0L,
                title = "",
                posterPath = "",
                rating = 0f
            )
        )
    }

    @Test
    fun `Artist maps to ArtistCacheDto correctly`() {
        val artist = Artist(
            id = 3L,
            name = "Emma Watson",
            photoPath = "/emma.jpg"
        )
        val query = "emma"
        val before = Instant.now().toEpochMilli()

        val dto = artist.toArtistCacheDto(query)

        assertThat(dto).isEqualTo(
            ArtistCacheDto(
                id = 3,
                name = "Emma Watson",
                photoPath = "/emma.jpg",
                query = query,
                timestamp = dto.timestamp
            )
        )
        val delta = (Instant.now().toEpochMilli() - dto.timestamp).absoluteValue
        assertThat(dto.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `ArtistCacheDto maps to Artist correctly`() {
        val dto = ArtistCacheDto(
            id = 5,
            name = null,
            photoPath = null,
            query = "test",
            timestamp = 123L
        )

        val artist = dto.toArtist()

        assertThat(artist).isEqualTo(
            Artist(
                id = 5L,
                name = "",
                photoPath = ""
            )
        )
    }

    @Test
    fun `Movie with exact float maps correctly to MovieCacheDto`() {
        val movie = Movie(
            id = 1L,
            title = "Test",
            posterPath = " ",
            rating = 9.0f
        )
        val dto = movie.toMovieCacheDto("test")

        assertThat(dto.voteAverage).isWithin(0.001).of(9.0)
    }

    @Test
    fun `Movie with max float rating maps correctly`() {
        val movie = Movie(
            id = 1000L,
            title = "Max Float",
            posterPath = "/max.jpg",
            rating = Float.MAX_VALUE
        )
        val dto = movie.toMovieCacheDto("max")

        assertThat(dto.voteAverage).isWithin(0.001).of(Float.MAX_VALUE.toDouble())
    }
}



