package com.cairosquad.local.cache.request

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.utils.RequestCacheDto

@Dao
interface RequestCachedDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertRequestCache(request: RequestCacheDto)

    @Query("Delete from RequestCacheDto where timestamp < :expirationTime")
    suspend fun deleteExpiredRequestCache(expirationTime: Long)
}