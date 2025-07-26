package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "GenreOfMovieCacheDto")
data class GenreOfMovieCacheDto(
    @ColumnInfo(name = "genre_id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "timestamp")
    val cachingTimestamp: Long = Date().time,
)