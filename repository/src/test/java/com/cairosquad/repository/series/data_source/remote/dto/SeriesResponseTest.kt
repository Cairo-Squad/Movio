package com.cairosquad.repository.series.data_source.remote.dto

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test

class SeriesResponseTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `equality check between identical DTOs`() {
        val dto1 = SeriesResponse(
            name = "My Series",
            numberOfEpisodes = 10,
            numberOfSeasons = 2,
            seasons = listOf(
                SeasonRemoteDto(id = 1, name = "Season 1"),
                SeasonRemoteDto(id = 2, name = "Season 2")
            )
        )

        val dto2 = SeriesResponse(
            name = "My Series",
            numberOfEpisodes = 10,
            numberOfSeasons = 2,
            seasons = listOf(
                SeasonRemoteDto(id = 1, name = "Season 1"),
                SeasonRemoteDto(id = 2, name = "Season 2")
            )
        )

        assertThat(dto1).isEqualTo(dto2)
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode())
    }

    @Test
    fun `handles null and empty seasons list`() {
        val dtoWithNullSeasons = SeriesResponse(
            name = "No Seasons Series",
            numberOfEpisodes = 5,
            numberOfSeasons = 0,
            seasons = null
        )

        assertThat(dtoWithNullSeasons.seasons).isNull()

        val dtoWithEmptySeasons = SeriesResponse(
            name = "Empty Seasons Series",
            numberOfEpisodes = 5,
            numberOfSeasons = 0,
            seasons = emptyList()
        )

        assertThat(dtoWithEmptySeasons.seasons).isEmpty()
    }

    @Test
    fun `JSON serialization serializes all fields`() {
        val dto = SeriesResponse(
            name = "Serialized Series",
            numberOfEpisodes = 20,
            numberOfSeasons = 3,
            seasons = listOf(
                SeasonRemoteDto(id = 10, name = "Season A", seasonNumber = 1),
                SeasonRemoteDto(id = 20, name = "Season B", seasonNumber = 2)
            )
        )

        val jsonString = json.encodeToString(dto)

        assertThat(jsonString).contains("\"name\":\"Serialized Series\"")
        assertThat(jsonString).contains("\"number_of_episodes\":20")
        assertThat(jsonString).contains("\"number_of_seasons\":3")
        assertThat(jsonString).contains("\"name\":\"Season A\"")
        assertThat(jsonString).contains("\"name\":\"Season B\"")
    }

    @Test
    fun `JSON deserialization parses all fields`() {
        val jsonString = """
            {
              "name": "Deserialized Series",
              "number_of_episodes": 12,
              "number_of_seasons": 1,
              "seasons": [
                { "id": 100, "name": "Season X", "season_number": 1 },
                { "id": 200, "name": "Season Y", "season_number": 2 }
              ]
            }
        """.trimIndent()

        val dto = json.decodeFromString<SeriesResponse>(jsonString)

        assertThat(dto.name).isEqualTo("Deserialized Series")
        assertThat(dto.numberOfEpisodes).isEqualTo(12)
        assertThat(dto.numberOfSeasons).isEqualTo(1)
        assertThat(dto.seasons).hasSize(2)
        assertThat(dto.seasons?.get(0)?.name).isEqualTo("Season X")
        assertThat(dto.seasons?.get(1)?.name).isEqualTo("Season Y")
    }

    @Test
    fun `deserialization handles null fields gracefully`() {
        val jsonString = """
            {
              "name": null,
              "number_of_episodes": null,
              "number_of_seasons": null,
              "seasons": null
            }
        """.trimIndent()

        val dto = json.decodeFromString<SeriesResponse>(jsonString)

        assertThat(dto.name).isNull()
        assertThat(dto.numberOfEpisodes).isNull()
        assertThat(dto.numberOfSeasons).isNull()
        assertThat(dto.seasons).isNull()
    }
}