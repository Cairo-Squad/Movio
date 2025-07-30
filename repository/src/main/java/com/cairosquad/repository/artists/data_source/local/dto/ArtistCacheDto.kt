package com.cairosquad.repository.artists.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistCacheDto")
data class ArtistCacheDto(
    @ColumnInfo(name = "artist_id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "photoPath")
    val photoPath: String,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "birthDate")
    val birthDate: Long?,
    @ColumnInfo(name = "biography")
    val biography: String,
    @ColumnInfo(name = "department")
    val department: String,
)