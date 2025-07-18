package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.entity.Genre

class UpdateUserCategoryPreferenceUseCase(
    private val userCategoryPreferenceRepository: UserCategoryPreferenceRepository
) {
    suspend operator fun invoke(genres: List<Genre>) {
        userCategoryPreferenceRepository.updatePreferences(genres)
    }
}
