package com.cairosquad.viewmodel.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object TimeUtil {

    /**
     * Convert time to date in `MM/dd/yyyy` format
     *
     * @param time time in milliseconds
     * @return date formated as a string, e.g., `06/16/1999`
     */
    internal fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return format.format(date)
    }

    /**
     * Convert time to year in `yyyy` format
     *
     * @param time time in milliseconds
     * @return year as a string, e.g., `1999`
     */
    internal fun convertLongToYear(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy", Locale.getDefault())
        return format.format(date)
    }

    /**
     * Convert time to year in `MMMM dd, yyyy` format
     *
     * @param time time in milliseconds
     * @return named date as a string, e.g., `June 14, 2025`
     */
    internal fun convertLongToNamedDate(time: Long?): String {
        if (time == null) return ""
        val date = Date(time)
        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    internal fun convertIntToHourMinuteFormat(time: Int): String {

        val hours = "%d".format(time / 60, Locale.getDefault())
        val minutes = "%d".format(time % 60, Locale.getDefault())

        if (Locale.getDefault().language == "ar") {
            return "$hours س $minutes د"
        }

        return "${hours}h ${minutes}min"
    }
}