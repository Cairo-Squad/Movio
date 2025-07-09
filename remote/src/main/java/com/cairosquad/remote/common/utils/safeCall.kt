package com.cairosquad.remote.common.utils

import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): T {
    val response = try {
        execute()
    } catch (e: Exception) {
        throw e
    }

    return responseToResult(response)
}