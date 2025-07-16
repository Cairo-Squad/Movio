package com.cairosquad.local.search.discovery.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PersonalizedMoviesIdsDto")
data class PersonalizedMoviesIdsDto(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int = 0
)
