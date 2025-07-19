package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CACHED_MOVIES_TABLE_NAME)
data class MovieCacheDto(
    @ColumnInfo(name = CACHED_MOVIES_ID_COLUMN_NAME)
    @PrimaryKey
    val id: Int ,
    @ColumnInfo(name = CACHED_MOVIES_PAGE_COLUMN_NAME)
    val page: Int,
    @ColumnInfo(name = CACHED_MOVIES_TITLE_COLUMN_NAME)
    val title: String?,
    @ColumnInfo(name = "posterPath")
    val posterPath: String?,
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double?,
    @ColumnInfo(name = CACHED_MOVIES_QUERY_COLUMN_NAME)
    val query: String,
    @ColumnInfo(name = CACHED_MOVIES_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,
)
