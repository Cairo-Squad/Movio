package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "CacheCodeReviewCacheCrossRef",
    primaryKeys = ["cacheCode", "review_id"]
)
data class CacheCodeReviewCacheCrossRef(
    @ColumnInfo(name = "cacheCode")
    val cacheCode: String,
    @ColumnInfo(name = "review_id")
    val reviewId: String,
) {
    companion object {
        fun fromRequestAndReviewList(
            cacheCode: CacheCodeDto,
            reviews: List<ReviewCacheDto>
        ): List<CacheCodeReviewCacheCrossRef> {
            return reviews.map { CacheCodeReviewCacheCrossRef(cacheCode.cacheCode, it.id) }
        }
    }
}