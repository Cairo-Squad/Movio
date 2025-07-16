package com.cairosquad.local.search.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_QUERY_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_QUERY_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_PAGE_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_PAGE_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_PAGE_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_QUERY_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto

@Dao
interface CacheDao {
    @Query("SELECT * FROM $CACHED_MOVIES_TABLE_NAME WHERE $CACHED_MOVIES_QUERY_COLUMN_NAME = :query AND $CACHED_MOVIES_PAGE_COLUMN_NAME  = :page ORDER BY id")
    suspend fun getCachedMovies(query: String,page: Int): List<MovieCacheDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMovies(results: List<MovieCacheDto>)

    @Query("DELETE FROM $CACHED_MOVIES_TABLE_NAME WHERE $CACHED_MOVIES_TIMESTAMP_COLUMN_NAME < :expirationTime")
    suspend fun deleteExpiredMoviesCache(expirationTime: Long)


    //@Query("SELECT * FROM $CACHED_SERIES_TABLE_NAME where $CACHED_SERIES_QUERY_COLUMN_NAME = :query")
  //  suspend fun getCachedSeries(query: String): List<SeriesCacheDto>

    @Query("SELECT * FROM $CACHED_SERIES_TABLE_NAME WHERE $CACHED_SERIES_QUERY_COLUMN_NAME = :query AND $CACHED_SERIES_PAGE_COLUMN_NAME = :page ORDER BY id")
    suspend fun getCachedSeries(query: String, page: Int): List<SeriesCacheDto>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheSeries(results: List<SeriesCacheDto>)

    @Query("DELETE FROM $CACHED_SERIES_TABLE_NAME WHERE $CACHED_SERIES_TIMESTAMP_COLUMN_NAME < :expirationTime")
    suspend fun deleteExpiredSeriesCache(expirationTime: Long)


//    @Query("SELECT * FROM $CACHED_ARTIST_TABLE_NAME where $CACHED_ARTIST_QUERY_COLUMN_NAME = :query")
//    suspend fun getCachedArtist(query: String): List<ArtistCacheDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheArtist(results: List<ArtistCacheDto>)
    @Query("SELECT * FROM $CACHED_ARTIST_TABLE_NAME WHERE $CACHED_ARTIST_QUERY_COLUMN_NAME = :query AND $CACHED_ARTIST_PAGE_COLUMN_NAME = :page ORDER BY id")
    suspend fun getCachedArtist(query: String, page: Int): List<ArtistCacheDto>
    @Query("DELETE FROM $CACHED_ARTIST_TABLE_NAME WHERE $CACHED_ARTIST_TIMESTAMP_COLUMN_NAME < :expirationTime")
    suspend fun deleteExpiredArtistCache(expirationTime: Long)
}