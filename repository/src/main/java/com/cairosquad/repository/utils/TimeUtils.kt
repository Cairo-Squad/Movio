package com.cairosquad.repository.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TimeUtils {


    /**
     * Converts a date string in "yyyy-MM-dd" format to epoch milliseconds.
     */
    fun dateToLong(dateString: String): Long {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.parse(dateString)?.time ?: 0L
        } catch (_: Exception) {
            0L
        }
    }

    /**
     * Converts a date string in ISO 8601 format (e.g., "2018-09-06T19:31:51.288Z")
     * to epoch milliseconds.
     */
    fun isoDateToLong(isoDateString: String): Long {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            isoFormat.parse(isoDateString)?.time ?: 0L
        } catch (_: Exception) {
            0L
        }
    }
}