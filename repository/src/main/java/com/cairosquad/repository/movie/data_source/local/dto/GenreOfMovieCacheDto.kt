package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "GenreOfMovieCacheDto")
data class GenreOfMovieCacheDto(
    @PrimaryKey
    @ColumnInfo(name = "genre_id_language")
    val genreIdWithLanguage: String,
    @ColumnInfo(name = "genre_id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long = Date().time,
)