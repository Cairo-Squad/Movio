package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.VersionRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    private val repository: VersionRepository
) {
     fun getAppVersion(): String = repository.getAppVersion()
}