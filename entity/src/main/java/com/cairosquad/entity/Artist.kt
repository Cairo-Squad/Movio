package com.cairosquad.entity

data class Artist(
    val id: Long,
    val name: String,
    val photoPath: String,
    val country: String = "", // TODO: remove default value
    val birthDate: Long = 0, // TODO: remove default value
    val biography: String = "", // TODO: remove default value
    val department: String = "", // TODO: remove default value
)