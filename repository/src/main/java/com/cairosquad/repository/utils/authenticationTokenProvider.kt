package com.cairosquad.repository.utils

import com.cairosquad.repository.login.data_source.local.LocalLoginDataSource
import kotlinx.coroutines.runBlocking

fun authenticationTokenProvider(
    localLoginDataSource: LocalLoginDataSource,
): String? {
    return runBlocking {
        try {
            localLoginDataSource.getSessionId().ifBlank { null }
        } catch (_: Exception) {
            null
        }
    }
}