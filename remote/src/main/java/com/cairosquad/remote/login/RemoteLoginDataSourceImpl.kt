package com.cairosquad.remote.login

import com.cairosquad.remote.utils.retrofit.interceptor.AuthInterceptor
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.login.data_source.remote.dto.RequestTokenResponse
import com.cairosquad.repository.login.data_source.remote.dto.SessionIdResponse

class RemoteLoginDataSourceImpl(
    private val loginApiService: LoginApiService
) : RemoteLoginDataSource {
    override suspend fun createRequestToken(): RequestTokenResponse {
        return safeCallApi { loginApiService.createRequestToken() }
    }

    override suspend fun authenticateRequestToken(
        username: String,
        password: String,
        requestToken: String
    ): RequestTokenResponse {
        return safeCallApi {
            loginApiService.authenticateRequestToken(
                username,
                password,
                requestToken
            )
        }
    }

    override suspend fun createSessionId(requestToken: String): SessionIdResponse {
        return safeCallApi { loginApiService.createSessionId(requestToken) }
    }

    override suspend fun updateInterceptorToken(token: String) {
        AuthInterceptor.updateToken(token)
    }
}