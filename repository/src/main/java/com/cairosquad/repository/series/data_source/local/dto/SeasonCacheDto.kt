package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "SeasonCacheDto")
data class SeasonCacheDto(
    @PrimaryKey
    @ColumnInfo(name = "season_id")
    val id: Long,
    @ColumnInfo(name = "series_id")
    val seriesId: Long,
    @ColumnInfo(name = "season_number")
    val seasonNumber: Int,
    @ColumnInfo(name = "season_name")
    val seasonName: String,
    @ColumnInfo(name = "episodes_count")
    val episodesCount: Int,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "poster_path")
    val posterPath: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "air_date")
    val airDate: Long,
    @ColumnInfo(name = "caching_timestamp")
    val cachingTimestamp: Long = Date().time
)