package com.cairosquad.repository.series.data_source.remote.dto

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EpisodeRemoteDtoTest {

    @Test
    fun `toEntity maps all non-null fields correctly`() {
        val dto = EpisodeRemoteDto(
            airDate = "2023-10-17",
            episodeNumber = 5,
            name = "Episode Name",
            overview = "This is an overview",
            id = 101L,
            runtime = 42,
            seasonNumber = 2,
            stillPath = "/image.jpg",
            voteAverage = 8.7,
            seriesId = 202L
        )
        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo(101L)
        assertThat(entity.episodeNumber).isEqualTo(5)
        assertThat(entity.episodeName).isEqualTo("Episode Name")
        assertThat(entity.runtimeInMinutes).isEqualTo(42)
        assertThat(entity.rating).isEqualTo(4.35f)
        assertThat(entity.seasonNumber).isEqualTo(2)
        assertThat(entity.seriesId).isEqualTo(202L)
        assertThat(entity.photoPath).isEqualTo("/image.jpg")
    }

    @Test
    fun `toEntity maps null fields to default values`() {
        val dto = EpisodeRemoteDto(
            airDate = null,
            episodeNumber = null,
            name = null,
            overview = null,
            id = null,
            runtime = null,
            seasonNumber = null,
            stillPath = null,
            voteAverage = null,
            seriesId = 202L // seriesId is non-null
        )

        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo(0L)
        assertThat(entity.episodeNumber).isEqualTo(0)
        assertThat(entity.episodeName).isEmpty()
        assertThat(entity.runtimeInMinutes).isEqualTo(0)
        assertThat(entity.rating).isEqualTo(0f)
        assertThat(entity.seasonNumber).isEqualTo(0)
        assertThat(entity.seriesId).isEqualTo(202L)
        assertThat(entity.photoPath).isEmpty()
    }

    @Test
    fun `toEntity preserves seriesId even when other fields are null`() {
        val dto = EpisodeRemoteDto(
            seriesId = 999L
        )

        val entity = dto.toEntity()

        assertThat(entity.seriesId).isEqualTo(999L)
    }

    @Test
    fun `equality check between two identical DTOs`() {
        val dto1 = EpisodeRemoteDto(seriesId = 1L, name = "Episode 1")
        val dto2 = EpisodeRemoteDto(seriesId = 1L, name = "Episode 1")

        assertThat(dto1).isEqualTo(dto2)
    }

    @Test
    fun `hashCode consistency for identical DTOs`() {
        val dto1 = EpisodeRemoteDto(seriesId = 1L, name = "Episode 1")
        val dto2 = EpisodeRemoteDto(seriesId = 1L, name = "Episode 1")

        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode())
    }
}
