package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CACHED_MOVIES_TABLE_NAME)
data class CachedMovieDto(
    @ColumnInfo(name = CACHED_MOVIES_ID_COLUMN_NAME)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = CACHED_MOVIES_TITLE_COLUMN_NAME)
    val title: String?,
    @ColumnInfo(name = CACHED_MOVIES_POSTER_PATH_COLUMN_NAME)
    val posterPath: String?,
    @ColumnInfo(name = CACHED_MOVIES_VOTE_AVERAGE_COLUMN_NAME)
    val voteAverage: Double?,
    @ColumnInfo(name = CACHED_MOVIES_QUERY_COLUMN_NAME)
    val query: String,
    @ColumnInfo(name = CACHED_MOVIES_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,
)
