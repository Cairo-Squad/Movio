package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.entity.Genre
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@ExperimentalCoroutinesApi
class UpdateUserCategoryPreferenceUseCaseTest {

    private lateinit var useCase: UpdateUserCategoryPreferenceUseCase
    private val repository: UserCategoryPreferenceRepository = mockk(relaxed = true)


    @Before
    fun setUp() {
        useCase = UpdateUserCategoryPreferenceUseCase(repository)
    }

    @Test
    fun `invoke should call repository with given genres`() = runTest {
        val genres = listOf(
            Genre(id = 1, name = "Action"),
            Genre(id = 2, name = "Drama")
        )

        useCase(genres)

        coVerify(exactly = 1) {
            repository.updatePreferences(genres)
        }
    }
}
