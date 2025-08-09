package com.cairosquad.viewmodel.main

enum class Theme {
    LIGHT,
    DARK;

    fun toDomain(): com.cairosquad.entity.Theme {
        return when (this) {
            LIGHT -> com.cairosquad.entity.Theme.LIGHT
            DARK -> com.cairosquad.entity.Theme.DARK
        }
    }
}

fun com.cairosquad.entity.Theme.toUi(): Theme {
    return when (this) {
        com.cairosquad.entity.Theme.LIGHT -> Theme.LIGHT
        com.cairosquad.entity.Theme.DARK -> Theme.DARK
    }

}