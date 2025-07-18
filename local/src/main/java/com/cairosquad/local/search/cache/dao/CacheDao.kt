package com.cairosquad.local.search.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.artists.dto.ArtistMovieCachedDto
import com.cairosquad.repository.artists.dto.ArtistSeriesCachedDto
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_ID_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_NAME_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_ID_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_TITLE_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_ID_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_NAME_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SERIES_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto

@Dao
interface CacheDao {

    @Query("SELECT * FROM $CACHED_MOVIES_TABLE_NAME")
    suspend fun getCachedMovies(): List<MovieCacheDto>

    @Query("SELECT * FROM $CACHED_MOVIES_TABLE_NAME where $CACHED_MOVIES_ID_COLUMN_NAME = :id")
    suspend fun getCachedMovies(id: Long): List<MovieCacheDto>

    @Query("SELECT * FROM $CACHED_MOVIES_TABLE_NAME where $CACHED_MOVIES_TITLE_COLUMN_NAME LIKE '%' || :query || '%'")
    suspend fun getCachedMovies(query: String): List<MovieCacheDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMovies(results: List<MovieCacheDto>)

    @Query("DELETE FROM $CACHED_MOVIES_TABLE_NAME WHERE $CACHED_MOVIES_TIMESTAMP_COLUMN_NAME < :expirationTime")
    suspend fun deleteExpiredMoviesCache(expirationTime: Long)



    @Query("SELECT * FROM $CACHED_SERIES_TABLE_NAME")
    suspend fun getCachedSeries(): List<SeriesCacheDto>

    @Query("SELECT * FROM $CACHED_SERIES_TABLE_NAME where $CACHED_SERIES_ID_COLUMN_NAME = :id")
    suspend fun getCachedSeries(id: Long): List<SeriesCacheDto>

    @Query("SELECT * FROM $CACHED_SERIES_TABLE_NAME where $CACHED_SERIES_NAME_COLUMN_NAME LIKE '%' || :query || '%'")
    suspend fun getCachedSeries(query: String): List<SeriesCacheDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheSeries(results: List<SeriesCacheDto>)

    @Query("DELETE FROM $CACHED_SERIES_TABLE_NAME WHERE $CACHED_SERIES_TIMESTAMP_COLUMN_NAME < :expirationTime")
    suspend fun deleteExpiredSeriesCache(expirationTime: Long)



    @Query("SELECT * FROM $CACHED_ARTIST_TABLE_NAME")
    suspend fun getCachedArtist(): List<ArtistCacheDto>

    @Query("SELECT * FROM $CACHED_ARTIST_TABLE_NAME where $CACHED_ARTIST_ID_COLUMN_NAME = :id")
    suspend fun getCachedArtist(id: Long): ArtistCacheDto

    @Query("SELECT * FROM $CACHED_ARTIST_TABLE_NAME where $CACHED_ARTIST_NAME_COLUMN_NAME LIKE '%' || :query || '%'")
    suspend fun getCachedArtist(query: String): List<ArtistCacheDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheArtist(results: List<ArtistCacheDto>)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheArtistMovies(results: List<ArtistMovieCachedDto>)

    @Query("SELECT * FROM $CACHED_MOVIES_TABLE_NAME WHERE $CACHED_MOVIES_ID_COLUMN_NAME IN (SELECT movieId FROM ArtistMovieCachedDto WHERE artistId = :artistId)")
    suspend fun getCachedArtistMovies(artistId: Long): List<MovieCacheDto>

    @Query("DELETE FROM ArtistMovieCachedDto " +
            "WHERE " +
            "(" +
                "movieId IN (SELECT $CACHED_MOVIES_ID_COLUMN_NAME FROM $CACHED_MOVIES_TABLE_NAME WHERE $CACHED_MOVIES_TIMESTAMP_COLUMN_NAME < :expirationTime)" +
            "or " +
                "artistId In (SELECT $CACHED_ARTIST_ID_COLUMN_NAME FROM $CACHED_ARTIST_TABLE_NAME WHERE $CACHED_ARTIST_TIMESTAMP_COLUMN_NAME < :expirationTime)" +
            ")"
    )
    suspend fun deleteExpiredArtistMoviesCache(expirationTime: Long)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheArtistSeries(results: List<ArtistSeriesCachedDto>)

    @Query("SELECT * FROM $CACHED_SERIES_TABLE_NAME WHERE $CACHED_SERIES_ID_COLUMN_NAME IN (SELECT seriesId FROM ArtistSeriesCachedDto WHERE artistId = :artistId)")
    suspend fun getCachedArtistSeries(artistId: Long): List<SeriesCacheDto>

    @Query("DELETE FROM ArtistSeriesCachedDto " +
            "WHERE " +
            "(" +
            "seriesId IN (SELECT $CACHED_SERIES_ID_COLUMN_NAME FROM $CACHED_SERIES_TABLE_NAME WHERE $CACHED_SERIES_TIMESTAMP_COLUMN_NAME < :expirationTime)" +
            "or " +
            "artistId In (SELECT $CACHED_ARTIST_ID_COLUMN_NAME FROM $CACHED_ARTIST_TABLE_NAME WHERE $CACHED_ARTIST_TIMESTAMP_COLUMN_NAME < :expirationTime)" +
            ")"
    )
    suspend fun deleteExpiredArtistSeriesCache(expirationTime: Long)

    @Query("DELETE FROM $CACHED_ARTIST_TABLE_NAME WHERE $CACHED_ARTIST_TIMESTAMP_COLUMN_NAME < :expirationTime")
    suspend fun deleteExpiredArtistCache(expirationTime: Long)
}