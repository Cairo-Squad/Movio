package com.cairosquad.repository.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TimeUtilsTest {

    @Test
    fun `dateToLong parses valid yyyy-MM-dd date correctly`() {
        val dateString = "2025-08-20"
        val expected = SimpleDateFormat("yyyy-MM-dd").parse(dateString).time

        val result = TimeUtils.dateToLong(dateString)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `isoDateToLong parses valid ISO 8601 date correctly`() {
        val isoDateString = "2025-08-20T14:30:00.000Z"
        val isoFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
        val expected = isoFormat.parse(isoDateString).time

        val result = TimeUtils.isoDateToLong(isoDateString)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `dateToLong throws ParseException on invalid date format`() {
        val invalidDate = "20/08/2025"
        assertThrows<ParseException> {
            TimeUtils.dateToLong(invalidDate)
        }
    }

    @Test
    fun `isoDateToLong throws ParseException on invalid ISO format`() {
        val invalidIsoDate = "2025/08/20 14:30"
        assertThrows<ParseException> {
            TimeUtils.isoDateToLong(invalidIsoDate)

        }
    }

    fun `dateToLong throws ParseException on empty string`() {
        assertThrows<ParseException> {
            TimeUtils.dateToLong("")
        }
    }

    fun `isoDateToLong throws ParseException on empty string`() {
        assertThrows<ParseException> {
            TimeUtils.isoDateToLong("")
        }
    }
}