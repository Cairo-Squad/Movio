package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.entity.Genre
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class GetUserCategoryPreferenceUseCaseTest {

    private lateinit var useCase: GetUserCategoryPreferenceUseCase
    private val repository: UserCategoryPreferenceRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetUserCategoryPreferenceUseCase(repository)
    }

    @Test
    fun `invoke should return top genre from repository`() = runTest {
        val expectedGenre = Genre(id = 1, name = "Action")
        coEvery { repository.getTopPreferences() } returns expectedGenre

        val result = useCase()

        assertEquals(expectedGenre, result)
        coVerify(exactly = 1) { repository.getTopPreferences() }
    }

    @Test
    fun `invoke should return null if no preferences exist`() = runTest {
        coEvery { repository.getTopPreferences() } returns null

        val result = useCase()

        assertNull(result)
        coVerify(exactly = 1) { repository.getTopPreferences() }
    }
}
