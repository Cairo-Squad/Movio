package com.cairosquad.local.login.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto
import com.cairosquad.repository.search.data_source.local.dto.CACHED_SESSION_ID_TABLE_NAME

@Dao
interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSessionId(sessionId: SessionIdDto)

    @Query("SELECT * FROM $CACHED_SESSION_ID_TABLE_NAME")
    suspend fun getSessionId(): List<SessionIdDto>
}