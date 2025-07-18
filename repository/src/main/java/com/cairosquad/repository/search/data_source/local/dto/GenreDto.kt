package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "genres",
    indices = [Index(value = ["genre_name"], unique = true)]
)
data class GenreDto(
    @PrimaryKey
    @ColumnInfo(name = "genre_id")
    val id: Long,

    @ColumnInfo(name = "genre_name")
    val name: String,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "click_count")
    var clickCount: Int = 0,
)