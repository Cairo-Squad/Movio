package com.cairosquad.remote.common.utils

import com.cairosquad.repository.common.exception.NoInternetException
import io.ktor.client.statement.HttpResponse
import java.io.IOException

suspend inline fun <reified T> callApi(
    execute: () -> HttpResponse
): T {
    val response = try {
        execute()
    } catch (e: IOException) {
        throw NoInternetException(e.message ?: "")
    } catch (e: Exception) {
        throw e
    }

    return responseToException(response)
}