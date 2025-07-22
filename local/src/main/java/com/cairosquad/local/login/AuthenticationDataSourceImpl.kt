package com.cairosquad.local.login

import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.repository.login.data_source.local.AuthenticationDataSource
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto

class AuthenticationDataSourceImpl(
    private val loginDao: LoginDao
) : AuthenticationDataSource {
    override suspend fun saveSessionId(sessionId: String) = loginDao.saveSessionId(
        sessionId = SessionIdDto(sessionId = sessionId)
    )

    override suspend fun getSessionId() = loginDao.getSessionId()?.sessionId ?: ""
}