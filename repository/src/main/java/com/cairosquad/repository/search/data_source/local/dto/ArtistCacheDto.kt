package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CACHED_ARTIST_TABLE_NAME)
data class ArtistCacheDto(
    @ColumnInfo(name = CACHED_ARTIST_ID_COLUMN_NAME)
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = CACHED_ARTIST_PAGE_COLUMN_NAME)
    val page: Int,
    @ColumnInfo(name = CACHED_ARTIST_NAME_COLUMN_NAME)
    val name: String?,
    @ColumnInfo(name = "photoPath")
    val photoPath: String?,
    @ColumnInfo(name = CACHED_ARTIST_QUERY_COLUMN_NAME)
    val query: String,
    @ColumnInfo(name = CACHED_ARTIST_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,
)
