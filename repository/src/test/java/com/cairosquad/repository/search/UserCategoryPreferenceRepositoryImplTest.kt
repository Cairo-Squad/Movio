package com.cairosquad.repository.search

import com.cairosquad.entity.Genre
import com.cairosquad.repository.search.data_source.local.UserCategoryPreferenceLocalDataSource
import com.cairosquad.repository.search.data_source.local.dto.GenreDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class UserCategoryPreferenceRepositoryImplTest {

    private lateinit var repository: UserCategoryPreferenceRepositoryImpl
    private val localDataSource: UserCategoryPreferenceLocalDataSource = mockk(relaxed = true)


    @Before
    fun setUp() {
        repository = UserCategoryPreferenceRepositoryImpl(localDataSource)
    }

    @Test
    fun `updatePreferences should map genres to DTOs and pass to localDataSource`() = runTest {
        val domainGenres = listOf(
            Genre(id = 1, name = "Action"),
            Genre(id = 2, name = "Drama")
        )

        repository.updatePreferences(domainGenres)

        coVerify(exactly = 1) {
            localDataSource.updatePreferences(
                match {
                    it.size == 2 &&
                            it[0].name == "Action" &&
                            it[1].name == "Drama"
                }
            )
        }
    }

    @Test
    fun `getTopPreferences should return domain genre from localDataSource`() = runTest {
        val dto = GenreDto(id = 1, name = "Comedy", clickCount = 3, lastUpdated = 12345L)
        coEvery { localDataSource.getTopPreferences() } returns dto

        val result = repository.getTopPreferences()

        assertNotNull(result)
        assertEquals("Comedy", result?.name)
        assertEquals(1, result?.id)

        coVerify { localDataSource.getTopPreferences() }
    }

    @Test
    fun `getTopPreferences should return null if localDataSource returns null`() = runTest {

        coEvery { localDataSource.getTopPreferences() } returns null

        val result = repository.getTopPreferences()

        assertNull(result)
        coVerify { localDataSource.getTopPreferences() }
    }
}
