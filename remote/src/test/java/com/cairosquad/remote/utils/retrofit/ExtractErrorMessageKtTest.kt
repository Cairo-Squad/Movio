package com.cairosquad.remote.utils.retrofit

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ExtractErrorMessageTest {

    @Test
    fun `extractErrorMessage should return message from valid error body`() {
        // Given
        val json = """{"status_message":"Invalid token"}"""

        // When
        val result = extractErrorMessage(json)

        // Then
        assertThat(result).isEqualTo("Invalid token")
    }

    @Test
    fun `extractErrorMessage should return empty space if statusMessage is null`() {
        // Given
        val json = """{"status_message":null}"""

        // When
        val result = extractErrorMessage(json)

        // Then
        assertThat(result).isEqualTo(" ")
    }

    @Test
    fun `extractErrorMessage should return Unexpected error for null body`() {
        // When
        val result = extractErrorMessage(null)

        // Then
        assertThat(result).isEqualTo("Unexpected error")
    }

    @Test
    fun `extractErrorMessage should return Unexpected error for blank string`() {
        // When
        val result = extractErrorMessage(" ")

        // Then
        assertThat(result).isEqualTo("Unexpected error")
    }

    @Test(expected = Exception::class)
    fun `extractErrorMessage should throw exception for invalid JSON`() {
        // Given
        val invalidJson = """{invalid_json"""

        // When
        extractErrorMessage(invalidJson)

        // Then: should throw an exception
    }
}