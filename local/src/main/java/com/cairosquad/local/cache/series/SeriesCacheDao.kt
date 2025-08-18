package com.cairosquad.local.cache.series

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeSeriesCacheCrossRef
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesGenreCacheCrossRef
import com.cairosquad.repository.series.data_source.local.dto.SeriesWithoutGenreCacheDto

@Dao
interface SeriesCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForCacheCodeAndSeriesCache(crossRef: List<CacheCodeSeriesCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForSeriesAndGenreCache(crossRef: List<SeriesGenreCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeriesWithoutGenre(series: List<SeriesWithoutGenreCacheDto>)

    @Query("Delete from SeriesWithoutGenreCacheDto where cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredSeriesWithoutGenreCache(expirationTime: Long)

    @Query("Delete from CacheCodeSeriesCacheCrossRef " +
            "where " +
                "Not series_id_language in (Select series_id_language from SeriesWithoutGenreCacheDto) " +
             "OR " +
                "Not cacheCode in (Select cacheCode from CacheCodeDto)")
    suspend fun deleteCrossRefForNonExistingCacheCodeAndSeriesCache()

    @Query("Delete from SeriesGenreCacheCrossRef " +
            "where " +
                "Not series_id_language in (Select series_id_language from SeriesWithoutGenreCacheDto) " +
             "OR " +
                "Not genre_id_language in (Select genre_id_language from SeriesGenreCacheCrossRef)")
    suspend fun deleteCrossRefForNonExistingSeriesAndGenreCache()

    @Transaction
    @Query("Select * From CacheCodeDto where cacheCode = :cacheCode")
    suspend fun getSeriesByCacheCode(cacheCode: String): CacheCodeWithSeriesCacheDto?
}