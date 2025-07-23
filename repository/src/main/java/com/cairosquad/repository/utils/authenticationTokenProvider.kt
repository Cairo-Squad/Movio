package com.cairosquad.repository.utils

import com.cairosquad.repository.login.data_source.local.LocalAuthenticationDataSource
import kotlinx.coroutines.runBlocking

fun authenticationTokenProvider(
    localAuthenticationDataSource: LocalAuthenticationDataSource,
): String? {
    return runBlocking {
        try {
            localAuthenticationDataSource.getSessionId().ifBlank { null }
        } catch (_: Exception) {
            null
        }
    }
}