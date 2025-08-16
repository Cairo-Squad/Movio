package com.cairosquad.remote.account

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.dto.AddToListRequest
import com.cairosquad.repository.account.data_source.remote.dto.CreateListRequest
import com.cairosquad.repository.account.data_source.remote.dto.FavoriteRequest
import com.cairosquad.repository.account.data_source.remote.dto.HistoryRequest
import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto
import com.cairosquad.repository.account.data_source.remote.dto.list_details.toRemoteMovieDto
import com.cairosquad.repository.account.data_source.remote.dto.list_details.toRemoteSeriesDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(
    private val apiService: AccountApiService
) : AccountRemoteDataSource {
    override suspend fun getAccountDetails(): AccountDto {
        return safeCallApi { apiService.getAccountDetails() }
    }

    override suspend fun getMovieLists(accountId: Long, page: Int): List<MediaListDto> {
        return safeCallApi { getListsByType(accountId, page, "movie") }
    }

    override suspend fun getSeriesLists(accountId: Long, page: Int): List<MediaListDto> {
        return safeCallApi { getListsByType(accountId, page, "tv") }
    }

    override suspend fun getMoviesOfList(
        listId: Long,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi {
            apiService.getListDetails(listId, page)
                .items
                ?.filter { it.mediaType == "movie" }
                ?.map { it.toRemoteMovieDto() }
                ?: emptyList()
        }
    }

    override suspend fun getSeriesOfList(
        listId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi {
            apiService.getListDetails(listId, page)
                .items
                ?.filter { it.mediaType == "tv" }
                ?.map { it.toRemoteSeriesDto() }
                ?: emptyList()
        }
    }

    override suspend fun addMovieToFavorite(accountId: Long, movieId: Long) {
        safeCallApi {
            apiService.addItemToFavorite(
                accountId,
                FavoriteRequest("movie", movieId, true)
            )
        }
    }

    override suspend fun addSeriesToFavorite(accountId: Long, seriesId: Long) {
        safeCallApi {
            apiService.addItemToFavorite(
                accountId,
                FavoriteRequest("tv", seriesId, true)
            )
        }
    }

    override suspend fun removeMovieFromFavorite(accountId: Long, movieId: Long) {
        safeCallApi {
            apiService.addItemToFavorite(
                accountId,
                FavoriteRequest("movie", movieId, false)
            )
        }
    }

    override suspend fun removeSeriesFromFavorite(accountId: Long, seriesId: Long) {
        safeCallApi {
            apiService.addItemToFavorite(
                accountId,
                FavoriteRequest("tv", seriesId, false)
            )
        }
    }

    override suspend fun getFavoriteMovies(
        accountId: Long,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi {
            apiService.getFavoriteMovies(accountId, page)
                .results
                ?.filterNotNull()
                ?: emptyList()
        }
    }

    override suspend fun getFavoriteSeries(
        accountId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi {
            apiService.getFavoriteSeries(accountId, page)
                .results
                ?.filterNotNull()
                ?: emptyList()
        }
    }

    override suspend fun addMovieToHistory(accountId: Long, movieId: Long) {
        safeCallApi {
            apiService.addItemToHistory(
                accountId,
                HistoryRequest("movie", movieId, true)
            )
        }
    }

    override suspend fun addSeriesToHistory(accountId: Long, seriesId: Long) {
        safeCallApi {
            apiService.addItemToHistory(
                accountId,
                HistoryRequest("tv", seriesId, true)
            )
        }
    }

    override suspend fun getHistoryMovies(
        accountId: Long,
        page: Int
    ): List<MovieRemoteDto> {
        val asd = safeCallApi {
            apiService.getMovieHistory(accountId, page)
                .results
                ?.filterNotNull()
                ?: emptyList()
        }
        return asd
    }

    override suspend fun getHistorySeries(
        accountId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi {
            apiService.getSeriesHistory(accountId, page)
                .results
                ?.filterNotNull()
                ?: emptyList()
        }
    }

    override suspend fun getRatedMovies(
        accountId: Long,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi {
            apiService.getRatedMovies(accountId, page)
                .results
                ?.filterNotNull()
                ?: emptyList()
        }
    }

    override suspend fun getRatedSeries(
        accountId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi {
            apiService.getRatedSeries(accountId, page)
                .results
                ?.filterNotNull()
                ?: emptyList()
        }
    }

    override suspend fun addMovieToList(listId: Long, movieId: Long) {
        safeCallApi {
            apiService.addMovieToList(listId, AddToListRequest(movieId))
        }
    }

    override suspend fun createList(listName: String) {
        safeCallApi {
            apiService.createList(
                CreateListRequest(
                    name = listName,
                    language = "en",
                    description = " "
                )
            )
        }
    }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        safeCallApi {
            apiService.removeMovieFromList(listId, AddToListRequest(movieId))
        }
    }

    override suspend fun removeMovieFromHistory(accountId: Long, movieId: Long) {
        apiService.addItemToHistory(
            accountId, HistoryRequest(
                mediaType = "movie", mediaId = movieId, false
            )
        )
    }

    override suspend fun removeSeriesFromHistory(accountId: Long, seriesId: Long) {
        safeCallApi {
            apiService.addItemToHistory(
                accountId,
                HistoryRequest("tv", seriesId, false)
            )
        }
    }

    private suspend fun getListsByType(
        accountId: Long,
        page: Int,
        type: String
    ): List<MediaListDto> {
        return apiService.getLists(accountId, page)
            .results
            .filter { it.listType == type }
    }
}