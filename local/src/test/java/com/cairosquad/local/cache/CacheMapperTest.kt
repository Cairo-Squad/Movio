package com.cairosquad.local.search.cache.dao

import com.cairosquad.local.search.cache.entity.*
import com.cairosquad.repository.search.data_source.local.Dto.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CacheMapperTest {
    @Test
    fun `Should MovieCacheDto correctly map to MovieCacheEntity`() {
        val input = MovieCacheDto(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            voteAverage = MOVIE_RATING,
            posterPath = MOVIE_POSTER,
            query = MOVIE_QUERY,
            timestamp = MOVIE_TIMESTAMP
        )

        val expected = MovieCacheEntity(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            posterPath = MOVIE_POSTER,
            voteAverage = MOVIE_RATING,
            query = MOVIE_QUERY,
            timestamp = MOVIE_TIMESTAMP
        )

        assertThat(input.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should MovieCacheEntity correctly map to MovieCacheDto`() {
        val input = MovieCacheEntity(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            posterPath = MOVIE_POSTER,
            voteAverage = MOVIE_RATING,
            query = MOVIE_QUERY,
            timestamp = MOVIE_TIMESTAMP
        )

        val expected = MovieCacheDto(
            id = MOVIE_ID,
            title = MOVIE_TITLE,
            voteAverage = MOVIE_RATING,
            posterPath = MOVIE_POSTER,
            query = MOVIE_QUERY,
            timestamp = MOVIE_TIMESTAMP
        )

        assertThat(input.toDto()).isEqualTo(expected)
    }
    @Test
    fun `Should SeriesCacheDto map correctly to SeriesCacheEntity`() {
        val input = SeriesCacheDto(
            id = SERIES_ID,
            name = SERIES_NAME,
            posterPath = SERIES_POSTER,
            voteAverage = SERIES_RATING,
            query = SERIES_QUERY,
            timestamp = SERIES_TIMESTAMP
        )

        val expected = SeriesCacheEntity(
            id = SERIES_ID,
            name = SERIES_NAME,
            posterPath = SERIES_POSTER,
            voteAverage = SERIES_RATING,
            query = SERIES_QUERY,
            timestamp = SERIES_TIMESTAMP
        )

        assertThat(input.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should SeriesCacheEntity map correctly to SeriesCacheDto`() {
        val input = SeriesCacheEntity(
            id = SERIES_ID,
            name = SERIES_NAME,
            posterPath = SERIES_POSTER,
            voteAverage = SERIES_RATING,
            query = SERIES_QUERY,
            timestamp = SERIES_TIMESTAMP
        )

        val expected = SeriesCacheDto(
            id = SERIES_ID,
            name = SERIES_NAME,
            posterPath = SERIES_POSTER,
            voteAverage = SERIES_RATING,
            query = SERIES_QUERY,
            timestamp = SERIES_TIMESTAMP
        )

        assertThat(input.toDto()).isEqualTo(expected)
    }
    @Test
    fun `Should ArtistCacheDto map correctly to ArtistCacheEntity`() {
        val input = ArtistCacheDto(
            id = ARTIST_ID,
            name = ARTIST_NAME,
            photoPath = ARTIST_PHOTO,
            query = ARTIST_QUERY,
            timestamp = ARTIST_TIMESTAMP
        )

        val expected = ArtistCacheEntity(
            id = ARTIST_ID,
            name = ARTIST_NAME,
            photoPath = ARTIST_PHOTO,
            query = ARTIST_QUERY,
            timestamp = ARTIST_TIMESTAMP
        )

        assertThat(input.toEntity()).isEqualTo(expected)
    }

    @Test
    fun `Should ArtistCacheEntity map correctly to ArtistCacheDto`() {
        val input = ArtistCacheEntity(
            id = ARTIST_ID,
            name = ARTIST_NAME,
            photoPath = ARTIST_PHOTO,
            query = ARTIST_QUERY,
            timestamp = ARTIST_TIMESTAMP
        )

        val expected = ArtistCacheDto(
            id = ARTIST_ID,
            name = ARTIST_NAME,
            photoPath = ARTIST_PHOTO,
            query = ARTIST_QUERY,
            timestamp = ARTIST_TIMESTAMP
        )

        assertThat(input.toDto()).isEqualTo(expected)
    }
    companion object {
        private const val MOVIE_ID = 1
        private const val MOVIE_TITLE = "Inception"
        private const val MOVIE_POSTER = "/inception.jpg"
        private const val MOVIE_RATING = 8.8
        private const val MOVIE_QUERY = "sci-fi"
        private const val MOVIE_TIMESTAMP = 123_456_789L
        private const val SERIES_ID = 2
        private const val SERIES_NAME = "Breaking Bad"
        private const val SERIES_POSTER = "/breakingbad.jpg"
        private const val SERIES_RATING = 9.5
        private const val SERIES_QUERY = "drama"
        private const val SERIES_TIMESTAMP = 987_654_321L
        private const val ARTIST_ID = 3
        private const val ARTIST_NAME = "Emma Watson"
        private const val ARTIST_PHOTO = "/emma.jpg"
        private const val ARTIST_QUERY = "emma"
        private const val ARTIST_TIMESTAMP = 1_122_334_455L
    }
}
