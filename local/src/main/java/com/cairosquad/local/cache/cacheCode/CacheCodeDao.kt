package com.cairosquad.local.cache.cacheCode

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

@Dao
interface CacheCodeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCacheCode(request: CacheCodeDto)

    @Query("Delete from CacheCodeDto where cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredCacheCode(expirationTime: Long)
}