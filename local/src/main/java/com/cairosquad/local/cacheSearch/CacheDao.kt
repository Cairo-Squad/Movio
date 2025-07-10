package com.cairosquad.local.cacheSearch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.local.cacheSearch.entity.ArtistCacheEntity
import com.cairosquad.local.cacheSearch.entity.MovieCacheEntity
import com.cairosquad.local.cacheSearch.entity.SeriesCacheEntity

@Dao
interface CacheDao {
    @Query("SELECT * FROM MovieCacheEntity where queryColumn = :query")
    suspend fun getCachedMovies(query: String): List<MovieCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMovies(results: List<MovieCacheEntity>)

    @Query("DELETE FROM MovieCacheEntity WHERE timestamp < :expirationTime")
    suspend fun deleteExpiredMoviesCache(expirationTime: Long)

    @Query("SELECT * FROM SeriesCacheEntity where queryColumn = :query")
    suspend fun getCachedSeries(query: String): List<SeriesCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheSeries(results: List<SeriesCacheEntity>)

    @Query("DELETE FROM SeriesCacheEntity WHERE timestamp < :expirationTime")
    suspend fun deleteExpiredSeriesCache(expirationTime: Long)

    @Query("SELECT * FROM ArtistCacheEntity where queryColumn = :query")
    suspend fun getCachedArtist(query: String): List<ArtistCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheArtist(results: List<ArtistCacheEntity>)

    @Query("DELETE FROM ArtistCacheEntity WHERE timestamp < :expirationTime")
    suspend fun deleteExpiredArtistCache(expirationTime: Long)
}