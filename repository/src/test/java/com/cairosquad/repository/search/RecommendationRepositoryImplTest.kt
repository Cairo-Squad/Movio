package com.cairosquad.repository.search

import com.cairosquad.entity.Movie
import com.cairosquad.repository.search.data_source.remote.RemoteRecommendationDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import kotlin.test.Test

class RecommendationRepositoryImplTest {
    private lateinit var dataSource: RemoteRecommendationDataSource
    private lateinit var recommendationRepository: RecommendationRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        recommendationRepository = RecommendationRepositoryImpl(dataSource)
    }

    @Test
    fun `given movies from remote when getForYouMovies called then return mapped movie list`() =
        runTest {
            // Given
            val remoteMovies = listOf(
                MovieDto(
                    id = 1,
                    title = "Movie 1",
                    posterPath = null,
                    releaseDate = null,
                    voteAverage = null
                ),
                MovieDto(
                    id = 2,
                    title = "Movie 2",
                    posterPath = null,
                    releaseDate = null,
                    voteAverage = null
                )
            )
            coEvery { dataSource.getForYouMovies() } returns remoteMovies

            // When
            val result = recommendationRepository.getForYouMovies()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].id).isEqualTo(1)
            assertThat(result[1].title).isEqualTo("Movie 2")
        }

    @Test
    fun `given empty list from remote when getForYouMovies called then return empty list`() =
        runTest {
            // Given
            coEvery { dataSource.getForYouMovies() } returns emptyList()

            // When
            val result = recommendationRepository.getForYouMovies()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `given movies from remote when getExploreMoreMovies called then return mapped movie list`() =
        runTest {
            // Given
            val remoteMovieDto = listOf(
                MovieDto(
                    id = 3,
                    title = "Movie 3",
                    posterPath = "some_path",
                    releaseDate = "2023-01-01",
                    voteAverage = 4.2
                )
            )
            coEvery { dataSource.getExploreMoreMovies() } returns remoteMovieDto

            // When
            val result = recommendationRepository.getExploreMoreMovies()

            // Then
            assertThat(result.size).isEqualTo(1)
            assertThat(result[0].id).isEqualTo(3)
            assertThat(result[0].title).isEqualTo("Movie 3")
            assertThat(result[0].rating).isEqualTo(4.2f)
        }
}
