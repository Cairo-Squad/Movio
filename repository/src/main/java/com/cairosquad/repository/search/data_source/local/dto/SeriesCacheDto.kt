package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CACHED_SERIES_TABLE_NAME)
data class SeriesCacheDto(
    @ColumnInfo(name = CACHED_SERIES_ID_COLUMN_NAME)
    @PrimaryKey
    val id: Int ,
    @ColumnInfo(name = CACHED_SERIES_PAGE_COLUMN_NAME)
    val page: Int,
    @ColumnInfo(name = CACHED_SERIES_NAME_COLUMN_NAME)
    val name: String?,
    @ColumnInfo(name = "posterPath")
    val posterPath: String?,
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double?,
    @ColumnInfo(name = CACHED_SERIES_QUERY_COLUMN_NAME)
    val query: String,
    @ColumnInfo(name = CACHED_SERIES_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,
)