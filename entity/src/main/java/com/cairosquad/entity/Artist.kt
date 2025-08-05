package com.cairosquad.entity

data class Artist(
    val id: Long,
    val name: String,
    val photoPath: String,
    val country: String = "",
    val birthDate: Long? = null,
    val biography: String = "",
    val department: String = "",
)