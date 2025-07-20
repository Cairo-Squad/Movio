package com.cairosquad.remote.utils.retrofit

import com.cairosquad.repository.utils.mappers.ErrorDto
import kotlinx.serialization.json.Json

fun extractErrorMessage(errorBody: String?): String {
    return try {
        if (errorBody.isNullOrBlank()) return "Unexpected error"
        val json = Json { ignoreUnknownKeys = true }
        val errorDto = json.decodeFromString<ErrorDto>(errorBody)
        errorDto.statusMessage ?: "Unexpected error"
    } catch (e: Exception) {
        "Unexpected error"
    }
}
