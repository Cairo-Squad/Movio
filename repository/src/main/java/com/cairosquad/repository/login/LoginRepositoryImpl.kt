package com.cairosquad.repository.login

import com.cairosquad.domain.repository.LoginRepository
import com.cairosquad.repository.login.data_source.local.LocalLoginDataSource
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.utils.mappers.tryToCall

class LoginRepositoryImpl(
    private val remoteLoginDataSource: RemoteLoginDataSource,
    private val localLoginDataSource: LocalLoginDataSource
) : LoginRepository {
    override suspend fun login(username: String, password: String) {
        tryToCall {
            var requestTokenResponse = remoteLoginDataSource.createRequestToken()
            requestTokenResponse = remoteLoginDataSource.authenticateRequestToken(
                username = username,
                password = password,
                requestToken = requestTokenResponse.toEntity()
            )
            remoteLoginDataSource.createSessionId(requestTokenResponse.toEntity())
        }

    }

    override suspend fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        tryToCall {
            localLoginDataSource.saveSessionId("")
        }
    }
}