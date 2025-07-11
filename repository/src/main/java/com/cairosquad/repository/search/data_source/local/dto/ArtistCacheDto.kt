package com.cairosquad.repository.search.data_source.local.Dto

data class ArtistCacheDto(
    val id: Int = 0,
    val name: String? = null,
    val photoPath: String? = null,
    val query: String,
    val timestamp: Long
)
