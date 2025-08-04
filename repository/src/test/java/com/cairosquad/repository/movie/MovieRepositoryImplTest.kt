package com.cairosquad.repository.movie

import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.model.SortType
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.movie.data_source.local.MoviesLocalDataSource
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.AuthorDetailsDto
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.utils.exception.RepoEmptyResponseException
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfAllMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfFreeToWatchMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoreRecommendedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMovie
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMovieReviews
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoviesByCategory
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoviesOfArtist
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfNowPlayingMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfPersonalizedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfPopularMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSimilarMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSuggestedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTopRatedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTrendingMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfUpcomingMovies
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private lateinit var remoteDataSource: MoviesRemoteDataSource
    private lateinit var localDataSource: MoviesLocalDataSource

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        repository = MovieRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `should return cached movies when getSimilarMovies is called and cache is available`() = runTest {
        val movieId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfSimilarMovies(movieId, page)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getSimilarMovies(movieId, page)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getSimilarMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getSimilarMovies is called and cache is empty`() = runTest {
        val movieId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfSimilarMovies(movieId, page)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSimilarMovies(movieId, page) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getSimilarMovies(movieId, page)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getSimilarMovies(movieId, page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getPersonalizedMovies is called and cache is available`() = runTest {
        val page = 1
        val cacheCode = getCacheCodeOfPersonalizedMovies(page)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getPersonalizedMovies(page)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getPersonalizedMovies(any()) }
    }

    @Test
    fun `should fetch data from remote when getPersonalizedMovies is called and cache is empty`() = runTest {
        val page = 1
        val cacheCode = getCacheCodeOfPersonalizedMovies(page)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getPersonalizedMovies(page) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getPersonalizedMovies(page)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getPersonalizedMovies(page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getSuggestedMovies is called and cache is available`() = runTest {
        val cacheCode = getCacheCodeOfSuggestedMovies()
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getSuggestedMovies()

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getSuggestedMovies() }
    }

    @Test
    fun `should fetch data from remote when getSuggestedMovies is called and cache is empty`() = runTest {
        val cacheCode = getCacheCodeOfSuggestedMovies()
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSuggestedMovies() } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getSuggestedMovies()

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getSuggestedMovies() }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getTopRatingMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfTopRatedMovies(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getTopRatingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getTopRatingMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getTopRatingMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfTopRatedMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getTopRatingMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getTopRatingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getTopRatingMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getUpcomingMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfUpcomingMovies(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getUpcomingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getUpcomingMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getUpcomingMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfUpcomingMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getUpcomingMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getUpcomingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getUpcomingMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getNowPlayingMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfNowPlayingMovies(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getNowPlayingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getNowPlayingMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getNowPlayingMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfNowPlayingMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getNowPlayingMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getNowPlayingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getNowPlayingMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getTrendingMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfTrendingMovies(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getTrendingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getTrendingMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getTrendingMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfTrendingMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getTrendingMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getTrendingMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getTrendingMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getMoreRecommendedMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfMoreRecommendedMovies(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getMoreRecommendedMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getMoreRecommendedMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getMoreRecommendedMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfMoreRecommendedMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getMoreRecommendedMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getMoreRecommendedMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getMoreRecommendedMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should fetch data from remote when getFreeToWatchMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfFreeToWatchMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getFreeToWatchMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getFreeToWatchMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getFreeToWatchMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getMoviesByCategory is called and cache is available`() = runTest {
        val genreId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfMoviesByCategory(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getMoviesByCategory(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getMoviesByCategory(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getMoviesByCategory is called and cache is empty`() = runTest {
        val genreId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfMoviesByCategory(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getMoviesByCategory(genreId, page) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getMoviesByCategory(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getMoviesByCategory(genreId, page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getPopularMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfPopularMovies(page, genreId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getPopularMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getPopularMovies(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getPopularMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfPopularMovies(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getPopularMovies(page, genreId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getPopularMovies(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getPopularMovies(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movies when getAllMovies is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val sortType: SortType? = null
        val cacheCode = getCacheCodeOfAllMovies(page, genreId, sortType)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getAllMovies(page, genreId, sortType)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getAllMovies(any(), any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getAllMovies is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val sortType: SortType? = null
        val cacheCode = getCacheCodeOfAllMovies(page, genreId, sortType)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getAllMovies(page, genreId, sortType?.sortBy) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getAllMovies(page, genreId, sortType)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getAllMovies(page, genreId, sortType?.sortBy) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return movies when getMoviesByQuery is called`() = runTest {
        val query = "Test"
        val page = 1

        coEvery { remoteDataSource.getMoviesByQuery(query, page) } returns listOf(movieRemoteDto)

        val result = repository.getMoviesByQuery(query, page)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { remoteDataSource.getMoviesByQuery(any(), any()) }
    }


    @Test
    fun `should fetch data  when getMoviesByQuery is called`() = runTest {
        val query = "Test"
        val page = 1
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getMoviesByQuery(query, page) } returns listOf(movieRemoteDto)

        val result = repository.getMoviesByQuery(query, page)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getMoviesByQuery(query, page) }
    }

    @Test
    fun `should return cached movies when getMoviesOfArtist is called and cache is available`() = runTest {
        val artistId = 100L
        val cacheCode = getCacheCodeOfMoviesOfArtist(artistId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getMoviesOfArtist(artistId)

        assertThat(result).isEqualTo(listOf(expectedMovie))
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getMoviesOfArtist(any()) }
    }

    @Test
    fun `should fetch data from remote when getMoviesOfArtist is called and cache is empty`() = runTest {
        val artistId = 100L
        val cacheCode = getCacheCodeOfMoviesOfArtist(artistId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getMoviesOfArtist(artistId) } returns listOf(movieRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getMoviesOfArtist(artistId)

        assertThat(result).isEqualTo(listOf(expectedMovie.copy(trailerPath = "", runtimeMinutes = 0)))
        coVerify(exactly = 1) { remoteDataSource.getMoviesOfArtist(artistId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached movie when getMovieById is called and cache is available`() = runTest {
        val movieId = 1L
        val cacheCode = getCacheCodeOfMovie(movieId)
        val cachedMovies = listOf(cachedMovieDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns cachedMovies

        val result = repository.getMovieById(movieId)

        assertThat(result).isEqualTo(expectedMovie)
        coVerify(exactly = 1) { localDataSource.getMoviesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getMovieById(any()) }
    }

    @Test
    fun `should fetch data from remote when getMovieById is called and cache is empty`() = runTest {
        val movieId = 1L
        val cacheCode = getCacheCodeOfMovie(movieId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMovieById(movieId) } returns movieDetailsRemoteDto
        coEvery { remoteDataSource.getVideoKey(movieId) } returns "trailer_key"
        coEvery { localDataSource.insertCacheCodeWithMovies(any()) } just Runs

        val result = repository.getMovieById(movieId)

        assertThat(result).isEqualTo(expectedMovie)
        coVerify(exactly = 1) { remoteDataSource.getMovieById(movieId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should throw DomainEmptyResponseException when getMovieById is called and remote returns empty`() = runTest {
        val movieId = 1L
        val cacheCode = getCacheCodeOfMovie(movieId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMoviesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMovieById(movieId) } throws RepoEmptyResponseException()

        assertFailsWith<DomainEmptyResponseException> {
            repository.getMovieById(movieId)
        }
        coVerify(exactly = 1) { remoteDataSource.getMovieById(movieId) }
        coVerify(exactly = 0) { localDataSource.insertCacheCodeWithMovies(any()) }
    }

    @Test
    fun `should return cached reviews when getMovieReviews is called and cache is available`() = runTest {
        val movieId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfMovieReviews(page, movieId)
        val cachedReviews = listOf(cachedReviewDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMovieReviewsByCacheCode(cacheCode) } returns cachedReviews

        val result = repository.getMovieReviews(movieId, page)

        assertThat(result).isEqualTo(listOf(expectedReview))
        coVerify(exactly = 1) { localDataSource.getMovieReviewsByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getMovieReviews(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getMovieReviews is called and cache is empty`() = runTest {
        val movieId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfMovieReviews(page, movieId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getMovieReviewsByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getMovieReviews(movieId, page) } returns listOf(reviewRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithReviews(any()) } just Runs

        val result = repository.getMovieReviews(movieId, page)

        assertThat(result).isEqualTo(listOf(expectedReview))
        coVerify(exactly = 1) { remoteDataSource.getMovieReviews(movieId, page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithReviews(any()) }
    }

    @Test
    fun `should return cached genres when getMoviesGenres is called and cache is available`() = runTest {
        val cachedGenres = listOf(cachedGenreDto)
        coEvery { localDataSource.getMovieGenres() } returns cachedGenres

        val result = repository.getMoviesGenres()

        assertThat(result).isEqualTo(listOf(expectedGenre))
        coVerify(exactly = 1) { localDataSource.getMovieGenres() }
        coVerify(exactly = 0) { remoteDataSource.getMoviesGenres() }
    }

    @Test
    fun `should fetch data from remote when getMoviesGenres is called and cache is empty`() = runTest {
        coEvery { localDataSource.getMovieGenres() } returns emptyList()
        coEvery { remoteDataSource.getMoviesGenres() } returns listOf(genreRemoteDto)
        coEvery { localDataSource.insertMovieGenres(any()) } just Runs

        val result = repository.getMoviesGenres()

        assertThat(result).isEqualTo(listOf(expectedGenre))
        coVerify(exactly = 1) { remoteDataSource.getMoviesGenres() }
        coVerify(exactly = 1) { localDataSource.insertMovieGenres(any()) }
    }

    private companion object {
        private val movieRemoteDto = MovieRemoteDto(
            id = 1,
            title = "Test Movie",
            voteAverage = 8.0,
            posterPath = "/poster.jpg",
            genreIds = listOf(1L),
            overview = "A great movie",
            releaseDate = "2023-01-01"
        )

        private val movieDetailsRemoteDto = MovieDetailsRemoteDto(
            id = 1L,
            title = "Test Movie",
            voteAverage = 8.0,
            posterPath = "/poster.jpg",
            genres = listOf(GenreDto(id = 1, name = "Drama")),
            overview = "A great movie",
            releaseDate = "2023-01-01",
            runtime = 120,
        )

        private val cachedGenreDto = GenreOfMovieCacheDto(
            id = 1L,
            name = "Drama",
            cachingTimestamp = System.currentTimeMillis()
        )

        private val expectedGenre = Genre(
            id = 1L,
            name = "Drama"
        )

        private val cachedMovieDto = MovieCacheDto(
            movieWithoutGenre = MovieWithoutGenreCacheDto(
                id = 1L,
                title = "Test Movie",
                rating = 4.0f,
                posterPath = "/poster.jpg",
                trailerPath = "trailer_key",
                overview = "A great movie",
                releaseDate = 1672531200000L, // 2023-01-01
                runtime = 120,
                cachingTimestamp = System.currentTimeMillis()
            ),
            genres = listOf(cachedGenreDto)
        )

        private val expectedMovie = Movie(
            id = 1L,
            title = "Test Movie",
            rating = 4.0f,
            posterPath = "/poster.jpg",
            trailerPath = "trailer_key",
            genres = listOf(expectedGenre),
            overview = "A great movie",
            releaseDate = 1672531200000L,
            runtimeMinutes = 120
        )

        private val reviewRemoteDto = ReviewRemoteDto(
            id = "review1",
            author = "John Doe",
            authorDetails = AuthorDetailsDto(
                avatarPath = "/avatar.jpg",
                rating = 8.5
            ),
            content = "Great movie!",
            createdAt = "2023-01-01T00:00:00.000Z"
        )

        private val cachedReviewDto = ReviewCacheDto(
            id = "review1",
            author = "John Doe",
            authorPhotoPath = "/avatar.jpg",
            rating = 4.25f,
            date = 1672531200000L,
            description = "Great movie!",
            cachingTimestamp = System.currentTimeMillis()
        )

        private val expectedReview = Review(
            id = "review1",
            author = "John Doe",
            authorPhotoPath = "/avatar.jpg",
            rating = 4.25f,
            date = 1672531200000L,
            description = "Great movie!"
        )

        private val genreRemoteDto = GenreDto(
            id = 1,
            name = "Drama"
        )
    }
}