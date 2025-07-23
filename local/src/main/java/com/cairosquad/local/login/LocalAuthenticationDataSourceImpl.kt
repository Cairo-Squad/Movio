package com.cairosquad.local.login

import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.repository.login.data_source.local.LocalAuthenticationDataSource
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto

class LocalAuthenticationDataSourceImpl(
    private val loginDao: LoginDao
) : LocalAuthenticationDataSource {
    override suspend fun saveSessionId(sessionId: String) {
        loginDao.saveSessionId(
            sessionId = SessionIdDto(sessionId = sessionId)
        )
    }

    override suspend fun getSessionId() = loginDao.getSessionId().getOrNull(0)?.sessionId ?: ""
}