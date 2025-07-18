package com.cairosquad.repository.movie.data_source.remote.dto


import com.cairosquad.entity.Review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRemoteDto(
    @SerialName("author")
    val author: String? = null,
    @SerialName("author_details")
    val authorDetails: AuthorDetailsDto? = null,
    @SerialName("content")
    val content: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Long,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("url")
    val url: String? = null
) {
    fun toEntity() = Review(
        id = id,
        author = author.orEmpty(),
        authorPhotoPath = authorDetails?.avatarPath.orEmpty(),
        rating = authorDetails?.rating.toString(),
        date = createdAt.orEmpty().toLong(),
        description = content.orEmpty()
    )
}