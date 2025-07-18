package com.cairosquad.viewmodel.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TimeUtilTest {

    private fun getTimeInMillis(dateString: String, pattern: String = "yyyy-MM-dd"): Long {
        val sdf = SimpleDateFormat(pattern, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.parse(dateString)?.time ?: throw IllegalArgumentException("Invalid date")
    }

    @Test
    fun `convertLongToTime SHOULD return date in MM_dd_yyyy format`() {
        val time = getTimeInMillis("1999-06-16")
        val result = TimeUtil.convertLongToTime(time)
        assertThat(result).isEqualTo("06/16/1999")
    }

    @Test
    fun `convertLongToYear SHOULD return year in yyyy format`() {
        val time = getTimeInMillis("2025-07-18")
        val result = TimeUtil.convertLongToYear(time)
        assertThat(result).isEqualTo("2025")
    }

    @Test
    fun `convertLongToNamedDate SHOULD return date in MMMM dd, yyyy format`() {
        val time = getTimeInMillis("2025-07-18")
        val result = TimeUtil.convertLongToNamedDate(time)
        assertThat(result).isEqualTo("July 18, 2025")
    }

    // Edge Cases

    @Test
    fun `convertLongToTime SHOULD handle epoch time`() {
        val time = 0L // Epoch
        val result = TimeUtil.convertLongToTime(time)
        assertThat(result).isEqualTo("01/01/1970")
    }

    @Test
    fun `convertLongToYear SHOULD handle far future year`() {
        val time = getTimeInMillis("3000-01-01")
        val result = TimeUtil.convertLongToYear(time)
        assertThat(result).isEqualTo("3000")
    }

    @Test
    fun `convertLongToNamedDate SHOULD handle far past year`() {
        val time = getTimeInMillis("1000-05-05")
        val result = TimeUtil.convertLongToNamedDate(time)
        assertThat(result).isEqualTo("May 05, 1000")
    }

    @Test
    fun `convertLongToNamedDate SHOULD handle negative timestamp`() {
        val time = getTimeInMillis("1960-12-31") // Negative millis
        val result = TimeUtil.convertLongToNamedDate(time)
        assertThat(result).isEqualTo("December 31, 1960")
    }

    @Test
    fun `convertLongToTime SHOULD handle leap year date`() {
        val time = getTimeInMillis("2016-02-29") // Leap year
        val result = TimeUtil.convertLongToTime(time)
        assertThat(result).isEqualTo("02/29/2016")
    }
}