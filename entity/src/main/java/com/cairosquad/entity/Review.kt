package com.cairosquad.entity

data class Review(
    val id: Long,
    val author: String,
    val authorPhotoPath: String,
    val rating: String,
    val date: Long,
    val description: String,
)