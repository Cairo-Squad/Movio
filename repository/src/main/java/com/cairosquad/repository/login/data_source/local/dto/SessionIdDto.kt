package com.cairosquad.repository.login.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SESSION_ID_COLUMN_NAME
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SESSION_ID_TABLE_NAME

@Entity(tableName = CACHED_SESSION_ID_TABLE_NAME)
data class SessionIdDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = CACHED_SESSION_ID_COLUMN_NAME)
    val sessionId: String
)
