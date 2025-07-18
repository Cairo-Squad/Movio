package com.cairosquad.repository.artists.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistMovieCachedDto")
data class ArtistMovieCachedDto (
    @PrimaryKey
    val artistId: Long,
    val movieId: Long
)