package com.cairosquad.repository.search.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearchEntity(
    @ColumnInfo(name = "query_column")
    @PrimaryKey
    val query: String,
    @ColumnInfo(name= "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)