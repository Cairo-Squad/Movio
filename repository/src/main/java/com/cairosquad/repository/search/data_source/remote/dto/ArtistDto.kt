package com.cairosquad.repository.search.data_source.remote.dto


import com.cairosquad.entity.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
) {
    fun toEntity(): Artist {
        return Artist(
            id = id?.toLong() ?: 0L,
            name = name ?: "",
            photoPath = profilePath ?: ""
        )
    }
}