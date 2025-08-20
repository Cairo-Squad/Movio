package com.cairosquad.local.cache.series

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeEpisodeCrossRef
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeSeasonCrossRef
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithEpisodesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeasonsCacheDto
import com.cairosquad.repository.series.data_source.local.dto.EpisodeCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeasonCacheDto

@Dao
interface SeasonEpisodeCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeasons(seasons: List<SeasonCacheDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeCacheDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForCacheCodeAndSeason(crossRef: List<CacheCodeSeasonCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefForCacheCodeAndEpisode(crossRef: List<CacheCodeEpisodeCrossRef>)

    @Query("DELETE FROM SeasonCacheDto WHERE caching_timestamp < :expirationTime")
    suspend fun deleteExpiredSeasonCache(expirationTime: Long)

    @Query("DELETE FROM EpisodeCacheDto WHERE caching_timestamp < :expirationTime")
    suspend fun deleteExpiredEpisodeCache(expirationTime: Long)

    @Query(
        "DELETE FROM CacheCodeSeasonCrossRef " +
                "WHERE " +
                "NOT season_Id_language IN (SELECT season_id_language FROM SeasonCacheDto) " +
                "OR NOT cacheCode IN (SELECT cacheCode FROM CacheCodeDto)"
    )
    suspend fun deleteCrossRefForNonExistingCacheCodeAndSeason()

    @Query(
        "DELETE FROM CacheCodeEpisodeCrossRef " +
                "WHERE " +
                "NOT episode_Id_language IN (SELECT episode_id_language FROM EpisodeCacheDto) " +
                "OR NOT cacheCode IN (SELECT cacheCode FROM CacheCodeDto)"
    )
    suspend fun deleteCrossRefForNonExistingCacheCodeAndEpisode()

    @Transaction
    @Query("SELECT * FROM CacheCodeDto WHERE cacheCode = :cacheCode")
    suspend fun getSeasonsByCacheCode(cacheCode: String): CacheCodeWithSeasonsCacheDto?

    @Transaction
    @Query("SELECT * FROM CacheCodeDto WHERE cacheCode = :cacheCode")
    suspend fun getEpisodesByCacheCode(cacheCode: String): CacheCodeWithEpisodesCacheDto?
}