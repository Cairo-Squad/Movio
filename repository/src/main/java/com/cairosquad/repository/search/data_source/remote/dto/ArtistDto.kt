package com.cairosquad.repository.search.data_source.remote.dto


import com.cairosquad.entity.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    @SerialName("adult")
    val adult: Boolean? = null,
    @SerialName("gender")
    val gender: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("known_for")
    val knownFor: List<MediaDto?>? = null
) {
    fun toArtist(): Artist {
        return Artist(
            id = id?.toLong() ?: 0L,
            name = name ?: "",
            photoPath = profilePath ?: ""
        )
    }
}