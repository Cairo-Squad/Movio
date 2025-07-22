package com.cairosquad.local.login.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto

@Dao
interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSessionId(sessionId: SessionIdDto)

    @Query("SELECT sessionId FROM SessionIdDto")
    suspend fun getSessionId(): SessionIdDto?
}