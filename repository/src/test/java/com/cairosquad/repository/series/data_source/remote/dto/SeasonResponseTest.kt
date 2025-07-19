package com.cairosquad.repository.series.data_source.remote.dto

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test

class SeasonResponseTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `SeasonResponse equality check`() {
        val response1 = SeasonResponse(
            episodes = listOf(
                EpisodeRemoteDto(id = 1L, name = "Ep1", seriesId = 101L),
                EpisodeRemoteDto(id = 2L, name = "Ep2", seriesId = 101L)
            ),
            id = 500L
        )

        val response2 = SeasonResponse(
            episodes = listOf(
                EpisodeRemoteDto(id = 1L, name = "Ep1", seriesId = 101L),
                EpisodeRemoteDto(id = 2L, name = "Ep2", seriesId = 101L)
            ),
            id = 500L
        )

        assertThat(response1).isEqualTo(response2)
    }

    @Test
    fun `SeasonResponse hashCode consistency`() {
        val response1 = SeasonResponse(
            episodes = emptyList(),
            id = 123L
        )
        val response2 = SeasonResponse(
            episodes = emptyList(),
            id = 123L
        )

        assertThat(response1.hashCode()).isEqualTo(response2.hashCode())
    }

    @Test
    fun `SeasonResponse JSON serialization`() {
        val response = SeasonResponse(
            episodes = listOf(
                EpisodeRemoteDto(
                    airDate = "2024-07-01",
                    episodeNumber = 1,
                    name = "Pilot",
                    id = 10L,
                    runtime = 42,
                    seasonNumber = 1,
                    stillPath = "/ep1.jpg",
                    voteAverage = 8.5,
                    seriesId = 101L
                )
            ),
            id = 202L
        )

        val jsonString = json.encodeToString(response)
        assertThat(jsonString).contains("\"id\":202")
        assertThat(jsonString).contains("\"name\":\"Pilot\"")
    }

    @Test
    fun `SeasonResponse JSON deserialization`() {
        val jsonString = """
            {
                "id": 202,
                "episodes": [
                    {
                        "show_id": 10,
                        "name": "Pilot",
                        "series_id": 101,
                        "air_date": "2024-07-01",
                        "episode_number": 1,
                        "runtime": 42,
                        "season_number": 1,
                        "still_path": "/ep1.jpg",
                        "vote_average": 8.5
                    }
                ]
            }
        """.trimIndent()

        val response = json.decodeFromString<SeasonResponse>(jsonString)

        assertThat(response.id).isEqualTo(202L)
        assertThat(response.episodes).hasSize(1)
        val episode = response.episodes!!.first()
        assertThat(episode.name).isEqualTo("Pilot")
        assertThat(episode.runtime).isEqualTo(42)
    }

    @Test
    fun `SeasonResponse handles null episodes list`() {
        val response = SeasonResponse(
            episodes = null,
            id = 404L
        )

        assertThat(response.episodes).isNull()
        assertThat(response.id).isEqualTo(404L)
    }
}