package com.cairosquad.local.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cairosquad.repository.account.data_source.local.dto.AccountLocalDto

@Dao
interface AccountCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(accountDetail: AccountLocalDto)

    @Query("SELECT * FROM AccountLocalDto")
    suspend fun getAccount(): List<AccountLocalDto>

    @Query("DELETE FROM AccountLocalDto")
    fun deleteAccount()
}