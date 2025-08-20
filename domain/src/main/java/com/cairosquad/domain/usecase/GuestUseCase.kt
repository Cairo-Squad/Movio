package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.GuestRepository
import javax.inject.Inject

class ManageGuestUseCase @Inject constructor(
    private val guestRepository: GuestRepository
) {
    suspend fun setGuestState(enteredAsGuest: Boolean) {
        guestRepository.setGuestState(enteredAsGuest)
    }
    suspend fun getGuestState(): Boolean {
        return guestRepository.getGuestState()
    }
}