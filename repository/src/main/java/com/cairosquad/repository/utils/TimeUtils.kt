package com.cairosquad.repository.common

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    /**
     * Converts a date string in "yyyy-MM-dd" format to epoch milliseconds.
     */
    fun dateToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString).time
    }

    /**
     * Converts a date string in ISO 8601 format (e.g., "2018-09-06T19:31:51.288Z")
     * to epoch milliseconds.
     */
    fun isoDateToLong(isoDateString: String): Long {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
        return isoFormat.parse(isoDateString).time
    }
}