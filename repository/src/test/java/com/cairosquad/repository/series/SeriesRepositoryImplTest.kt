package com.cairosquad.repository.series

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesRepositoryImplTest {

    private lateinit var seriesRepository: SeriesRepositoryImpl

    @Before
    fun setUp() {
        seriesRepository = SeriesRepositoryImpl()
    }

    @Test
    fun `should return fake series when getSeries is called`() = runTest {
        val result = seriesRepository.getSeries(1399L)

        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(1399L)
        assertThat(result.title).isEqualTo("Game of Thrones")
    }

    @Test
    fun `should return fake reviews list when getSeriesReviews is called`() = runTest {
        val result = seriesRepository.getSeriesReviews(1399L)

        assertThat(result).isNotNull()
        assertThat(result).hasSize(2)
        assertThat(result[0].author).isEqualTo("lmao7")
        assertThat(result[1].author).isEqualTo("Vlad Ulbricht")
    }

    @Test
    fun `should return fake seasons list when getSeriesSeasons is called`() = runTest {
        val result = seriesRepository.getSeriesSeasons(1399L)

        assertThat(result).isNotNull()
        assertThat(result).hasSize(14)
        assertThat(result[0].seasonName).isEqualTo("Specials")
        assertThat(result[1].seasonName).isEqualTo("Season 1")
    }

    @Test
    fun `should return list of 10 fake episodes when getEpisodes is called`() = runTest {
        val result = seriesRepository.getEpisodes(seriesId = 1399L, seasonNumber = 1)

        assertThat(result).isNotNull()
        assertThat(result).hasSize(10)
        assertThat(result[0].episodeName).isEqualTo("Winter Is Coming")
    }

    @Test
    fun `should return list of 10 fake series when getSimilarSeries is called`() = runTest {
        val result = seriesRepository.getSimilarSeries(1399L)

        assertThat(result).isNotNull()
        assertThat(result).hasSize(10)
        assertThat(result[0].title).isEqualTo("Game of Thrones")
    }

    @Test
    fun `should return fake artists list when getSeriesTopCast is called`() = runTest {
        val result = seriesRepository.getSeriesTopCast(1399L)

        assertThat(result).isNotNull()
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Joseph Mawle")
        assertThat(result[1].name).isEqualTo("Art Parkinson")
    }
}