package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "RequestReviewCacheCrossRef",
    primaryKeys = ["request", "review_id"]
)
data class RequestReviewCacheCrossRef(
    @ColumnInfo(name = "request")
    val request: String,
    @ColumnInfo(name = "review_id")
    val reviewId: String,
) {
    companion object {
        fun fromRequestAndReviewList(
            request: RequestCacheDto,
            reviews: List<ReviewCacheDto>
        ): List<RequestReviewCacheCrossRef> {
            return reviews.map { RequestReviewCacheCrossRef(request.request, it.id) }
        }
    }
}