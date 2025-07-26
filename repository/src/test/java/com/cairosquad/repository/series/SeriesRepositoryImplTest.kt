package com.cairosquad.repository.series

import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesRepositoryImplTest {

    private lateinit var seriesRepository: SeriesRepositoryImpl
    private lateinit var seriesRemoteDataSource: SeriesRemoteDataSource

    @Before
    fun setUp() {

        seriesRemoteDataSource = mockk(relaxed = true)

        seriesRepository = SeriesRepositoryImpl(
            seriesRemoteDataSource = seriesRemoteDataSource
        )
    }

    @Test
    fun `should return fake series when getSeries is called`() = runTest {
        coEvery { seriesRemoteDataSource.getSeriesById(1399L) } returns SeriesDetailsRemoteDto()

        val result = seriesRepository.getSeriesById(1399L)

        assertThat(result.id).isEqualTo(SeriesDetailsRemoteDto().toEntity("").id)
    }
}