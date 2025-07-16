package com.cairosquad.local.search.discovery.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SuggestedMoviesIdsDto")
data class SuggestedMoviesIdsDto(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int = 0
)
