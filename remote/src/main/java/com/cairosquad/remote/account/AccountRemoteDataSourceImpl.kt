package com.cairosquad.remote.account

import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.cairosquad.repository.account.data_source.remote.dto.MediaListDto
import com.cairosquad.repository.account.data_source.remote.dto.acount.AccountDto
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(
    private val apiService: AccountApiService
) : AccountRemoteDataSource {
    override suspend fun getAccountDetails(): AccountDto {
        return apiService.getAccountDetails()
    }

    override suspend fun getMovieLists(accountId: Long, page: Int): List<MediaListDto> {

        return getListsByType(accountId, page, "movie")
    }

    override suspend fun getSeriesLists(accountId: Long, page: Int): List<MediaListDto> {
        return getListsByType(accountId, page, "tv")
    }

    private suspend fun getListsByType(
        accountId: Long,
        page: Int,
        type: String
    ): List<MediaListDto> {
        return apiService.getLists(accountId, page)
            .results
            .filter { it.listType == type }
    }
}