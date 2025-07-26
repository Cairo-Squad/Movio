package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "RequestCacheDto")
data class RequestCacheDto(
    @PrimaryKey
    @ColumnInfo(name = "request")
    val request: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = Date().time
)