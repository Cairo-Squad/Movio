package com.cairosquad.repository.artists.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistSeriesCachedDto")
data class ArtistSeriesCachedDto (
    @PrimaryKey
    val artistId: Long,
    val seriesId: Long
)