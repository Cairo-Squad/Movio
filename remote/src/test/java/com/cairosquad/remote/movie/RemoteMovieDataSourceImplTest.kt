package com.cairosquad.remote.movie

import com.cairosquad.remote.artists.MoviesListResponse
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.utils.sharedDto.remote.GenreResponse
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class RemoteMovieDataSourceImplTest {

    private lateinit var apiService: MovieApiService
    private lateinit var dataSource: MoviesRemoteDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        dataSource = spyk(MoviesRemoteDataSourceImpl(apiService))
    }

    @Test
    fun `getMoviesOfArtist should return list with non-null IDs`() = runTest {
        // Given
        val artistId = 99L
        val response = MoviesListResponse(
            movies = listOf(remoteMovies)
        )

        coEvery { apiService.getMoviesOfArtist(artistId) } returns response

        // When
        val result = dataSource.getMoviesOfArtist(artistId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(1)
    }

    @Test
    fun `getMovie should return movie details`() = runTest {

        val movieId = 10L
        val expected = MovieDetailsRemoteDto(
            id = 10,
            title = "Oppenheimer",
            overview = "A brilliant physicist...",
            releaseDate = "2023-07-21",
            voteAverage = 9.2
        )
        coEvery { apiService.getMovieById(movieId) } returns expected

        val result = dataSource.getMovieById(movieId)

        assertThat(result.id).isEqualTo(10)
        assertThat(result.title).isEqualTo("Oppenheimer")
        assertThat(result.releaseDate).isEqualTo("2023-07-21")
    }

    @Test
    fun `getMovieReviews should return non-null list of reviews`() = runTest {

        val movieId = 10L
        val expected = ResultResponse(
            results = listOf(
                ReviewRemoteDto(id = "r1", author = "Alice", content = "Great movie!"),
                ReviewRemoteDto(id = "r2", author = "Bob", content = "Masterpiece!")
            )
        )
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        val result = dataSource.getMovieReviews(movieId, page)

        assertThat(result).hasSize(2)
        assertThat(result.first().author).isEqualTo("Alice")
    }

    @Test
    fun `getSimilarMovies should return filtered list of movies`() = runTest {

        val movieId = 10L
        val expected = ResultResponse(
            results = listOf(
                MovieRemoteDto(id = 21, title = "Tenet", posterPath = null),
                MovieRemoteDto(id = null, title = null, posterPath = null),
            )
        )
        coEvery { apiService.getSimilarMovies(movieId, page) } returns expected

        val result = dataSource.getSimilarMovies(movieId, page)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Tenet")
    }


    @Test
    fun `getMovieReviews should return all valid items`() = runTest {

        val movieId = 10L
        val expected = ResultResponse(
            results = listOf(
                ReviewRemoteDto(id = "r1", author = "Alice", content = "Good"),
                ReviewRemoteDto(id = "r2", author = "Bob", content = "Excellent")
            )
        )
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        val result = dataSource.getMovieReviews(movieId, page)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getMovieReviews should filter out null items from results`() = runTest {
        val movieId = 10L
        val validReview = ReviewRemoteDto(id = "r1", author = "Alice", content = "Nice")
        val expected = ResultResponse(results = listOf(null, validReview, null))
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        val result = dataSource.getMovieReviews(movieId, page)

        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo("r1")
    }

    @Test
    fun `getMovieReviews should return empty list when results is null`() = runTest {

        val movieId = 10L
        val page = 1
        val expected = ResultResponse<ReviewRemoteDto>(results = null)
        coEvery { apiService.getMovieReviews(movieId, page) } returns expected

        val result = dataSource.getMovieReviews(movieId, page)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getTopRatingMovies should return filtered list of movies`() = runTest {

        val genreId = 28L
        val expected = ResultResponse(
            results = listOf(
                MovieRemoteDto(id = 1, title = "The Shawshank Redemption", voteAverage = 9.3),
                MovieRemoteDto(id = 2, title = "The Godfather", voteAverage = 9.2),
                null
            )
        )
        coEvery { apiService.getTopRatingMovies(page, genreId) } returns expected

        val result = dataSource.getTopRatingMovies(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("The Shawshank Redemption")
        assertThat(result[1].voteAverage?.toDouble()).isWithin(0.001).of(9.2)
    }

    @Test
    fun `getTopRatingMovies should return empty list when results is null`() = runTest {

        val page = 1
        coEvery {
            apiService.getTopRatingMovies(
                page, any()
            )
        } returns ResultResponse(results = null)

        val result = dataSource.getTopRatingMovies(page, 122L)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getVideoKey should return empty string when no video key found`() = runTest {

        val movieId = 123L
        coEvery { apiService.getVideoKey(movieId).getVideoKey() } returns null

        val result = dataSource.getVideoKey(movieId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getVideoKey should return video key when available`() = runTest {

        val movieId = 123L
        val expectedKey = "abc123"
        coEvery { apiService.getVideoKey(movieId).getVideoKey() } returns expectedKey

        val result = dataSource.getVideoKey(movieId)

        assertThat(result).isEqualTo(expectedKey)
    }

    @Test
    fun `getUpcomingMovies should return empty list when results is null`() = runTest {

        coEvery {
            apiService.getUpcomingMovies(any(), any(), any(), any())
        } returns ResultResponse(results = null)

        val result = dataSource.getUpcomingMovies(1, 122L)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoreRecommendedMovies should pass correct parameters`() = runTest {

        val genreId = 35L
        val expectedMovies = listOf(
            MovieRemoteDto(id = 1, title = "Inception"),
            MovieRemoteDto(id = 2, title = "Interstellar")
        )
        coEvery {
            apiService.getMoreRecommendedMovies(
                page,
                withGenres = genreId
            )
        } returns ResultResponse(results = expectedMovies)

        val result = dataSource.getMoreRecommendedMovies(page, genreId)

        assertThat(result).hasSize(2)
        coVerify { apiService.getMoreRecommendedMovies(page, withGenres = genreId) }
    }

    @Test
    fun `getFreeToWatchMovies should return filtered results`() = runTest {

        val response = ResultResponse(
            results = listOf(
                MovieRemoteDto(id = 1, title = "Free Movie 1"),
                null,
                MovieRemoteDto(id = 2, title = "Free Movie 2")
            )
        )
        coEvery { apiService.getFreeToWatchMovies(page, any()) } returns response

        val result = dataSource.getFreeToWatchMovies(page, 123L)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Free Movie 1")
    }

    @Test
    fun `getMoviesByCategory should pass genreId correctly`() = runTest {

        val genreId = 35L
        val page = 2
        val expected = listOf(MovieRemoteDto(id = 1, title = "Comedy Movie"))
        coEvery {
            apiService.getMoviesByGenre(
                genreId,
                page
            )
        } returns ResultResponse(results = expected)

        val result = dataSource.getMoviesByCategory(genreId, page)

        assertThat(result).hasSize(1)
        coVerify { apiService.getMoviesByGenre(genreId, page) }
    }

    @Test
    fun `getMoviesGenres should return non-null genres`() = runTest {

        val expectedGenres = listOf(
            GenreDto(id = 28, name = "Action"), GenreDto(id = 35, name = "Comedy")
        )
        coEvery { apiService.getMoviesGenres() } returns GenreResponse(genres = expectedGenres)

        val result = dataSource.getMoviesGenres()

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Action")
    }

    @Test
    fun `getAllMovies should handle all parameters`() = runTest {

        val genreId = 35L
        val expected = listOf(
            MovieRemoteDto(id = 1, title = "Movie 1"), MovieRemoteDto(id = 2, title = "Movie 2")
        )
        coEvery {
            apiService.getAllMovies(
                page,
                genreId,
                sortBy
            )
        } returns ResultResponse(results = expected)

        val result = dataSource.getAllMovies(page, genreId, sortBy)

        assertThat(result).hasSize(2)
        coVerify { apiService.getAllMovies(page, genreId, sortBy) }
    }

    @Test
    fun `getMoviesGenres should return empty list when genres is null`() = runTest {

        coEvery { apiService.getMoviesGenres() } returns GenreResponse(genres = null)

        val result = dataSource.getMoviesGenres()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoreRecommendedMovies should return empty list when results is null`() = runTest {

        coEvery {
            apiService.getMoreRecommendedMovies(
                any(),
                any()
            )
        } returns ResultResponse(results = null)

        val result = dataSource.getMoreRecommendedMovies(1, 122L)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getUpcomingMovies should use correct date range`() = runTest {

        val genreId = 35L
        val today = LocalDate.now()
        val thirtyDaysFromNow = today.plusDays(30)
        val expectedMovies = listOf(
            MovieRemoteDto(id = 1, title = "Dune: Part Two"),
            MovieRemoteDto(id = 2, title = "The Batman")
        )

        coEvery {
            apiService.getUpcomingMovies(
                page = page, withGenres = genreId,  // This was missing in your test
                minDate = today.toString(), maxDate = thirtyDaysFromNow.toString()
            )
        } returns ResultResponse(results = expectedMovies)

        val result = dataSource.getUpcomingMovies(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Dune: Part Two")

        coVerify {
            apiService.getUpcomingMovies(
                page = page,
                withGenres = genreId,
                minDate = today.toString(),
                maxDate = thirtyDaysFromNow.toString()
            )
        }
    }

    @Test
    fun `getUpcomingMovies should filter null results`() = runTest {

        val today = LocalDate.now()
        val thirtyDaysFromNow = today.plusDays(30)
        val response = ResultResponse(
            results = listOf(
                MovieRemoteDto(id = 1, title = "Valid Movie"),
                null,
                MovieRemoteDto(id = 2, title = "Another Valid Movie")
            )
        )

        coEvery {
            apiService.getUpcomingMovies(
                page = page,
                withGenres = genreId,
                minDate = today.toString(),
                maxDate = thirtyDaysFromNow.toString()
            )
        } returns response

        val result = dataSource.getUpcomingMovies(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Valid Movie")
        assertThat(result[1].title).isEqualTo("Another Valid Movie")

        coVerify {
            apiService.getUpcomingMovies(
                page = page,
                withGenres = genreId,
                minDate = today.toString(),
                maxDate = thirtyDaysFromNow.toString()
            )
        }
    }

    @Test
    fun `getNowPlayingMovies should use correct date range`() = runTest {
        val genreId = 35L
        val today = LocalDate.now()
        val twoWeeksAgo = today.minusWeeks(2)
        val expectedMovies = listOf(
            MovieRemoteDto(id = 1, title = "Spider-Man: No Way Home"),
            MovieRemoteDto(id = 2, title = "Top Gun: Maverick")
        )

        coEvery {
            apiService.getNowPlayingMovies(
                page = page,
                withGenres = genreId,
                minDate = twoWeeksAgo.toString(),
                maxDate = today.toString()
            )
        } returns ResultResponse(results = expectedMovies)

        val result = dataSource.getNowPlayingMovies(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Spider-Man: No Way Home")

        coVerify {
            apiService.getNowPlayingMovies(
                page = page,
                withGenres = genreId,
                minDate = twoWeeksAgo.toString(),
                maxDate = today.toString()
            )
        }
    }

    @Test
    fun `getTrendingMovies should use correct date range`() = runTest {

        val today = LocalDate.now()
        val lastMonth = today.minusDays(30)
        val expectedMovies = listOf(
            MovieRemoteDto(id = 1, title = "Everything Everywhere All at Once"),
            MovieRemoteDto(id = 2, title = "The Whale")
        )

        coEvery {
            apiService.getTrendingMovies(
                page = page,
                withGenres = genreId,
                minDate = lastMonth.toString(),
                maxDate = today.toString()
            )
        } returns ResultResponse(results = expectedMovies)

        val result = dataSource.getTrendingMovies(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Everything Everywhere All at Once")

        coVerify {
            apiService.getTrendingMovies(
                page = page,
                withGenres = genreId,
                minDate = lastMonth.toString(),
                maxDate = today.toString()
            )
        }
    }

    @Test
    fun `getPopularMovies should handle null genreId`() = runTest {

        val expected = listOf(MovieRemoteDto(id = 1, title = "Popular Movie"))

        coEvery {
            apiService.getPopularMovies(
                page,
                genreId
            )
        } returns ResultResponse(results = expected)

        val result = dataSource.getPopularMovies(page, genreId)

        assertThat(result).hasSize(1)

        coVerify { apiService.getPopularMovies(page, genreId) }
    }

    @Test
    fun `getAllMovies should handle parameters`() = runTest {

        val expected = listOf(MovieRemoteDto(id = 1, title = "Default Movie"))

        coEvery {
            apiService.getAllMovies(
                page,
                genreId,
                sortBy
            )
        } returns ResultResponse(results = expected)

        val result = dataSource.getAllMovies(page, genreId, sortBy)

        assertThat(result).hasSize(1)

        coVerify { apiService.getAllMovies(page, genreId, sortBy) }
    }

    @Test
    fun `getFreeToWatchMovies should handle genreId`() = runTest {

        val expected = listOf(MovieRemoteDto(id = 1, title = "Free Movie"))

        coEvery {
            apiService.getFreeToWatchMovies(
                page,
                genreId
            )
        } returns ResultResponse(results = expected)

        val result = dataSource.getFreeToWatchMovies(page, genreId)

        assertThat(result).hasSize(1)

        coVerify { apiService.getFreeToWatchMovies(page, genreId) }
    }

    @Test
    fun `getMoviesByQuery SHOULD return list of movieRemoteDto`() = runTest {
        val expectedMovies = listOf(MovieRemoteDto(id = 123, title = "Let's goo"))
        coEvery { apiService.getMoviesByQuery("let", 1) } returns ResultResponse<MovieRemoteDto>(results = expectedMovies)

        val result = dataSource.getMoviesByQuery("let", 1)

        assertThat(result).isEqualTo(expectedMovies)
    }

    @Test
    fun `getPersonalizedMovies Should return list of movieRemoteDto`() = runTest{
        val expectedMovies = listOf(MovieRemoteDto(id = 123, title = "Let's goo"))
        coEvery { apiService.getPersonalizedMovies(1) } returns ResultResponse<MovieRemoteDto>(results = expectedMovies)

        val result = dataSource.getPersonalizedMovies(1)

        assertThat(result).isEqualTo(expectedMovies)
    }

    @Test
    fun `getSuggestedMovies Should return list of movieRemoteDto`() = runTest{
        val expectedMovies = listOf(MovieRemoteDto(id = 123, title = "Let's goo"))
        coEvery { apiService.getSuggestedMovies() } returns ResultResponse<MovieRemoteDto>(results = expectedMovies)

        val result = dataSource.getSuggestedMovies()

        assertThat(result).isEqualTo(expectedMovies)
    }

    private companion object {

        val remoteMovies = MovieRemoteDto(id = 1, title = "Movie 1", posterPath = null)
        const val page = 1
        const val genreId = 122L
        const val sortBy = "popularity.desc"
    }
}