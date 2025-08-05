package com.cairosquad.remote.account

import com.cairosquad.repository.account.data_source.remote.dto.FavoriteRequest
import com.cairosquad.repository.account.data_source.remote.dto.MediaListResponse
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountApiService {

	@GET("account")
	suspend fun getAccountDetails(): AccountDto

	@GET("account/{accountId}/lists")
	suspend fun getLists(
		@Path("accountId")
		accountId: Long,
		@Query("page")
		page: Int
	): MediaListResponse

	@POST("account/{accountId}/favorite")
	suspend fun addItemToFavorite(
		@Path("accountId")
		accountId: Long,
		@Body
		body: FavoriteRequest
	): Response<Unit>

	@GET("account/{accountId}/favorite/movies")
	suspend fun getFavoriteMovies(
		@Path("accountId")
		accountId: Long,
		@Query("page")
		page: Int
	): ResultResponse<MovieRemoteDto>

	@GET("account/{accountId}/favorite/tv")
	suspend fun getFavoriteSeries(
		@Path("accountId")
		accountId: Long,
		@Query("page")
		page: Int
	): ResultResponse<SeriesRemoteDto>

	@GET("account/{accountId}/rated/movies")
	suspend fun getRatedMovies(
		@Path("accountId")
		accountId: Long,
		@Query("page")
		page: Int
	): ResultResponse<MovieRemoteDto>

	@GET("account/{accountId}/rated/tv")
	suspend fun getRatedSeries(
		@Path("accountId")
		accountId: Long,
		@Query("page")
		page: Int
	): ResultResponse<SeriesRemoteDto>
}