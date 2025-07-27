package com.cairosquad.local.cache.series

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cairosquad.repository.series.data_source.local.dto.RequestSeriesCacheCrossRef
import com.cairosquad.repository.series.data_source.local.dto.RequestWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesGenreCacheCrossRef
import com.cairosquad.repository.series.data_source.local.dto.SeriesWithoutGenreCacheDto

@Dao
interface SeriesCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForRequestAndSeriesCache(mappings: List<RequestSeriesCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForSeriesAndGenreCache(mappings: List<SeriesGenreCacheCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeriesWithoutGenre(series: List<SeriesWithoutGenreCacheDto>)

    @Query("Delete from SeriesWithoutGenreCacheDto where cachingTimestamp < :expirationTime")
    suspend fun deleteExpiredSeriesWithoutGenreCache(expirationTime: Long)

    @Query("Delete from RequestSeriesCacheCrossRef " +
            "where " +
                "Not series_id in (Select series_id from SeriesWithoutGenreCacheDto) " +
             "OR " +
                "Not request in (Select request from RequestCacheDto)")
    suspend fun deleteCrossRefForNonExistingRequestAndSeriesCache()

    @Query("Delete from SeriesGenreCacheCrossRef " +
            "where " +
                "Not series_id in (Select series_id from SeriesWithoutGenreCacheDto) " +
             "OR " +
                "Not genre_id in (Select genre_id from SeriesGenreCacheCrossRef)")
    suspend fun deleteCrossRefForNonExistingSeriesAndGenreCache()

    @Transaction
    @Query("Select * From RequestCacheDto where request = :request")
    suspend fun getSeriesByRequest(request: String): RequestWithSeriesCacheDto?
}