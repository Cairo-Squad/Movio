package com.cairosquad.domain.repository

interface VersionRepository {
    fun getAppVersion(): String
}