package com.cairosquad.local.cache.reviews

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.utils.sharedDto.local.RequestReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.RequestWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewCacheDto>)

    @Query("Delete from ReviewCacheDto where cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredReviewCache(expirationTime: Long)

    @Query("Select * From RequestCacheDto where request = :request")
    suspend fun getReviewsByRequest(request: String): RequestWithReviewsCacheDto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestReviewCacheCrossRef(mappings: List<RequestReviewCacheCrossRef>)

    @Query(
        "Delete from RequestReviewCacheCrossRef " +
        "where " +
                "Not review_id in (Select review_id from ReviewCacheDto) " +
            "OR " +
                "Not request in (Select request from RequestCacheDto)"
    )
    suspend fun deleteUnwantedRequestReviewCacheCrossRef()
}