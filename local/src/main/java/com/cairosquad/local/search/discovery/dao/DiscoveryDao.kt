package com.cairosquad.local.search.discovery.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.search.data_source.local.dto.PersonalizedMoviesIdsDto
import com.cairosquad.repository.search.data_source.local.dto.SuggestedMoviesIdsDto
import com.cairosquad.repository.search.data_source.local.dto.CACHED_ARTIST_TIMESTAMP_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_ID_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_MOVIES_TABLE_NAME
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto

@Dao
interface DiscoveryDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun cachePersonalizedMoviesIds(moviesIds: List<PersonalizedMoviesIdsDto>)

    @Query(
        "SELECT * FROM $CACHED_MOVIES_TABLE_NAME " +
            "where $CACHED_MOVIES_ID_COLUMN_NAME " +
            "IN (SELECT movie_id FROM PersonalizedMoviesIdsDto)"
    )
    suspend fun getPersonalizedMoviesIds(): List<MovieCacheDto>

    @Query(
        "DELETE FROM PersonalizedMoviesIdsDto " +
                "WHERE movie_id IN " +
                "(SELECT id FROM MovieCacheDto " +
                "WHERE $CACHED_ARTIST_TIMESTAMP_COLUMN_NAME < :expirationTime)"
    )
    suspend fun deleteExpiredPersonalizedMoviesId(expirationTime: Long)


    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun cacheSuggestedMovies(moviesIds: List<SuggestedMoviesIdsDto>)


    @Query(
        "SELECT * FROM $CACHED_MOVIES_TABLE_NAME " +
                "where $CACHED_MOVIES_ID_COLUMN_NAME " +
                "IN (SELECT movie_id FROM SuggestedMoviesIdsDto)"
    )    suspend fun getSuggestedMovies(): List<MovieCacheDto>

    @Query(
        "DELETE FROM SuggestedMoviesIdsDto " +
                "WHERE movie_id IN " +
                "(SELECT id FROM MovieCacheDto " +
                "WHERE $CACHED_ARTIST_TIMESTAMP_COLUMN_NAME < :expirationTime)"
    )
    suspend fun deleteExpiredSuggestedMoviesId(expirationTime: Long)

}