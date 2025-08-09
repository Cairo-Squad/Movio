package com.cairosquad.remote.account

import com.cairosquad.repository.account.data_source.remote.dto.AddToListRequest
import com.cairosquad.repository.account.data_source.remote.dto.CreateListRequest
import com.cairosquad.repository.account.data_source.remote.dto.FavoriteRequest
import com.cairosquad.repository.account.data_source.remote.dto.HistoryRequest
import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.MediaListResponse
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto
import com.cairosquad.repository.account.data_source.remote.dto.list_details.ListDetailsItem
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun `getAccountDetails SHOULD return account details`() = runTest {
        val expected = AccountDto(avatar = null, id = 123, name = "youssef", username = "youssef")
        coEvery { apiService.getAccountDetails() } returns expected

        val result = remoteDataSource.getAccountDetails()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getFavoriteMovies SHOULD return non-null filtered results`() = runTest {
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
    fun `getFavoriteMovies SHOULD return empty list WHEN results contains null`() = runTest {
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
    fun `getFavoriteMovies SHOULD return empty list WHEN results are null`() = runTest {

        coEvery { apiService.getFavoriteMovies(1, 1).results } returns null

        val result = remoteDataSource.getFavoriteMovies(1, 1)

        assertThat(result).isEqualTo(emptyList<MovieRemoteDto>())
    }

    @Test
    fun `getFavoriteSeries SHOULD return non-null filtered results`() = runTest {
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
    fun `getFavoriteSeries SHOULD return empty list WHEN results are null`() = runTest {

        coEvery { apiService.getFavoriteSeries(1, 1).results } returns null

        val result = remoteDataSource.getFavoriteSeries(1, 1)

        assertThat(result).isEqualTo(emptyList<MovieRemoteDto>())
    }

    @Test
    fun `getHistoryMovies SHOULD return non-null filtered results`() = runTest {
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
    fun `getHistoryMovies SHOULD return empty list WHEN results is null`() = runTest {

        coEvery { apiService.getMovieHistory(1, 1).results } returns null

        val result = remoteDataSource.getHistoryMovies(1, 1)

        assertThat(result).isEqualTo(emptyList<MovieRemoteDto>())
    }


    @Test
    fun `getHistorySeries SHOULD return non-null filtered results`() = runTest {
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
    fun `getHistorySeries SHOULD return empty list WHEN results is null`() = runTest {

        coEvery { apiService.getSeriesHistory(1, 1).results } returns null

        val result = remoteDataSource.getHistorySeries(1, 1)

        assertThat(result).isEqualTo(emptyList<MovieRemoteDto>())
    }

    @Test
    fun `getRatedMovies SHOULD return non-null filtered results`() = runTest {
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
    fun `getRatedMovies SHOULD return empty list WHEN results are null filtered results`() = runTest {

        coEvery { apiService.getRatedMovies(1, 1).results } returns null

        val result = remoteDataSource.getRatedMovies(1, 1)

        assertThat(result).isEqualTo(emptyList<MovieRemoteDto>())
    }

    @Test
    fun `getRatedSeries SHOULD return non-null filtered results`() = runTest {
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
    fun `getRatedSeries SHOULD return empty list WHEN results are null filtered results`() = runTest {

        coEvery { apiService.getRatedSeries(1, 1).results } returns null

        val result = remoteDataSource.getRatedSeries(1, 1)

        assertThat(result).isEqualTo(emptyList<SeriesRemoteDto>())
    }

    @Test
    fun `getMovieLists SHOULD return lists`() = runTest {
        val expected = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, expected, 1, 1)

        val result = remoteDataSource.getMovieLists(123, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMovieLists SHOULD return lists of type movie`() = runTest {
        val testLists = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"), MediaListDto(id = 312, name = "Test List", mediaCount = 3, listType = "tv"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, testLists, 1, 1)

        val result = remoteDataSource.getMovieLists(123, 1)

        assertThat(result[0].listType).isEqualTo("movie")
    }

    @Test
    fun `getSeriesLists SHOULD return lists`() = runTest {
        val expected = listOf(MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"))
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, expected, 1, 1)

        val result = remoteDataSource.getMovieLists(123, 1)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSeriesLists SHOULD return lists of type series`() = runTest {
        val testLists = listOf(
            MediaListDto(id = 123, name = "Test List", mediaCount = 3, listType = "movie"),
            MediaListDto(id = 312, name = "Test List", mediaCount = 3, listType = "tv")
        )
        coEvery { apiService.getLists(123, 1) } returns MediaListResponse(page = 1, testLists, 1, 1)

        val result = remoteDataSource.getSeriesLists(123, 1)

        assertThat(result[0].listType).isEqualTo("tv")
    }

    @Test
    fun `addMovieToFavorite SHOULD call apiService addItemToFavorite with correct parameters`() =
        runTest {
            val movieId = 123L
            val accountId = 123L
            val requestBody = FavoriteRequest(type = "movie", id = movieId, favorite = true)
            coEvery { apiService.addItemToFavorite(accountId, requestBody) } returns mockk()

            remoteDataSource.addMovieToFavorite(accountId, movieId)

            coVerify { apiService.addItemToFavorite(accountId, requestBody) }
        }

    @Test
    fun `addSeriesToFavorite SHOULD call apiService addItemToFavorite with correct parameters`() =
        runTest {
            val movieId = 123L
            val accountId = 123L
            val requestBody = FavoriteRequest(type = "tv", id = movieId, favorite = true)
            coEvery { apiService.addItemToFavorite(accountId, requestBody) } returns mockk()

            remoteDataSource.addSeriesToFavorite(accountId, movieId)

            coVerify { apiService.addItemToFavorite(accountId, requestBody) }
        }

    @Test
    fun `removeMovieFromFavorite SHOULD call apiService addItemToFavorite with correct parameters`() =
        runTest {
            val movieId = 123L
            val accountId = 123L
            val requestBody = FavoriteRequest(type = "movie", id = movieId, favorite = false)
            coEvery { apiService.addItemToFavorite(accountId, requestBody) } returns mockk()

            remoteDataSource.removeMovieFromFavorite(accountId, movieId)

            coVerify { apiService.addItemToFavorite(accountId, requestBody) }
        }

    @Test
    fun `removeSeriesFromFavorite SHOULD call apiService addItemToFavorite with correct parameters`() =
        runTest {
            val movieId = 123L
            val accountId = 123L
            val requestBody = FavoriteRequest(type = "tv", id = movieId, favorite = false)
            coEvery { apiService.addItemToFavorite(accountId, requestBody) } returns mockk()

            remoteDataSource.removeSeriesFromFavorite(accountId, movieId)

            coVerify { apiService.addItemToFavorite(accountId, requestBody) }
        }

    @Test
    fun `addMovieToList SHOULD call apiService addMovieToList`() = runTest {
        val listId = 123L
        val movieId = 11L
        coEvery { apiService.addMovieToList(listId, AddToListRequest(movieId)) } returns mockk()

        remoteDataSource.addMovieToList(listId, movieId)

        coVerify { apiService.addMovieToList(listId, AddToListRequest(movieId)) }
    }

    @Test
    fun `createList SHOULD call apiService createList`() = runTest {
        val listName = "AsdAsd"
        val requestBody = CreateListRequest(
            name = listName,
            language = "en",
            description = " "
        )
        coEvery { apiService.createList(requestBody) } returns mockk()

        remoteDataSource.createList(listName)

        coVerify { apiService.createList(requestBody) }
    }

    @Test
    fun `removeMovieFromList SHOULD call apiService removeMovieFromList`() = runTest {
        val listId = 123L
        val movieId = 11L

        coEvery { apiService.removeMovieFromList(listId, AddToListRequest(movieId)) } returns mockk()

        remoteDataSource.removeMovieFromList(listId, movieId)

        coVerify { apiService.removeMovieFromList(listId, AddToListRequest(movieId)) }
    }

    @Test
    fun `addMovieToHistory SHOULD call apiService addItemToHistory with correct parameters`() = runTest {
            val movieId = 123L
            val accountId = 123L
            val requestBody =
                HistoryRequest(mediaType = "movie", mediaId = movieId, watchlist = true)
            coEvery { apiService.addItemToHistory(accountId, requestBody) } returns mockk()

            remoteDataSource.addMovieToHistory(accountId, movieId)

            coVerify { apiService.addItemToHistory(accountId, requestBody) }
    }

    @Test
    fun `addSeriesToHistory SHOULD call apiService addItemToHistory with correct parameters`() = runTest {
        val seriesId = 123L
        val accountId = 123L
        val requestBody =
            HistoryRequest(mediaType = "tv", mediaId = seriesId, watchlist = true)
        coEvery { apiService.addItemToHistory(accountId, requestBody) } returns mockk()

        remoteDataSource.addSeriesToHistory(accountId, seriesId)

        coVerify { apiService.addItemToHistory(accountId, requestBody) }
    }

    @Test
    fun `getMoviesOfList SHOULD return list of MovieRemoteDto if there is any`() = runTest {
        val listId = 123L
        val page = 1
        val expected = listOf(MovieRemoteDto(id = 123, title = "asd asd"))
        val response: List<ListDetailsItem>? = listOf(ListDetailsItem(id = 123, title = "asd asd", mediaType = "movie"))

        coEvery { apiService.getListDetails(listId, page).items } returns response

        val result = remoteDataSource.getMoviesOfList(listId, page)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMoviesOfList SHOULD return empty list of MovieRemoteDto if items is null`() = runTest {
        val listId = 123L
        val page = 1
        val expected: List<MovieRemoteDto> = emptyList()

        coEvery { apiService.getListDetails(listId, page).items } returns null

        val result = remoteDataSource.getMoviesOfList(listId, page)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSeriesOfList SHOULD return list of SeriesRemoteDto if there is any`() = runTest {
        val listId = 123L
        val page = 1
        val expected = listOf(SeriesRemoteDto(id = 123, name = "asd asd", voteAverage = 0.0f))
        val response: List<ListDetailsItem>? = listOf(ListDetailsItem(id = 123, title = "asd asd", mediaType = "tv"))

        coEvery { apiService.getListDetails(listId, page).items } returns response

        val result = remoteDataSource.getSeriesOfList(listId, page)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSeriesOfList SHOULD return empty list of SeriesRemoteDto if items is null`() = runTest {
        val listId = 123L
        val page = 1
        val expected: List<SeriesRemoteDto> = emptyList()

        coEvery { apiService.getListDetails(listId, page).items } returns null

        val result = remoteDataSource.getSeriesOfList(listId, page)

        assertThat(result).isEqualTo(expected)
    }
}