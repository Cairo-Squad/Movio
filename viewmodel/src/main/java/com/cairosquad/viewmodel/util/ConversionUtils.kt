package com.cairosquad.viewmodel.util

fun Float.roundToFirstDecimalPlace(): Float {
    return "%.1f".format(this).toFloat()
}
