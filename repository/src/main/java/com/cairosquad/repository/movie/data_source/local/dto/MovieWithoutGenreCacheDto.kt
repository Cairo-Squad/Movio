package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "MovieWithoutGenreCacheDto")
data class MovieWithoutGenreCacheDto(
    @ColumnInfo(name = "movie_id_language")
    @PrimaryKey
    val movieIdWithLanguage: String,
    @ColumnInfo(name = "movie_id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: Long,
    @ColumnInfo(name = "runtime")
    val runtime: Int?,
    @ColumnInfo(name = "trailerPath")
    val trailerPath: String,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long = Date().time,
)
