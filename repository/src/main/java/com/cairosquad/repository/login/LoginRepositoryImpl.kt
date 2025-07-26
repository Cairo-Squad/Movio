package com.cairosquad.repository.login

import com.cairosquad.domain.repository.LoginRepository
import com.cairosquad.repository.login.data_source.local.LocalAuthenticationDataSource
import com.cairosquad.repository.login.data_source.remote.RemoteLoginDataSource
import com.cairosquad.repository.utils.mappers.tryToCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginRepositoryImpl(
    private val remoteLoginDataSource: RemoteLoginDataSource,
    private val localAuthenticationDataSource: LocalAuthenticationDataSource
) : LoginRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            localAuthenticationDataSource.getSessionId()
                .takeIf { it != "" }
                ?.also { remoteLoginDataSource.updateInterceptorToken(it) }
        }
    }

    override suspend fun login(username: String, password: String) {
        tryToCall {
            var requestTokenResponse = remoteLoginDataSource.createRequestToken()
            requestTokenResponse = remoteLoginDataSource.authenticateRequestToken(
                username = username,
                password = password,
                requestToken = requestTokenResponse.toEntity()
            )
            remoteLoginDataSource.createSessionId(requestTokenResponse.toEntity())
            localAuthenticationDataSource.saveSessionId(requestTokenResponse.toEntity())
            remoteLoginDataSource.updateInterceptorToken(requestTokenResponse.toEntity())
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return tryToCall {
            localAuthenticationDataSource.getSessionId().isNotEmpty()
        }
    }

    override suspend fun logout() {
        tryToCall {
            localAuthenticationDataSource.saveSessionId("")
            remoteLoginDataSource.updateInterceptorToken("")
        }
    }
}