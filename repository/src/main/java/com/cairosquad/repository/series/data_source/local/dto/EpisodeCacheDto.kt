package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "EpisodeCacheDto")
data class EpisodeCacheDto(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val id: Long,
    @ColumnInfo(name = "episode_number")
    val episodeNumber: Int,
    @ColumnInfo(name = "photo_path")
    val photoPath: String,
    @ColumnInfo(name = "episode_name")
    val episodeName: String,
    @ColumnInfo(name = "runtime_minutes")
    val runtimeMinutes: Int,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "season_number")
    val seasonNumber: Int,
    @ColumnInfo(name = "series_id")
    val seriesId: Long,
    @ColumnInfo(name = "caching_timestamp")
    val cachingTimestamp: Long = Date().time
)