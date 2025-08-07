package com.cairosquad.repository.version

interface VersionDataSource {
    fun getAppVersion(): String
}