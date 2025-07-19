package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetMoviesDetailsUseCaseTest {
    private val moviesRepository: MoviesRepository = mockk(relaxed = true)
    private lateinit var useCase: GetMoviesDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetMoviesDetailsUseCase(moviesRepository)
    }

    @Test
    fun `getMovie SHOULD return movie from repository`() = runTest {
        coEvery { moviesRepository.getMovie(123L) } returns movie

        val result = useCase.getMovie(123L)

        assertThat(result).isEqualTo(movie)
        coVerify(exactly = 1) { moviesRepository.getMovie(123L) }
    }

    @Test
    fun `getMovieReviews SHOULD return reviews from repository`() = runTest {
        coEvery { moviesRepository.getMovieReviews(123L) } returns listOf(review)

        val result = useCase.getMovieReviews(123L)

        assertThat(result).containsExactly(review)
        coVerify(exactly = 1) { moviesRepository.getMovieReviews(123L) }
    }

    @Test
    fun `getSimilarMovies SHOULD return similar movies from repository`() = runTest {
        coEvery { moviesRepository.getSimilarMovies(123L) } returns listOf(similarMovie)

        val result = useCase.getSimilarMovies(123L)

        assertThat(result).containsExactly(similarMovie)
        coVerify(exactly = 1) { moviesRepository.getSimilarMovies(123L) }
    }

    @Test
    fun `getMovieTopCast SHOULD return top cast from repository`() = runTest {
        coEvery { moviesRepository.getMovieTopCast(123L) } returns listOf(actor)

        val result = useCase.getMovieTopCast(123L)

        assertThat(result).containsExactly(actor)
        coVerify(exactly = 1) { moviesRepository.getMovieTopCast(123L) }
    }

    @Test
    fun `getMovie SHOULD throw exception when repository fails`() = runTest {
        coEvery { moviesRepository.getMovie(123L) } throws RuntimeException("Failed to fetch")

        assertThrows<RuntimeException> {
            useCase.getMovie(123L)
        }
    }

    private companion object {

        private val actor = Artist(
            id = 312,
            name = "Keanu",
            photoPath = "/keanu.jpeg",
            country = "Japan",
            birthDate = 1234L,
            biography = "bio graphy",
            department = "actor"
        )

        private val review = Review(
            id = 123,
            author = "Ana",
            authorPhotoPath = "/poster.png",
            rating = "8",
            date = 123,
            description = ""
        )

        private val movie = Movie(
            id = 123,
            runtimeMinutes = 45,
            rating = 9f,
            title = "Spider Man",
            posterPath = "/poster.png",
            genres = listOf(Genre(1, "Action")),
            overview = "overview",
            releaseDate = 123L,
            trailerPath = ""
        )

        private val similarMovie = Movie(
            id = 123,
            runtimeMinutes = 45,
            rating = 9f,
            title = "Spider Man",
            posterPath = "/poster.png",
            genres = listOf(Genre(1, "Action")),
            overview = "overview",
            releaseDate = 123L,
            trailerPath = ""
        )
    }
}