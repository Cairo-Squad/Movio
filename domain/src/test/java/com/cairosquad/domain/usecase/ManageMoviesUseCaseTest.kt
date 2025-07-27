package com.cairosquad.domain.usecase

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ManageMoviesUseCaseTest {

    private lateinit var moviesRepository: MoviesRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var useCase: ManageMoviesUseCase

    @BeforeTest
    fun setUp() {
        moviesRepository = mockk()
        searchRepository = mockk()
        useCase = ManageMoviesUseCase(moviesRepository, searchRepository)
    }

    @Test
    fun `should return expectedMoviesSciFi and add query when getMoviesByQuery is called`() = runTest {
        val query = "space"
        val page = 1
        coEvery { moviesRepository.getMoviesByQuery(query, page) } returns expectedMoviesSciFi
        coEvery { searchRepository.addQuery(query) } returns Unit

        val result = useCase.getMoviesByQuery(query, page)

        assertEquals(expectedMoviesSciFi, result)
        coVerify { moviesRepository.getMoviesByQuery(query, page) }
        coVerify { searchRepository.addQuery(query) }
    }

    @Test
    fun `should throw RuntimeException when getMoviesByQuery fails`() = runTest {
        val query = "space"
        val page = 1
        val exception = RuntimeException("Search failed")
        coEvery { moviesRepository.getMoviesByQuery(query, page) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoviesByQuery(query, page)
        }

        assertEquals("Search failed", thrown.message)
        coVerify { moviesRepository.getMoviesByQuery(query, page) }
        coVerify(exactly = 0) { searchRepository.addQuery(query) }
    }

    @Test
    fun `should return expectedMoviesAdventurePopular when getAllMovies is called with category and sort type`() = runTest {
        val page = 1
        val genreId = 12L
        val sortType = SortType.POPULAR
        coEvery { moviesRepository.getAllMovies(page, genreId, sortType) } returns expectedMoviesAdventurePopular

        val result = useCase.getAllMovies(page, genreId, sortType)

        assertEquals(expectedMoviesAdventurePopular, result)
        coVerify { moviesRepository.getAllMovies(page, genreId, sortType) }
    }

    @Test
    fun `should return expectedMoviesNoFilter when getAllMovies is called with null category and sort`() = runTest {
        val page = 2
        val genreId: Long? = null
        val sortType: SortType? = null
        coEvery { moviesRepository.getAllMovies(page, genreId, sortType) } returns expectedMoviesNoFilter

        val result = useCase.getAllMovies(page, genreId, sortType)

        assertEquals(expectedMoviesNoFilter, result)
        coVerify { moviesRepository.getAllMovies(page, genreId, sortType) }
    }

    @Test
    fun `should return expectedMoviesNoFilter when getAllMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getAllMovies(page, null, null) } returns expectedMoviesNoFilter

        val result = useCase.getAllMovies(page)

        assertEquals(expectedMoviesNoFilter, result)
        coVerify { moviesRepository.getAllMovies(page, null, null) }
    }

    @Test
    fun `should throw RuntimeException when getAllMovies fails`() = runTest {
        val page = 3
        val genreId = 16L
        val sortType = SortType.LATEST
        val exception = RuntimeException("API failure")
        coEvery { moviesRepository.getAllMovies(page, genreId, sortType) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getAllMovies(page, genreId, sortType)
        }

        assertEquals("API failure", thrown.message)
        coVerify { moviesRepository.getAllMovies(page, genreId, sortType) }
    }

    @Test
    fun `should return expectedMoviesGenre35 when getFreeToWatchMovies is called with genreId`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { moviesRepository.getFreeToWatchMovies(page, genreId) } returns expectedMoviesGenre35

        val result = useCase.getFreeToWatchMovies(page, genreId)

        assertEquals(expectedMoviesGenre35, result)
        coVerify { moviesRepository.getFreeToWatchMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getFreeToWatchMovies is called with null genreId`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getFreeToWatchMovies(page, genreId) } returns expectedMoviesNoGenre

        val result = useCase.getFreeToWatchMovies(page, genreId)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getFreeToWatchMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getFreeToWatchMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getFreeToWatchMovies(page, null) } returns expectedMoviesNoGenre

        val result = useCase.getFreeToWatchMovies(page)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getFreeToWatchMovies(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getFreeToWatchMovies fails`() = runTest {
        val page = 3
        val genreId = 99L
        val exception = RuntimeException("Service unavailable")
        coEvery { moviesRepository.getFreeToWatchMovies(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getFreeToWatchMovies(page, genreId)
        }

        assertEquals("Service unavailable", thrown.message)
        coVerify { moviesRepository.getFreeToWatchMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesCategory28 when getMoreRecommendedMovies is called with genreId`() = runTest {
        val page = 1
        val genreId = 28L
        coEvery { moviesRepository.getMoreRecommendedMovies(page, genreId) } returns expectedMoviesCategory28

        val result = useCase.getMoreRecommendedMovies(page, genreId)

        assertEquals(expectedMoviesCategory28, result)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getMoreRecommendedMovies is called with null genreId`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getMoreRecommendedMovies(page, genreId) } returns expectedMoviesNoGenre

        val result = useCase.getMoreRecommendedMovies(page, genreId)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getMoreRecommendedMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getMoreRecommendedMovies(page, null) } returns expectedMoviesNoGenre

        val result = useCase.getMoreRecommendedMovies(page)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getMoreRecommendedMovies fails`() = runTest {
        val page = 3
        val genreId = 35L
        val exception = RuntimeException("Network error")
        coEvery { moviesRepository.getMoreRecommendedMovies(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoreRecommendedMovies(page, genreId)
        }

        assertEquals("Network error", thrown.message)
        coVerify { moviesRepository.getMoreRecommendedMovies(page, genreId) }
    }

    @Test
    fun `should return movie when getMovieById is called`() = runTest {
        val movieId = 123L
        coEvery { moviesRepository.getMovieById(movieId) } returns movie

        val result = useCase.getMovieById(movieId)

        assertThat(result).isEqualTo(movie)
        coVerify(exactly = 1) { moviesRepository.getMovieById(movieId) }
    }

    @Test
    fun `should throw RuntimeException when getMovieById fails`() = runTest {
        val movieId = 123L
        val exception = RuntimeException("Failed to fetch")
        coEvery { moviesRepository.getMovieById(movieId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMovieById(movieId)
        }

        assertEquals("Failed to fetch", thrown.message)
        coVerify(exactly = 1) { moviesRepository.getMovieById(movieId) }
    }

    @Test
    fun `should return reviews when getMovieReviews is called`() = runTest {
        val movieId = 123L
        val page = 1
        coEvery { moviesRepository.getMovieReviews(movieId, page) } returns listOf(review)

        val result = useCase.getMovieReviews(movieId, page)

        assertThat(result).containsExactly(review)
        coVerify(exactly = 1) { moviesRepository.getMovieReviews(movieId, page) }
    }

    @Test
    fun `should return reviews when getMovieReviews is called with only movieId`() = runTest {
        val movieId = 123L
        coEvery { moviesRepository.getMovieReviews(movieId, 1) } returns listOf(review)

        val result = useCase.getMovieReviews(movieId)

        assertThat(result).containsExactly(review)
        coVerify(exactly = 1) { moviesRepository.getMovieReviews(movieId, 1) }
    }

    @Test
    fun `should return similar movies when getSimilarMovies is called`() = runTest {
        val movieId = 123L
        val page = 1
        coEvery { moviesRepository.getSimilarMovies(movieId, page) } returns listOf(similarMovie)

        val result = useCase.getSimilarMovies(movieId, page)

        assertThat(result).containsExactly(similarMovie)
        coVerify(exactly = 1) { moviesRepository.getSimilarMovies(movieId, page) }
    }

    @Test
    fun `should return similar movies when getSimilarMovies is called with only movieId`() = runTest {
        val movieId = 123L
        coEvery { moviesRepository.getSimilarMovies(movieId, 1) } returns listOf(similarMovie)

        val result = useCase.getSimilarMovies(movieId)

        assertThat(result).containsExactly(similarMovie)
        coVerify(exactly = 1) { moviesRepository.getSimilarMovies(movieId, 1) }
    }

    @Test
    fun `should return top cast when getMovieTopCast is called`() = runTest {
        val movieId = 123L
        val page = 1
        coEvery { moviesRepository.getMovieTopCast(movieId, page) } returns listOf(actor)

        val result = useCase.getMovieTopCast(movieId, page)

        assertThat(result).containsExactly(actor)
        coVerify(exactly = 1) { moviesRepository.getMovieTopCast(movieId, page) }
    }

    @Test
    fun `should return top cast when getMovieTopCast is called with only movieId`() = runTest {
        val movieId = 123L
        coEvery { moviesRepository.getMovieTopCast(movieId, 1) } returns listOf(actor)

        val result = useCase.getMovieTopCast(movieId)

        assertThat(result).containsExactly(actor)
        coVerify(exactly = 1) { moviesRepository.getMovieTopCast(movieId, 1) }
    }

    @Test
    fun `should return expectedMoviesSciFi when getMoviesByCategory is called`() = runTest {
        val page = 1
        val genreId = 878L
        coEvery { moviesRepository.getMoviesByCategory(page, genreId) } returns expectedMoviesSciFi

        val result = useCase.getMoviesByCategory(page, genreId)

        assertEquals(expectedMoviesSciFi, result)
        coVerify { moviesRepository.getMoviesByCategory(page, genreId) }
    }

    @Test
    fun `should throw RuntimeException when getMoviesByCategory fails`() = runTest {
        val page = 2
        val genreId = 10749L
        val exception = RuntimeException("Database error")
        coEvery { moviesRepository.getMoviesByCategory(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoviesByCategory(page, genreId)
        }

        assertEquals("Database error", thrown.message)
        coVerify { moviesRepository.getMoviesByCategory(page, genreId) }
    }

    @Test
    fun `should return expectedGenres when getMoviesGenres is called`() = runTest {
        coEvery { moviesRepository.getMoviesGenres() } returns expectedGenres

        val result = useCase.getMoviesGenres()

        assertEquals(expectedGenres, result)
        coVerify { moviesRepository.getMoviesGenres() }
    }

    @Test
    fun `should throw RuntimeException when getMoviesGenres fails`() = runTest {
        val exception = RuntimeException("Genres fetch failed")
        coEvery { moviesRepository.getMoviesGenres() } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoviesGenres()
        }

        assertEquals("Genres fetch failed", thrown.message)
        coVerify { moviesRepository.getMoviesGenres() }
    }

    @Test
    fun `should return expectedNowPlayingWithGenre when getNowPlayingMovies is called with genre`() = runTest {
        val page = 1
        val genreId = 28L
        coEvery { moviesRepository.getNowPlayingMovies(page, genreId) } returns expectedNowPlayingWithGenre

        val result = useCase.getNowPlayingMovies(page, genreId)

        assertEquals(expectedNowPlayingWithGenre, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedNowPlayingWithoutGenre when getNowPlayingMovies is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getNowPlayingMovies(page, genreId) } returns expectedNowPlayingWithoutGenre

        val result = useCase.getNowPlayingMovies(page, genreId)

        assertEquals(expectedNowPlayingWithoutGenre, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedNowPlayingWithoutGenre when getNowPlayingMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getNowPlayingMovies(page, null) } returns expectedNowPlayingWithoutGenre

        val result = useCase.getNowPlayingMovies(page)

        assertEquals(expectedNowPlayingWithoutGenre, result)
        coVerify { moviesRepository.getNowPlayingMovies(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getNowPlayingMovies fails`() = runTest {
        val page = 3
        val genreId = 16L
        val exception = RuntimeException("Now playing fetch failed")
        coEvery { moviesRepository.getNowPlayingMovies(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getNowPlayingMovies(page, genreId)
        }

        assertEquals("Now playing fetch failed", thrown.message)
        coVerify { moviesRepository.getNowPlayingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedPersonalizedMovies when getPersonalizedMovies is called`() = runTest {
        val page = 1
        coEvery { moviesRepository.getPersonalizedMovies(page) } returns expectedPersonalizedMovies

        val result = useCase.getPersonalizedMovies(page)

        assertEquals(expectedPersonalizedMovies, result)
        coVerify { moviesRepository.getPersonalizedMovies(page) }
    }

    @Test
    fun `should return expectedPopularWithCategory when getPopularMovies is called with category`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { moviesRepository.getPopularMovies(page, genreId) } returns expectedPopularWithCategory

        val result = useCase.getPopularMovies(page, genreId)

        assertEquals(expectedPopularWithCategory, result)
        coVerify { moviesRepository.getPopularMovies(page, genreId) }
    }

    @Test
    fun `should return expectedPopularWithoutCategory when getPopularMovies is called with null category`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getPopularMovies(page, genreId) } returns expectedPopularWithoutCategory

        val result = useCase.getPopularMovies(page, genreId)

        assertEquals(expectedPopularWithoutCategory, result)
        coVerify { moviesRepository.getPopularMovies(page, genreId) }
    }

    @Test
    fun `should return expectedPopularWithoutCategory when getPopularMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getPopularMovies(page, null) } returns expectedPopularWithoutCategory

        val result = useCase.getPopularMovies(page)

        assertEquals(expectedPopularWithoutCategory, result)
        coVerify { moviesRepository.getPopularMovies(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getPopularMovies fails`() = runTest {
        val page = 3
        val genreId = 18L
        val exception = RuntimeException("Failed to fetch popular movies")
        coEvery { moviesRepository.getPopularMovies(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getPopularMovies(page, genreId)
        }

        assertEquals("Failed to fetch popular movies", thrown.message)
        coVerify { moviesRepository.getPopularMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getSuggestedMovies is called`() = runTest {
        coEvery { moviesRepository.getSuggestedMovies() } returns expectedMoviesNoGenre

        val result = useCase.getSuggestedMovies()

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getSuggestedMovies() }
    }

    @Test
    fun `should return expectedMoviesGenre35 when getTopRatingMovies is called with genre`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { moviesRepository.getTopRatingMovies(page, genreId) } returns expectedMoviesGenre35

        val result = useCase.getTopRatingMovies(page, genreId)

        assertEquals(expectedMoviesGenre35, result)
        coVerify { moviesRepository.getTopRatingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getTopRatingMovies is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getTopRatingMovies(page, genreId) } returns expectedMoviesNoGenre

        val result = useCase.getTopRatingMovies(page, genreId)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getTopRatingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getTopRatingMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getTopRatingMovies(page, null) } returns expectedMoviesNoGenre

        val result = useCase.getTopRatingMovies(page)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getTopRatingMovies(page, null) }
    }

    @Test
    fun `should return expectedMoviesCategory28 when getTrendingMovies is called with genre`() = runTest {
        val page = 1
        val genreId = 28L
        coEvery { moviesRepository.getTrendingMovies(page, genreId) } returns expectedMoviesCategory28

        val result = useCase.getTrendingMovies(page, genreId)

        assertEquals(expectedMoviesCategory28, result)
        coVerify { moviesRepository.getTrendingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getTrendingMovies is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getTrendingMovies(page, genreId) } returns expectedMoviesNoGenre

        val result = useCase.getTrendingMovies(page, genreId)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getTrendingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getTrendingMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getTrendingMovies(page, null) } returns expectedMoviesNoGenre

        val result = useCase.getTrendingMovies(page)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getTrendingMovies(page, null) }
    }

    @Test
    fun `should return expectedMoviesGenre35 when getUpcomingMovies is called with genre`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { moviesRepository.getUpcomingMovies(page, genreId) } returns expectedMoviesGenre35

        val result = useCase.getUpcomingMovies(page, genreId)

        assertEquals(expectedMoviesGenre35, result)
        coVerify { moviesRepository.getUpcomingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getUpcomingMovies is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getUpcomingMovies(page, genreId) } returns expectedMoviesNoGenre

        val result = useCase.getUpcomingMovies(page, genreId)

        assertEquals(expectedMoviesNoGenre, result)
    }

    @Test
    fun `should call getUpcomingMovies on repository when getUpcomingMovies is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { moviesRepository.getUpcomingMovies(page, genreId) } returns expectedMoviesNoGenre

        useCase.getUpcomingMovies(page, genreId)

        coVerify { moviesRepository.getUpcomingMovies(page, genreId) }
    }

    @Test
    fun `should return expectedMoviesNoGenre when getUpcomingMovies is called with only page`() = runTest {
        val page = 2
        coEvery { moviesRepository.getUpcomingMovies(page, null) } returns expectedMoviesNoGenre

        val result = useCase.getUpcomingMovies(page)

        assertEquals(expectedMoviesNoGenre, result)
        coVerify { moviesRepository.getUpcomingMovies(page, null) }
    }

    private companion object {
        val expectedMoviesSciFi = listOf(
            Movie(
                id = 30,
                title = "Galactic War",
                rating = 7.9f,
                posterPath = "/sci-fi1.jpg",
                genres = listOf(Genre(878, "Science Fiction")),
                overview = "A space epic battle.",
                releaseDate = 1709164800000L,
                runtimeMinutes = 125,
                trailerPath = "https://youtube.com/galacticwar"
            ),
            Movie(
                id = 31,
                title = "Alien Arrival",
                rating = 8.3f,
                posterPath = "/sci-fi2.jpg",
                genres = listOf(Genre(878, "Science Fiction"), Genre(53, "Thriller")),
                overview = "Mysterious signals from space.",
                releaseDate = 1711756800000L,
                runtimeMinutes = 115,
                trailerPath = "https://youtube.com/alienarrival"
            )
        )

        val expectedMoviesAdventurePopular = listOf(
            Movie(
                id = 20,
                title = "Jungle Quest",
                rating = 8.0f,
                posterPath = "/adventure1.jpg",
                genres = listOf(Genre(12, "Adventure")),
                overview = "An epic jungle journey.",
                releaseDate = 1706745600000L,
                runtimeMinutes = 130,
                trailerPath = "https://youtube.com/junglequest"
            )
        )

        val expectedMoviesNoFilter = listOf(
            Movie(
                id = 21,
                title = "Open Film",
                rating = 6.0f,
                posterPath = "/nofilter.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "A movie without restrictions.",
                releaseDate = 1700438400000L,
                runtimeMinutes = 100,
                trailerPath = "https://youtube.com/openfilm"
            )
        )

        val expectedMoviesGenre35 = listOf(
            Movie(
                id = 10,
                title = "Laugh Out Loud",
                rating = 7.1f,
                posterPath = "/comedy1.jpg",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A hilarious comedy for everyone.",
                releaseDate = 1701475200000L,
                runtimeMinutes = 90,
                trailerPath = "https://youtube.com/loltrailer"
            ),
            Movie(
                id = 11,
                title = "Funny Bones",
                rating = 6.8f,
                posterPath = "/comedy2.jpg",
                genres = listOf(Genre(35, "Comedy"), Genre(18, "Drama")),
                overview = "A heartfelt and funny journey.",
                releaseDate = 1704067200000L,
                runtimeMinutes = 100,
                trailerPath = "https://youtube.com/funnybones"
            )
        )

        val expectedMoviesNoGenre = listOf(
            Movie(
                id = 12,
                title = "Public Access",
                rating = 5.9f,
                posterPath = "/free.jpg",
                genres = listOf(Genre(99, "Documentary")),
                overview = "Free to watch documentary on open networks.",
                releaseDate = 1698883200000L,
                runtimeMinutes = 85,
                trailerPath = "https://youtube.com/publicaccess"
            )
        )

        val expectedMoviesCategory28 = listOf(
            Movie(
                id = 1,
                title = "Action Hero",
                rating = 7.8f,
                posterPath = "/path1.jpg",
                genres = listOf(Genre(28, "Action")),
                overview = "An action-packed adventure.",
                releaseDate = 1704067200000L,
                runtimeMinutes = 120,
                trailerPath = "https://youtube.com/trailer1"
            ),
            Movie(
                id = 2,
                title = "Thrill Ride",
                rating = 8.2f,
                posterPath = "/path2.jpg",
                genres = listOf(Genre(28, "Action"), Genre(53, "Thriller")),
                overview = "Edge of your seat thriller.",
                releaseDate = 1707523200000L,
                runtimeMinutes = 110,
                trailerPath = "https://youtube.com/trailer2"
            )
        )

        val expectedNowPlayingWithGenre = listOf(
            Movie(
                id = 100,
                title = "Action Reloaded",
                rating = 7.5f,
                posterPath = "/now1.jpg",
                genres = listOf(Genre(28, "Action")),
                overview = "Explosive action-packed movie currently in theaters.",
                releaseDate = 1720310400000L,
                runtimeMinutes = 115,
                trailerPath = "https://youtube.com/actionreloaded"
            )
        )

        val expectedNowPlayingWithoutGenre = listOf(
            Movie(
                id = 101,
                title = "Cinema Vibes",
                rating = 6.9f,
                posterPath = "/now2.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "Emotional drama now in theaters.",
                releaseDate = 1720396800000L,
                runtimeMinutes = 102,
                trailerPath = "https://youtube.com/cinemavibes"
            )
        )

        val expectedPopularWithCategory = listOf(
            Movie(
                id = 200,
                title = "Comedy King",
                rating = 7.3f,
                posterPath = "/popular1.jpg",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A laugh-out-loud comedy hit.",
                releaseDate = 1720483200000L,
                runtimeMinutes = 98,
                trailerPath = "https://youtube.com/comedyking"
            )
        )

        val expectedPopularWithoutCategory = listOf(
            Movie(
                id = 201,
                title = "Everyone's Favorite",
                rating = 8.1f,
                posterPath = "/popular2.jpg",
                genres = listOf(Genre(18, "Drama")),
                overview = "The most talked-about film of the year.",
                releaseDate = 1720569600000L,
                runtimeMinutes = 108,
                trailerPath = "https://youtube.com/everyonesfavorite"
            )
        )

        val expectedPersonalizedMovies = listOf(
            Movie(
                id = 101,
                title = "Dune",
                rating = 8.1f,
                posterPath = "/dune.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 9L,
                runtimeMinutes = 5,
                trailerPath = ""
            ),
            Movie(
                id = 102,
                title = "Blade Runner",
                rating = 8.0f,
                posterPath = "/blade.jpg",
                genres = emptyList(),
                overview = "",
                releaseDate = 9L,
                runtimeMinutes = 5,
                trailerPath = ""
            )
        )

        val expectedGenres = listOf(
            Genre(28, "Action"),
            Genre(35, "Comedy"),
            Genre(18, "Drama"),
            Genre(878, "Science Fiction"),
            Genre(10749, "Romance")
        )

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
            id = "123",
            author = "Ana",
            authorPhotoPath = "/poster.png",
            rating = 8.0,
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