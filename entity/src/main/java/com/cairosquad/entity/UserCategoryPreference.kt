package com.cairosquad.entity

data class UserCategoryPreference (
    val categoryId: String,
    val categoryName: String,
    var clickCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)