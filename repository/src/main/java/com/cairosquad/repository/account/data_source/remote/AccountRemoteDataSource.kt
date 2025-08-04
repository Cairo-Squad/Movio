package com.cairosquad.repository.account.data_source.remote

import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto

interface AccountRemoteDataSource {

    suspend fun getAccountDetails(): AccountDto

    suspend fun getMovieLists(accountId: Long, page: Int): List<MediaListDto>

    suspend fun getSeriesLists(accountId: Long, page: Int): List<MediaListDto>
}