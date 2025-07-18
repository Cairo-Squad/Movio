package com.cairosquad.entity

data class Review(
    val id: String,
    val author: String,
    val authorPhotoPath: String,
    val rating: String,
    val date: Long,
    val description: String,
)