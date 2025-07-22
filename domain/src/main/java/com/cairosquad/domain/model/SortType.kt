package com.cairosquad.domain.model

enum class SortType(val sortBy: String) {
    POPULAR("popularity.desc"),
    LATEST("primary_release_date.desc");
}