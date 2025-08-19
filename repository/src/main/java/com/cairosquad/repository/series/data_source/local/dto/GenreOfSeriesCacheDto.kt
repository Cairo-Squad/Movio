package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "GenreOfSeriesCacheDto")
data class GenreOfSeriesCacheDto(
    @PrimaryKey
    @ColumnInfo(name = "genre_id_language")
    val genreIdWithLanguage: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "genre_id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long = Date().time,
)