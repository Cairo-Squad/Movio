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
        val seriesEntity = Series(
            id = SERIES_ID,
            title = SERIES_TITLE,
            posterPath = SERIES_POSTER,
            rating = SERIES_RATING
        )
        val query = SERIES_QUERY
        val before = Instant.now().toEpochMilli()

        val seriesDto = seriesEntity.toSeriesCacheDto(query)

        assertThat(seriesDto).isEqualTo(
            SeriesCacheDto(
                id = SERIES_ID.toInt(),
                name = SERIES_TITLE,
                posterPath = SERIES_POSTER,
                voteAverage = SERIES_RATING.toDouble(),
                query = query,
                timestamp = seriesDto.timestamp
            )
        )
        val delta = (Instant.now().toEpochMilli() - seriesDto.timestamp).absoluteValue
        assertThat(seriesDto.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `Should SeriesCacheDto maps to Series correctly`() {
        val seriesDto = SeriesCacheDto(
            id = 7,
            name = null,
            posterPath = null,
            voteAverage = null,
            query = "any",
            timestamp = 1L
        )

        val series = seriesDto.toSeries()

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
    fun `Should Movie maps to MovieCacheDto correctly`() {
        val movie = Movie(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            posterPath = MOVIE_POSTER,
            rating = MOVIE_RATING
        )
        val query = MOVIE_QUERY
        val before = Instant.now().toEpochMilli()

        val movieDto = movie.toMovieCacheDto(query)

        assertThat(movieDto.id).isEqualTo(MOVIE_ID.toInt())
        assertThat(movieDto.title).isEqualTo(MOVIE_TITLE)
        assertThat(movieDto.posterPath).isEqualTo(MOVIE_POSTER)
        assertThat(movieDto.voteAverage).isNotNull()
        assertThat(movieDto.voteAverage!!).isWithin(0.001).of(MOVIE_RATING.toDouble())
        assertThat(movieDto.query).isEqualTo(query)

        val now = Instant.now().toEpochMilli()
        val delta = (now - movieDto.timestamp).absoluteValue
        assertThat(movieDto.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(150)
    }

    @Test
    fun `Should MovieCacheDto maps to Movie correctly`() {
        val movieDto = MovieCacheDto(
            id = 0,
            title = null,
            voteAverage = null,
            posterPath = null,
            query = "",
            timestamp = 0L
        )

        val movie = movieDto.toMovie()

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
    fun `Should Artist maps to ArtistCacheDto correctly`() {
        val artist = Artist(
            id = ARTIST_ID,
            name = ARTIST_NAME,
            photoPath = ARTIST_PHOTO
        )
        val query = ARTIST_QUERY
        val before = Instant.now().toEpochMilli()

        val artistDto = artist.toArtistCacheDto(query)

        assertThat(artistDto).isEqualTo(
            ArtistCacheDto(
                id = ARTIST_ID.toInt(),
                name = ARTIST_NAME,
                photoPath = ARTIST_PHOTO,
                query = query,
                timestamp = artistDto.timestamp
            )
        )
        val delta = (Instant.now().toEpochMilli() - artistDto.timestamp).absoluteValue
        assertThat(artistDto.timestamp).isAtLeast(before)
        assertThat(delta).isAtMost(tolerance)
    }

    @Test
    fun `Should ArtistCacheDto maps to Artist correctly`() {
        val artistDto = ArtistCacheDto(
            id = 5,
            name = null,
            photoPath = null,
            query = "test",
            timestamp = 123L
        )

        val artist = artistDto.toArtist()

        assertThat(artist).isEqualTo(
            Artist(
                id = 5L,
                name = "",
                photoPath = ""
            )
        )
    }

    @Test
    fun `Should Movie with exact float maps correctly to MovieCacheDto`() {
        val movie = Movie(
            id = 1L,
            title = "Test",
            posterPath = " ",
            rating = 9.0f
        )
        val movieDto = movie.toMovieCacheDto("test")

        assertThat(movieDto.voteAverage).isWithin(0.001).of(9.0)
    }

    @Test
    fun `Should Movie with max float rating maps correctly`() {
        val movie = Movie(
            id = 1000L,
            title = "Max Float",
            posterPath = "/max.jpg",
            rating = Float.MAX_VALUE
        )
        val movieDto = movie.toMovieCacheDto("max")

        assertThat(movieDto.voteAverage).isWithin(0.001).of(Float.MAX_VALUE.toDouble())
    }

    companion object {
        private const val MOVIE_ID = 40L
        private const val MOVIE_TITLE = "Inception"
        private const val MOVIE_POSTER = "/inc.jpg"
        private const val MOVIE_QUERY = "dream"
        private const val MOVIE_RATING = 8.8f
        private const val SERIES_ID = 100L
        private const val SERIES_TITLE = "Breaking Bad"
        private const val SERIES_POSTER = "/bb.jpg"
        private const val SERIES_QUERY = "crime series"
        private const val SERIES_RATING = 9.5f
        private const val ARTIST_ID = 3L
        private const val ARTIST_NAME = "Emma Watson"
        private const val ARTIST_PHOTO = "/emma.jpg"
        private const val ARTIST_QUERY = "emma"
    }
}
