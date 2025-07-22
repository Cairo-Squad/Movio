package com.cairosquad.remote.login

import com.cairosquad.repository.login.data_source.remote.dto.RequestTokenResponse
import com.cairosquad.repository.login.data_source.remote.dto.SessionIdResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {

    @GET("authentication/token/new")
    suspend fun createRequestToken(): RequestTokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun authenticateRequestToken(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("request_token") requestToken: String
    ): RequestTokenResponse

    @GET("authentication/session/new")
    suspend fun createSessionId(
        @Query("request_token") requestToken: String
    ): SessionIdResponse


}