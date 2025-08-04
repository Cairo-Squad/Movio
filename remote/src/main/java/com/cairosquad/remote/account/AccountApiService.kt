package com.cairosquad.remote.account

import com.cairosquad.repository.account.data_source.remote.dto.AccountDto
import com.cairosquad.repository.account.data_source.remote.dto.FavoriteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApiService {

	@GET("account")
	fun getAccountDetails(): AccountDto

	@POST("account/{accountId}/favorite")
	fun addItemToFavorite(
		@Path("accountId")
		accountId: Long,
		@Body
		body: FavoriteRequest
	): Response<Unit>
}