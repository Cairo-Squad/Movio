package com.cairosquad.viewmodel.details.series

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SeriesDetailsUiStateMapperTest {

    @Test
    fun `Artist toUiState SHOULD map fields correctly`() {

        val artist = Artist(
            id = 1L,
            name = "John Doe",
            photoPath = "/photo.jpg",
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )

        val uiState = artist.toUiState()

        assertThat(uiState.id).isEqualTo(1L)
        assertThat(uiState.name).isEqualTo("John Doe")
        assertThat(uiState.photoPath).isEqualTo("/photo.jpg")
    }

    @Test
    fun `Series toUiState SHOULD map fields and transform rating and releaseDate`() {
        val series = Series(
            id = 99L,
            title = "The Great Show",
            rating = 4.0f,
            posterPath = "/poster.jpg",
            genres = listOf(Genre(1, "Drama"), Genre(2, "Sci-Fi")),
            seasonsCount = 3,
            releaseDate = 0L,
            overview = "A great series",
            trailerPath = ""
        )

        val uiState = series.toUiState()

        assertThat(uiState.id).isEqualTo(99L)
        assertThat(uiState.title).isEqualTo("The Great Show")
        assertThat(uiState.rating).isEqualTo(4.0f)
        assertThat(uiState.posterPath).isEqualTo("/poster.jpg")
        assertThat(uiState.genres).containsExactly("Drama", "Sci-Fi")
        assertThat(uiState.seasonsCount).isEqualTo(3)
        assertThat(uiState.releaseDate).isEqualTo(null)
        assertThat(uiState.overview).isEqualTo("A great series")
        assertThat(uiState.trailerPath).isEmpty()
    }

    @Test
    fun `Season toUiState SHOULD map fields and transform rating and airDate`() {
        val season = Season(
            seasonNumber = 2,
            seasonName = "Season 2",
            episodesCount = 10,
            rating = 4.5f,
            posterPath = "/season.jpg",
            overview = "Best season",
            airDate = TimeUtil.convertLongToYear(0L).let { 0L },
            seriesId = 123L
        )

        val uiState = season.toUiState()

        assertThat(uiState.number).isEqualTo(2)
        assertThat(uiState.name).isEqualTo("Season 2")
        assertThat(uiState.episodesCount).isEqualTo(10)
        assertThat(uiState.rating).isEqualTo(4.5f) // halved
        assertThat(uiState.posterPath).isEqualTo("/season.jpg")
        assertThat(uiState.overview).isEqualTo("Best season")
        assertThat(uiState.airDate).isEqualTo("1970")
    }

    @Test
    fun `Review toUiState SHOULD map fields and transform rating and date`() {
        val review = Review(
            id = "5",
            author = "Jane Smith",
            authorPhotoPath = "/author.jpg",
            rating = 5.0f,
            date = TimeUtil.convertLongToNamedDate(0L).let { 0L }, // epoch
            description = "Amazing series!"
        )

        val uiState = review.toUiState()

        assertThat(uiState.id).isEqualTo("5")
        assertThat(uiState.author).isEqualTo("Jane Smith")
        assertThat(uiState.authorPhotoPath).isEqualTo("/author.jpg")
        assertThat(uiState.rating).isEqualTo(5.0f) // halved
        assertThat(uiState.date).isEqualTo("January 01, 1970")
        assertThat(uiState.description).isEqualTo("Amazing series!")
    }

    @Test
    fun `Series toUiState SHOULD handle empty genres`() {
        val series = Series(
            id = 1L,
            title = "Empty Genres",
            rating = 0f,
            posterPath = "",
            genres = emptyList(),
            seasonsCount = 0,
            releaseDate = getMillis("2025-12-31"),
            overview = "",
            trailerPath = ""
        )
        val uiState = series.toUiState()

        assertThat(uiState.genres).isEmpty()
        assertThat(uiState.rating).isEqualTo(0f)
        assertThat(uiState.posterPath).isEmpty()
        assertThat(uiState.releaseDate).isEqualTo("12/31/2025")
    }

    @Test
    fun `Review toUiState SHOULD handle negative timestamp`() {
        val review = Review(
            id = "10L",
            author = "Time Traveler",
            authorPhotoPath = "",
            rating = 0f,
            date = getMillis("1965-05-15"),
            description = "Before 1970!"
        )
        val uiState = review.toUiState()

        assertThat(uiState.date).isEqualTo("May 15, 1965")
    }

    private fun getMillis(date: String): Long {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .parse(date)?.time ?: throw IllegalArgumentException("Invalid date")
    }
}
