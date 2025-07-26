package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RequestWithReviewsCacheDto(
    @Embedded
    val request: RequestCacheDto,
    @Relation(
        parentColumn = "request",
        entityColumn = "review_id",
        associateBy = Junction(RequestReviewCacheCrossRef::class)
    )
    val reviews: List<ReviewCacheDto>,
)

fun getRequestOfMovieReviews(page: Int, movieId: Long): String {
    return "movies/tobRated/page = $page/movieId = $movieId"
}