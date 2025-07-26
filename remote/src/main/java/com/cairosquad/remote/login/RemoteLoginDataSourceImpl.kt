package com.cairosquad.remote.login

import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.login.data_source.remote.dto.RequestTokenResponse
import com.cairosquad.repository.login.data_source.remote.dto.SessionIdResponse

class RemoteLoginDataSourceImpl(
    private val apiServiceProvider: ApiServiceProvider
) : RemoteLoginDataSource {
    override suspend fun createRequestToken(): RequestTokenResponse {
        return safeCallApi { apiServiceProvider.getLoginApiService().createRequestToken() }
    }

    override suspend fun authenticateRequestToken(
        username: String,
        password: String,
        requestToken: String
    ): RequestTokenResponse {
        return safeCallApi {
            apiServiceProvider.getLoginApiService().authenticateRequestToken(
                username,
                password,
                requestToken
            )
        }
    }

    override suspend fun createSessionId(requestToken: String): SessionIdResponse {
        return safeCallApi {
            apiServiceProvider.getLoginApiService().createSessionId(requestToken)
        }
    }


    override suspend fun updateInterceptorToken(token: String) {
        apiServiceProvider.updateToken(token)
    }


}