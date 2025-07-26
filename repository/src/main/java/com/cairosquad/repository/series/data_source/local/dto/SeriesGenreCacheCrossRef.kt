package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(
    tableName = "SeriesGenreCacheCrossRef",
    primaryKeys = ["series_id", "genre_id"]
)
data class SeriesGenreCacheCrossRef(
    @ColumnInfo(name = "series_id")
    val seriesId: Long,
    @ColumnInfo(name = "genre_id")
    val genreId: Long
)