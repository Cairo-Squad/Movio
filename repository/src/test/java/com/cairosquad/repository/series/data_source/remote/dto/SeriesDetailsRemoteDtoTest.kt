package com.cairosquad.repository.series.data_source.remote.dto

import com.cairosquad.entity.Genre
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.utils.TimeUtils
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test

class SeriesDetailsRemoteDtoTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        mockkObject(TimeUtils)
    }

    @After
    fun teardown() {
        unmockkObject(TimeUtils)
    }

    @Test
    fun `toEntity maps all non-null fields correctly`() {
        every { TimeUtils.dateToLong("2024-01-15") } returns 1705286400000L

        val dto = SeriesDetailsRemoteDto(
            backdropPath = "/backdrop.jpg",
            firstAirDate = "2024-01-15",
            genres = listOf(GenreDto(1, "Drama"), GenreDto(2, "Action")),
            id = 10L,
            name = "Test Series",
            numberOfEpisodes = 20,
            numberOfSeasons = 2,
            overview = "This is a test series.",
            posterPath = "/poster.jpg",
            type = "Scripted",
            voteAverage = 8.9,
            voteCount = 100
        )

        val entity = dto.toEntity("")

        assertThat(entity.id).isEqualTo(10L)
        assertThat(entity.title).isEqualTo("Test Series")
        assertThat(entity.rating).isEqualTo(4.45f)
        assertThat(entity.posterPath).isEqualTo("/poster.jpg")
        assertThat(entity.genres).containsExactly(Genre(1, "Drama"), Genre(2, "Action"))
        assertThat(entity.overview).isEqualTo("This is a test series.")
        assertThat(entity.releaseDate).isEqualTo(1705286400000L)
        assertThat(entity.seasonsCount).isEqualTo(2)
        assertThat(entity.trailerPath).isEmpty()
    }

    @Test
    fun `toEntity maps null fields to default values`() {
        every { TimeUtils.dateToLong(any()) } returns 0L

        val dto = SeriesDetailsRemoteDto()

        val entity = dto.toEntity("")

        assertThat(entity.id).isEqualTo(0L)
        assertThat(entity.title).isEmpty()
        assertThat(entity.rating).isEqualTo(0f)
        assertThat(entity.posterPath).isEmpty()
        assertThat(entity.genres).isEmpty()
        assertThat(entity.overview).isEmpty()
        assertThat(entity.releaseDate).isEqualTo(0L)
        assertThat(entity.seasonsCount).isEqualTo(1)
        assertThat(entity.trailerPath).isEmpty()
    }

    @Test
    fun `toEntity handles null genres list`() {
        val dto = SeriesDetailsRemoteDto(genres = null, id = 50L, name = "No Genres")

        val entity = dto.toEntity("")

        assertThat(entity.genres).isEmpty()
        assertThat(entity.title).isEqualTo("No Genres")
    }

    @Test
    fun `equality check between two identical DTOs`() {
        val dto1 = SeriesDetailsRemoteDto(id = 1L, name = "Same Series")
        val dto2 = SeriesDetailsRemoteDto(id = 1L, name = "Same Series")

        assertThat(dto1).isEqualTo(dto2)
    }

    @Test
    fun `hashCode consistency for identical DTOs`() {
        val dto1 = SeriesDetailsRemoteDto(id = 1L, name = "Same Series")
        val dto2 = SeriesDetailsRemoteDto(id = 1L, name = "Same Series")

        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode())
    }

    @Test
    fun `JSON serialization and deserialization`() {
        val dto = SeriesDetailsRemoteDto(
            id = 15L,
            name = "Serialized Series",
            voteAverage = 9.5,
            genres = listOf(GenreDto(1, "Comedy"))
        )

        val jsonString = json.encodeToString(dto)
        assertThat(jsonString).contains("\"id\":15")
        assertThat(jsonString).contains("\"name\":\"Serialized Series\"")

        val deserialized = json.decodeFromString<SeriesDetailsRemoteDto>(jsonString)
        assertThat(deserialized.id).isEqualTo(15L)
        assertThat(deserialized.name).isEqualTo("Serialized Series")
        assertThat(deserialized.genres?.first()?.name).isEqualTo("Comedy")
    }
}