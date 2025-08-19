package com.cairosquad.local.cache.reviews

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewCacheDto>)

    @Query("Delete from ReviewCacheDto where cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredReviewCache(expirationTime: Long)

    @Query("Select * From CacheCodeDto where cacheCode = :cacheCode")
    suspend fun getReviewsByCacheCode(cacheCode: String): CacheCodeWithReviewsCacheDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestReviewCacheCrossRef(crossRef: List<CacheCodeReviewCacheCrossRef>)

    @Query(
        "Delete from CacheCodeReviewCacheCrossRef " +
        "where " +
                "Not review_id_language in (Select review_id_language from ReviewCacheDto) " +
            "OR " +
                "Not cacheCode in (Select cacheCode from CacheCodeDto)"
    )
    suspend fun deleteCrossRefForNonexistingRequestReviewCache()
}