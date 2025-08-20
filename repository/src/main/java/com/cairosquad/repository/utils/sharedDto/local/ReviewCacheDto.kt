package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "ReviewCacheDto")
data class ReviewCacheDto(
    @PrimaryKey
    @ColumnInfo("review_id_language")
    val reviewIdWithLanguage: String,
    @ColumnInfo("review_id")
    val id: String,
    @ColumnInfo("author")
    val author: String,
    @ColumnInfo("authorPhotoPath")
    val authorPhotoPath: String,
    @ColumnInfo("rating")
    val rating: Float,
    @ColumnInfo("date")
    val date: Long,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("cachingTimestamp")
    val cachingTimestamp: Long = Date().time
)