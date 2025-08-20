package com.cairosquad.domain.repository

interface GuestRepository {
    suspend fun setGuestState(enteredAsGuest: Boolean)
    suspend fun getGuestState(): Boolean
}