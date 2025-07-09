package com.cairosquad.repository.search.dataSource.remote.search.dto


import com.cairosquad.entity.Artist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    @SerialName("adult")
    val adult: Boolean?,
    @SerialName("gender")
    val gender: Int?,
    @SerialName("id")
    val id: Int?,
    @SerialName("known_for_department")
    val knownForDepartment: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("original_name")
    val originalName: String?,
    @SerialName("popularity")
    val popularity: Double?,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("known_for")
    val knownFor: List<MediaDto?>?
) {
    fun toArtist(): Artist {
        return Artist(
            id = id?.toLong() ?: 0L,
            name = name ?: "",
            photoPath = profilePath ?: ""
        )
    }
}