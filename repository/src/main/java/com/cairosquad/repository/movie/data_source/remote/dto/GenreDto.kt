package com.cairosquad.repository.movie.data_source.remote.dto


import com.cairosquad.entity.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String? = null
) {
    fun toEntity(): Genre {
        return Genre(
            id = id?.toLong() ?: 0L,
            name = name.orEmpty()
        )
    }
}