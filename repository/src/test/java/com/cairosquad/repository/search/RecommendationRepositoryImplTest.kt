package com.cairosquad.repository.search

import com.cairosquad.repository.search.data_source.remote.dto.ApiMovieDto
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class RecommendationRepositoryImplTest {
    private lateinit var dataSource: RemoteMovieDiscoveryDataSource
    private lateinit var recommendationRepository: RemoteMovieDiscoveryRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        recommendationRepository = RemoteMovieDiscoveryRepositoryImpl(dataSource)
    }

    @Test
    fun `given movies from remote when getForYouMovies called then return mapped movie list`() =
        runTest {
            // Given
            val remoteMovies = listOf(
                ApiMovieDto(
                    id = 1,
                    title = "Movie 1",
                    posterPath = null,
                    voteAverage = null
                ),
                ApiMovieDto(
                    id = 2,
                    title = "Movie 2",
                    posterPath = null,
                    voteAverage = null
                )
            )
            coEvery { dataSource.getPersonalizedMovies() } returns remoteMovies

            // When
            val result = recommendationRepository.getPersonalizedMovies()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].id).isEqualTo(1)
            assertThat(result[1].title).isEqualTo("Movie 2")
        }

    @Test
    fun `given empty list from remote when getForYouMovies called then return empty list`() =
        runTest {
            // Given
            coEvery { dataSource.getPersonalizedMovies() } returns emptyList()

            // When
            val result = recommendationRepository.getPersonalizedMovies()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `given movies from remote when getExploreMoreMovies called then return mapped movie list`() =
        runTest {
            // Given
            val remoteApiMovieDto = listOf(
                ApiMovieDto(
                    id = 3,
                    title = "Movie 3",
                    posterPath = "some_path",
                    voteAverage = 4.2
                )
            )
            coEvery { dataSource.getSuggestedMovies() } returns remoteMovieDto

            // When
            val result = recommendationRepository.getSuggestedMovies()

            // Then
            assertThat(result.size).isEqualTo(1)
            assertThat(result[0].id).isEqualTo(3)
            assertThat(result[0].title).isEqualTo("Movie 3")
            assertThat(result[0].rating).isEqualTo(4.2f)
        }
}
