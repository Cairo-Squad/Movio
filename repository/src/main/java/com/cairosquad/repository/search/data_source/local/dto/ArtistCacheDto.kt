package com.cairosquad.repository.search.data_source.local.Dto

import com.cairosquad.entity.Artist

data class ArtistCacheDto(
    val id: Int = 0,
    val name: String? = null,
    val photoPath: String? = null
) {
    fun toArtist(): Artist {
        return Artist(
            id = id.toLong(),
            name = name ?: "",
            photoPath = photoPath ?: "",
        )
    }
}
