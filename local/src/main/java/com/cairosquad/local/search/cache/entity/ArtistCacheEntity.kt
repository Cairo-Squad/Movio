package com.cairosquad.local.search.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistCacheEntity")
data class ArtistCacheEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "queryColumn")
    val query: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "photoPath")
    val photoPath: String?,
)