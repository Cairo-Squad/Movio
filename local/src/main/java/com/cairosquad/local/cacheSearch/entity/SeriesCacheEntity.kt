package com.cairosquad.local.cacheSearch.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SeriesCacheEntity")
data class SeriesCacheEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "queryColumn")
    val query: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,
)