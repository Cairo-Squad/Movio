package com.cairosquad.local.login

import android.util.Log
import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.repository.login.data_source.local.LocalLoginDataSource
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto

class LocalLoginDataSourceImpl(
    private val loginDao: LoginDao
) : LocalLoginDataSource {
    override suspend fun saveSessionId(sessionId: String) {
        Log.e("TEST", "saveSessionId: $sessionId")
        loginDao.saveSessionId(
            sessionId = SessionIdDto(sessionId = sessionId)
        )
    }

    override suspend fun getSessionId() = loginDao.getSessionId().getOrNull(0)?.sessionId ?: ""
}