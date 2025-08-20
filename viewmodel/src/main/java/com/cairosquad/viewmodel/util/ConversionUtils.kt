package com.cairosquad.viewmodel.util

import java.util.Locale

fun Float.roundToFirstDecimalPlace(): Float {
    return try {
        "%.1f".format(this).toFloat()
    } catch (_: Exception) {
        this.times(10).toInt().times(0.1f)
    }
}

fun Int.toLocalString(): String {
    return "%d".format(this, Locale.getDefault())
}

fun Float.toLocalString(numberOfDecimals: Int = 1): String {
    return "%.${numberOfDecimals}f".format(this, Locale.getDefault())
}

fun String.localizeNumbers(): String {
    val characters = this.split("")
    var result = ""
    characters.forEach { character ->
        val isNumber = character.toIntOrNull() != null
        result += if (isNumber) {
            character.toInt().toLocalString()
        } else {
            character
        }
    }

    return result
}