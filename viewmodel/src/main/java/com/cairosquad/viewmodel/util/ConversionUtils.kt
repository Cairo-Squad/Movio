package com.cairosquad.viewmodel.util

fun Float.roundToFirstDecimalPlace(): Float {
    return try {
        "%.1f".format(this).toFloat()
    } catch (_: Exception) {
        this.times(10).toInt().times(0.1f)
    }
}
