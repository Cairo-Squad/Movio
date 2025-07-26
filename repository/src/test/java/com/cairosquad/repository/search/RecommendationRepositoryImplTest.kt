package com.cairosquad.repository.search

import com.cairosquad.entity.Movie
import com.cairosquad.repository.movie.MovieRepositoryImpl
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
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
    private lateinit var recommendationRepository: MovieRepositoryImpl
    private lateinit var remoteMovieDataSource: RemoteMovieDataSource

    @Before
    fun setUp() {
        discoveryDataSource = mockk(relaxed = true)
        remoteMovieDiscoveryDataSource = mockk(relaxed = true)
        remoteMovieDataSource = mockk(relaxed = true)
        recommendationRepository = MovieRepositoryImpl(
            remoteMovieDiscoveryDataSource = remoteMovieDiscoveryDataSource,
            discoveryDataSource = discoveryDataSource,
            remoteMovieDataSource = remoteMovieDataSource
        )
    }

    @Test
    fun `should return mapped movie list when getForYouMovies is called with remote movies`() =
        runTest {
            // Given
            coEvery { remoteMovieDiscoveryDataSource.getPersonalizedMovies(1) } returns remoteMovies

            // When
            val result = recommendationRepository.getPersonalizedMovies(1)

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].id).isEqualTo(1)
            assertThat(result[1].title).isEqualTo("Movie 2")
        }

    @Test
    fun `should return list of 10 fake movies when getSimilarMovies is called`() = runTest {

        coEvery { remoteMovieDataSource.getSimilarMovies(1L, 1)} returns List(10) { fakeMovieRemote }

        val result = recommendationRepository.getSimilarMovies(1L, 1)

        assertThat(result).hasSize(10)
        assertThat(result).contains(fakeMovie)
    }


    @Test
    fun `should return empty list when getForYouMovies is called with empty remote data`() =
        runTest {
            // Given
            coEvery { remoteMovieDiscoveryDataSource.getPersonalizedMovies(1) } returns emptyList()

            // When
            val result = recommendationRepository.getPersonalizedMovies(1)

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

        val fakeMovie = Movie(
            id = 157336L,
            title = "Interstellar",
            rating = 8.457f,
            posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 0,
            trailerPath = ""
        )

        val fakeMovieRemote = MovieRemoteDto(
            id = 157336,
            title = "Interstellar",
            voteAverage = 8.457,
            posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
        )
    }
}