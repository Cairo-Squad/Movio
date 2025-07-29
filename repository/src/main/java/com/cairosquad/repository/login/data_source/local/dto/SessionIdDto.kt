package com.cairosquad.repository.login.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SessionIdDto")
data class SessionIdDto(
    @PrimaryKey
    val id: Long? = 0L,
    @ColumnInfo(name = "sessionId")
    val sessionId: String
)
