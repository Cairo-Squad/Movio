package com.cairosquad.repository.search

import com.cairosquad.repository.movies.MoviesRepositoryImpl
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class RecommendationRepositoryImplTest {
    private lateinit var remoteMovieDiscoveryDataSource: RemoteMovieDiscoveryDataSource
    private lateinit var discoveryDataSource: DiscoveryDataSource
    private lateinit var recommendationRepository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        discoveryDataSource = mockk(relaxed = true)
        remoteMovieDiscoveryDataSource = mockk(relaxed = true)
        recommendationRepository = MoviesRepositoryImpl(
            remoteMovieDiscoveryDataSource = remoteMovieDiscoveryDataSource,
            discoveryDataSource = discoveryDataSource
        )
    }

    @Test
    fun `should return mapped movie list when getForYouMovies is called with remote movies`() =
        runTest {
            // Given
            coEvery { remoteMovieDiscoveryDataSource.getPersonalizedMovies() } returns remoteMovies

            // When
            val result = recommendationRepository.getPersonalizedMovies()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].id).isEqualTo(1)
            assertThat(result[1].title).isEqualTo("Movie 2")
        }

    @Test
    fun `should return empty list when getForYouMovies is called with empty remote data`() =
        runTest {
            // Given
            coEvery { remoteMovieDiscoveryDataSource.getPersonalizedMovies() } returns emptyList()

            // When
            val result = recommendationRepository.getPersonalizedMovies()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `should return mapped movie list when getExploreMoreMovies is called with remote movies`() =
        runTest {
            // Given
            coEvery { remoteMovieDiscoveryDataSource.getSuggestedMovies() } returns movieRemoteDto
            coEvery { discoveryDataSource.clearExpiredCache(any()) } returns Unit
            coEvery { discoveryDataSource.getSuggestedMovies() } returns emptyList()

            // When
            val result = recommendationRepository.getSuggestedMovies()

            // Then
            assertThat(result.size).isEqualTo(1)
            assertThat(result[0].id).isEqualTo(3)
            assertThat(result[0].title).isEqualTo("Movie 3")
            assertThat(result[0].rating).isEqualTo(4.2f)
        }

    private companion object {
        val remoteMovies = listOf(
            MovieRemoteDto(
                id = 1,
                title = "Movie 1",
                posterPath = null,
                voteAverage = null
            ),
            MovieRemoteDto(
                id = 2,
                title = "Movie 2",
                posterPath = null,
                voteAverage = null
            )
        )

        val movieRemoteDto = listOf(
            MovieRemoteDto(
                id = 3,
                title = "Movie 3",
                posterPath = "some_path",
                voteAverage = 4.2
            )
        )
    }
}
