package com.cairosquad.local.search.recent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.local.search.recent.entity.RecentSearchEntity

@Dao
interface LocalRecentSearchDao {

    @Query("SELECT * FROM recent_search WHERE query_column LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    suspend fun getAllQueries(query: String): List<RecentSearchEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertQuery(entity: RecentSearchEntity)

    @Query("DELETE FROM recent_search")
    suspend fun clearAll()

    @Query("DELETE FROM recent_search WHERE query_column = :query")
    suspend fun deleteQuery(query: String)

    @Query("SELECT * FROM recent_search ORDER BY timestamp DESC")
    suspend fun getAllQueries(): List<RecentSearchEntity>
}