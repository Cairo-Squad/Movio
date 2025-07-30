package com.cairosquad.repository.series.data_source.remote.dto

import com.cairosquad.repository.utils.TimeUtils
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test


class SeasonRemoteDtoTest {

    private val seriesId = 555L

    @Before
    fun setup() {
        mockkObject(TimeUtils)
    }

    @After
    fun teardown() {
        io.mockk.unmockkObject(TimeUtils)
    }

    @Test
    fun `toEntity maps all non-null fields correctly`() {
        every { TimeUtils.dateToLong("2024-06-01") } returns 1720000000L

        val dto = SeasonRemoteDto(
            airDate = "2024-06-01",
            episodeCount = 10,
            id = 101L,
            name = "Season One",
            overview = "A great season.",
            posterPath = "/poster.jpg",
            seasonNumber = 1,
            voteAverage = 9.1
        )

        val entity = dto.toEntity(seriesId)

        assertThat(entity.seasonNumber).isEqualTo(1)
        assertThat(entity.seasonName).isEqualTo("Season One")
        assertThat(entity.seriesId).isEqualTo(seriesId)
        assertThat(entity.episodesCount).isEqualTo(10)
        assertThat(entity.rating).isEqualTo(4.55f)
        assertThat(entity.posterPath).isEqualTo("/poster.jpg")
        assertThat(entity.overview).isEqualTo("A great season.")
        assertThat(entity.airDate).isEqualTo(1720000000L)
    }

    @Test
    fun `toEntity maps null fields to default values`() {
        every { TimeUtils.dateToLong(any()) } returns 0L

        val dto = SeasonRemoteDto()

        val entity = dto.toEntity(seriesId)

        assertThat(entity.seasonNumber).isEqualTo(0)
        assertThat(entity.seasonName).isEmpty()
        assertThat(entity.seriesId).isEqualTo(seriesId)
        assertThat(entity.episodesCount).isEqualTo(0)
        assertThat(entity.rating).isEqualTo(0f)
        assertThat(entity.posterPath).isEmpty()
        assertThat(entity.overview).isEmpty()
        assertThat(entity.airDate).isEqualTo(0L)
    }

    @Test
    fun `toEntity preserves seriesId regardless of other values`() {
        every { TimeUtils.dateToLong(any()) } returns 0L

        val dto = SeasonRemoteDto(name = "Season 2")

        val entity = dto.toEntity(seriesId)

        assertThat(entity.seriesId).isEqualTo(seriesId)
        assertThat(entity.seasonName).isEqualTo("Season 2")
    }

    @Test
    fun `equality check between two identical DTOs`() {
        val dto1 = SeasonRemoteDto(name = "Season 3", seasonNumber = 3)
        val dto2 = SeasonRemoteDto(name = "Season 3", seasonNumber = 3)

        assertThat(dto1).isEqualTo(dto2)
    }

    @Test
    fun `hashCode consistency for identical DTOs`() {
        val dto1 = SeasonRemoteDto(name = "Season 3", seasonNumber = 3)
        val dto2 = SeasonRemoteDto(name = "Season 3", seasonNumber = 3)

        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode())
    }
}