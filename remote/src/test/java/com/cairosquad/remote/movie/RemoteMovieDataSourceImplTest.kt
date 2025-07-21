package com.cairosquad.remote.movie

import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteMovieDataSourceImplTest {

    private lateinit var apiService: MovieApiService
    private lateinit var dataSource: RemoteMovieDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        dataSource = spyk(RemoteMovieDataSourceImpl(apiService))
    }

    @Test
    fun `getMovie should return movie details`() = runTest {
        // Given
        val movieId = 10L
        val expected = MovieDetailsRemoteDto(
            id = 10,
            title = "Oppenheimer",
            overview = "A brilliant physicist...",
            releaseDate = "2023-07-21",
            voteAverage = 9.2
        )
        coEvery { apiService.getMovie(movieId) } returns expected

        // When
        val result = dataSource.getMovie(movieId)

        // Then
        assertThat(result.id).isEqualTo(10)
        assertThat(result.title).isEqualTo("Oppenheimer")
        assertThat(result.releaseDate).isEqualTo("2023-07-21")
    }

    @Test
    fun `getMovieReviews should return non-null list of reviews`() = runTest {
        // Given
        val movieId = 10L
        val page = 1
        val expected = ResultResponse(
            results = listOf(
                ReviewRemoteDto(id = "r1", author = "Alice", content = "Great movie!"),
                ReviewRemoteDto(id = "r2", author = "Bob", content = "Masterpiece!")
            )
        )
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        // When
        val result = dataSource.getMovieReviews(movieId, page)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.first().author).isEqualTo("Alice")
    }

    @Test
    fun `getSimilarMovies should return filtered list of movies`() = runTest {
        // Given
        val movieId = 10L
        val page = 1
        val expected = ResultResponse(
            results = listOf(
                MovieRemoteDto(id = 21, title = "Tenet", posterPath = null),
                MovieRemoteDto(id = null, title = null, posterPath = null),
            )
        )
        coEvery { apiService.getSimilarMovies(movieId, page) } returns expected

        // When
        val result = dataSource.getSimilarMovies(movieId, page)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Tenet")
    }

    @Test
    fun `getMovieTopCast should return cast list`() = runTest {
        // Given
        val movieId = 10L
        val page = 1
        val expected = CreditResponse(
            id = 10,
            cast = listOf(
                ArtistRemoteDto(id = 1, name = "Cillian Murphy", profilePath = null),
                ArtistRemoteDto(id = 2, name = "Emily Blunt", profilePath = null)
            )
        )
        coEvery { apiService.getMovieTopCast(movieId, page) } returns expected

        // When
        val result = dataSource.getMovieTopCast(movieId, page)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Cillian Murphy")
        assertThat(result[1].name).isEqualTo("Emily Blunt")
    }

    @Test
    fun `getMovieReviews should return all valid items`() = runTest {
        // Given
        val movieId = 10L
        val page = 1
        val expected = ResultResponse(
            results = listOf(
                ReviewRemoteDto(id = "r1", author = "Alice", content = "Good"),
                ReviewRemoteDto(id = "r2", author = "Bob", content = "Excellent")
            )
        )
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        // When
        val result = dataSource.getMovieReviews(movieId, page)

        // Then
        assertThat(result).hasSize(2)
    }

    @Test
    fun `getMovieReviews should filter out null items from results`() = runTest {
        // Given
        val movieId = 10L
        val page = 1
        val validReview = ReviewRemoteDto(id = "r1", author = "Alice", content = "Nice")
        val expected = ResultResponse(results = listOf(null, validReview, null))
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        // When
        val result = dataSource.getMovieReviews(movieId, page)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo("r1")
    }

    @Test
    fun `getMovieReviews should return empty list when results is null`() = runTest {
        // Given
        val movieId = 10L
        val page = 1
        val expected = ResultResponse<ReviewRemoteDto>(results = null)
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        // When
        val result = dataSource.getMovieReviews(movieId, page)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieTopCast filters out null items in cast list`() = runTest {
        // Given
        val artist1 = ArtistRemoteDto(id = 1, name = "Actor A")
        val artist2 = ArtistRemoteDto(id = 2, name = "Actor B")
        val response = CreditResponse(cast = listOf(artist1, null, artist2), id = 1)
        coEvery { apiService.getMovieTopCast(1L, 1) } returns response

        // When
        val result = dataSource.getMovieTopCast(1L, 1)

        // Then
        assertThat(result).containsExactly(artist1, artist2)
    }

    @Test
    fun `getMovieTopCast returns empty list when cast is null`() = runTest {
        // Given
        val response = CreditResponse(cast = null, id = 2)
        coEvery { apiService.getMovieTopCast(1L, 1) } returns response

        // When
        val result = dataSource.getMovieTopCast(1L, 1)

        // Then
        assertThat(result).isEmpty()
    }

}