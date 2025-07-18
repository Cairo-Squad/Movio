package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.entity.Genre
import com.cairosquad.entity.UserCategoryPreference

class GetUserCategoryPreferenceUseCase(
    private val userCategoryPreferenceRepository: UserCategoryPreferenceRepository
) {
    suspend operator fun invoke(): Genre? {
        return userCategoryPreferenceRepository.getTopPreferences()
    }
}