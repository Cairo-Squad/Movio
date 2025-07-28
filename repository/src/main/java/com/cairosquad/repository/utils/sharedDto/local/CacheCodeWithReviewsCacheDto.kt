package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CacheCodeWithReviewsCacheDto(
    @Embedded
    val cacheCode: CacheCodeDto,
    @Relation(
        parentColumn = "cacheCode",
        entityColumn = "review_id",
        associateBy = Junction(CacheCodeReviewCacheCrossRef::class)
    )
    val reviews: List<ReviewCacheDto>,
)
