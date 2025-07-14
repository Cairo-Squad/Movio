package com.cairosquad.repository.search.data_source.remote.dto


import com.cairosquad.entity.Series
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
) {
    fun toEntity(): Series {
        return Series(
            id = id ?: 0L,
            title = name ?: "",
            rating = voteAverage?.toFloat() ?: 0f,
            posterPath = posterPath ?: "",
        )
    }
}