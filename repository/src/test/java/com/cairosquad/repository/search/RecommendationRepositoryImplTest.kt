package com.cairosquad.repository.search

import com.cairosquad.repository.search.data_source.remote.RemoteRecommendationDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `should return mapped movie list when getForYouMovies is called with remote movies`() =
        runTest {
            // Given
            val remoteMovies = listOf(
                MovieDto(id = 1, title = "Movie 1", posterPath = null, voteAverage = null),
                MovieDto(id = 2, title = "Movie 2", posterPath = null, voteAverage = null)
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
    fun `should return empty list when getForYouMovies is called with empty remote data`() =
        runTest {
            // Given
            coEvery { dataSource.getForYouMovies() } returns emptyList()

            // When
            val result = recommendationRepository.getForYouMovies()

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `should return mapped movie list when getExploreMoreMovies is called with remote movies`() =
        runTest {
            // Given
            val remoteMovieDto = listOf(
                MovieDto(id = 3, title = "Movie 3", posterPath = "some_path", voteAverage = 4.2)
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
