package com.cairosquad.repository.utils.sharedDto.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "CacheCodeDto")
data class CacheCodeDto(
    @PrimaryKey
    @ColumnInfo(name = "cacheCode")
    val cacheCode: String,
    @ColumnInfo(name = "cachingTimestamp")
    val cachingTimestamp: Long = Date().time
)