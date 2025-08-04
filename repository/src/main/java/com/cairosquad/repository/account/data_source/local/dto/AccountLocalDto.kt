package com.cairosquad.repository.account.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AccountLocalDto")
data class AccountLocalDto(
    @PrimaryKey
    val id: Long? = 0L,
    @ColumnInfo(name = "accountId")
    val accountId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "imagePath")
    val imagePath: String
)