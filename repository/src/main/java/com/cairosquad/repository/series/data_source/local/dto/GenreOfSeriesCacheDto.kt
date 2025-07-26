package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "GenreOfSeriesCacheDto")
data class GenreOfSeriesCacheDto(
    @ColumnInfo(name = "genre_id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long = Date().time,
)