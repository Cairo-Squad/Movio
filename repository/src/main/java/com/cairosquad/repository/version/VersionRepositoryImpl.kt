package com.cairosquad.repository.version

import com.cairosquad.domain.repository.VersionRepository
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    private val dataSource: VersionDataSource
) : VersionRepository {
    override fun getAppVersion(): String {
        return dataSource.getAppVersion()
    }
}