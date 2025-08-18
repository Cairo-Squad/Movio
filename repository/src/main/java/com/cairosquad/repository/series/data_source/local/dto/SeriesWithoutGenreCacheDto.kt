package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "SeriesWithoutGenreCacheDto")
data class SeriesWithoutGenreCacheDto(
    @PrimaryKey
    @ColumnInfo(name = "series_id_language")
    val seriesIdWithLanguage: String,
    @ColumnInfo(name = "series_id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "posterPath")
    val posterPath: String,
    @ColumnInfo(name = "trailerPath")
    val trailerPath: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "releaseDate")
    val releaseDate: Long,
    @ColumnInfo(name = "seasonsCount")
    val seasonsCount: Int,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long = Date().time,
)