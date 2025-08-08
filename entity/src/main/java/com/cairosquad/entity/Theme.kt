package com.cairosquad.entity

enum class Theme {
    LIGHT,
    DARK;
    companion object {
        fun fromString(theme: String) = when (theme) {
            "light" -> LIGHT
            "dark" -> DARK
            else -> DARK
        }
    }
}