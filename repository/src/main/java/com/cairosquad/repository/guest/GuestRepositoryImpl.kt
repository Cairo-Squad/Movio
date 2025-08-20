package com.cairosquad.repository.guest

import com.cairosquad.domain.repository.GuestRepository
import com.cairosquad.repository.guest.data_source.local.GuestDataSource
import javax.inject.Inject

class GuestRepositoryImpl @Inject constructor(
    private val dataSource: GuestDataSource
) : GuestRepository {
    override suspend fun setGuestState(enteredAsGuest: Boolean) {
        dataSource.setGuestState(enteredAsGuest)
    }

    override suspend fun getGuestState(): Boolean {
        return dataSource.getGuestState()
    }
}