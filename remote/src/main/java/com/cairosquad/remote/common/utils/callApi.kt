package com.cairosquad.remote.common.utils

import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> callApi(
    execute: () -> HttpResponse
): T {
    val response = try {
        execute()
    } catch (e: Exception) {
        throw e
    }

    return responseToException(response)
}