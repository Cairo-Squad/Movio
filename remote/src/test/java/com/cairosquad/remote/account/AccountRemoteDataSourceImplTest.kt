package com.cairosquad.remote.account

import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.MediaListResponse
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class AccountRemoteDataSourceImplTest {

    private lateinit var apiService: AccountApiService
    private lateinit var remoteDataSource: AccountRemoteDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk(relaxed = true)
        remoteDataSource = AccountRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getAccountDetails should return account details`() = runTest {
        val expected = AccountDto(avatar = null, id = 123, name = "youssef", username = "youssef")
        coEvery { apiService.getAccountDetails() } returns expected

        val result = remoteDataSource.getAccountDetails()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getFavoriteMovies should return non-null filtered results`() = runTest {
        val movies = listOf(
            MovieRemoteDto(id = 1, title = "Movie 1"),
            null,
            MovieRemoteDto(id = 2, title = "Movie 2")
        )
        coEvery { apiService.getFavoriteMovies(1, 1) } returns ResultResponse<MovieRemoteDto>(results = movies)

        val result = remoteDataSource.getFavoriteMovies(1, 1)

        assertThat(result).containsExactly(
            MovieRemoteDto(id = 1, title = "Movie 1"),
            MovieRemoteDto(id = 2, title = "Movie 2")
        )
    }

    @Test
    fun `getFavoriteMovies should return empty list when results are null`() = runTest {
        val movies = listOf(
            null,
            null,
            null
        )
        coEvery { apiService.getFavoriteMovies(1, 1) } returns ResultResponse<MovieRemoteDto>(results = movies)

        val result = remoteDataSource.getFavoriteMovies(1, 1)

        assertThat(result).isEqualTo(emptyList<MovieRemoteDto>())
    }

    @Test
    fun `getFavoriteSeries should return non-null filtered results`() = runTest {
        val series = listOf(
            SeriesRemoteDto(id = 1, name = "Series 1"),
            null,
            SeriesRemoteDto(id = 2, name = "Series 2")
        )
        coEvery { apiService.getFavoriteSeries(1, 1) } returns ResultResponse<SeriesRemoteDto>(results = series)

        val result = remoteDataSource.getFavoriteSeries(1, 1)

        assertThat(result).containsExactly(
            SeriesRemoteDto(id = 1, name = "Series 1"),
            SeriesRemoteDto(id = 2, name = "Series 2")
        )
    }

    @Test
    fun `getHistoryMovies should return non-null filtered results`() = runTest {
        val movies = listOf(
            MovieRemoteDto(id = 1, title = "History Movie 1"),
            null,
            MovieRemoteDto(id = 2, title = "History Movie 2")
        )
        coEvery { apiService.getMovieHistory(1, 1) } returns ResultResponse<MovieRemoteDto>(results = movies)

        val result = remoteDataSource.getHistoryMovies(1, 1)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getHistorySeries should return non-null filtered results`() = runTest {
        val series = listOf(
            SeriesRemoteDto(id = 1, name = "History Series 1"),
            null,
            SeriesRemoteDto(id = 2, name = "History Series 2")
        )
        coEvery { apiService.getSeriesHistory(1, 1) } returns ResultResponse<SeriesRemoteDto>(results = series)

        val result = remoteDataSource.getHistorySeries(1, 1)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getRatedMovies should return non-null filtered results`() = runTest {
        val ratedMovies = listOf(
            MovieRemoteDto(id = 10, title = "Rated 1"),
            null,
            MovieRemoteDto(id = 11, title = "Rated 2")
        )
        coEvery { apiService.getRatedMovies(1, 1) } returns ResultResponse<MovieRemoteDto>(results = ratedMovies)

        val result = remoteDataSource.getRatedMovies(1, 1)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getRatedSeries should return non-null filtered results`() = runTest {
        val ratedSeries = listOf(
            SeriesRemoteDto(id = 20, name = "Rated Series 1"),
            null,
            SeriesRemoteDto(id = 21, name = "Rated Series 2")
        )
        coEvery { apiService.getRatedSeries(1, 1) } returns ResultResponse<SeriesRemoteDto>(results = ratedSeries)

        val result = remoteDataSource.getRatedSeries(1, 1)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `getMovieLists should return lists`() = runTest{
        val expected = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, expected, 1, 1)

        val result = remoteDataSource.getMovieLists(123, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieLists should return lists of type movie`() = runTest{
        val testLists = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"), MediaListDto(id = 312, name = "Test List", mediaCount = 3, listType = "tv"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, testLists, 1, 1)

        val result = remoteDataSource.getMovieLists(123, 1)

        assertThat(result[0].listType).isEqualTo("movie")
    }

    @Test
    fun `getSeriesLists should return lists`() = runTest{
        val expected = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, expected, 1, 1)

        val result = remoteDataSource.getMovieLists(123, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSeriesLists should return lists of type series`() = runTest{
        val testLists = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"), MediaListDto(id = 312, name = "Test List", mediaCount = 3, listType = "tv"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, testLists, 1, 1)

        val result = remoteDataSource.getSeriesLists(123, 1)

        assertThat(result[0].listType).isEqualTo("tv")
    }
}