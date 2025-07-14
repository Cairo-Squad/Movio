package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CACHED_SERIES_TABLE_NAME)
data class CachedSeriesDto(
    @ColumnInfo(name = CACHED_SERIES_ID_COLUMN_NAME)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = CACHED_SERIES_NAME_COLUMN_NAME)
    val name: String?,
    @ColumnInfo(name = CACHED_SERIES_POSTER_PATH_COLUMN_NAME)
    val posterPath: String?,
    @ColumnInfo(name = CACHED_SERIES_VOTE_AVERAGE_COLUMN_NAME)
    val voteAverage: Double?,
    @ColumnInfo(name = CACHED_SERIES_QUERY_COLUMN_NAME)
    val query: String,
    @ColumnInfo(name = CACHED_SERIES_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,
)