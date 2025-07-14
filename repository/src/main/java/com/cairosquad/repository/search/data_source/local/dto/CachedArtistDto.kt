package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CACHED_ARTIST_TABLE_NAME)
data class CachedArtistDto(
    @ColumnInfo(name = CACHED_ARTIST_ID_COLUMN_NAME)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = CACHED_ARTIST_NAME_COLUMN_NAME)
    val name: String?,
    @ColumnInfo(name = CACHED_ARTIST_PHOTO_PATH_COLUMN_NAME)
    val photoPath: String?,
    @ColumnInfo(name = CACHED_ARTIST_QUERY_COLUMN_NAME)
    val query: String,
    @ColumnInfo(name = CACHED_ARTIST_TIMESTAMP_COLUMN_NAME)
    val timestamp: Long,
)
