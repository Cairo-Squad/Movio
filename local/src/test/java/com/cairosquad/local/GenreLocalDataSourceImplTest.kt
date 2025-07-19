package com.cairosquad.local

import com.cairosquad.local.search.recommendation.dao.GenreLocalDataSourceImpl
import com.cairosquad.local.search.recommendation.dao.UserCategoryPreferenceDao
import com.cairosquad.repository.search.data_source.local.dto.GenreDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GenreLocalDataSourceImplTest {

    private lateinit var dataSource: GenreLocalDataSourceImpl
    private val genreDao: UserCategoryPreferenceDao = mockk(relaxed = true)


    @Before
    fun setUp() {
        dataSource = GenreLocalDataSourceImpl(genreDao)
    }

    @Test
    fun `updatePreferences should update existing and insert new genres`() = runTest {
        val inputGenres = listOf(
            GenreDto(id = 1,name = "Action"),
            GenreDto(id = 2,name = "Drama")
        )

        val existingGenre = GenreDto(id = 1,name = "Action", clickCount = 2, lastUpdated = 1000L)

        coEvery { genreDao.getGenresByNames(listOf("Action", "Drama")) } returns listOf(existingGenre)

        dataSource.updatePreferences(inputGenres)

        coVerify {
            genreDao.updateGenres(
                withArg {
                    assertEquals(1, it.size)
                    assertEquals("Action", it[0].name)
                    assertEquals(3, it[0].clickCount)
                }
            )
            genreDao.insert(
                withArg {
                    assertEquals(1, it.size)
                    assertEquals("Drama", it[0].name)
                }
            )
        }
    }

    @Test
    fun `getTopPreferences should return top genre`() = runTest {
        val expected = GenreDto(id = 1,name = "Sci-Fi", clickCount = 10)
        coEvery { genreDao.getTopCategories() } returns expected

        val result = dataSource.getTopPreferences()

        assertEquals(expected, result)
        coVerify(exactly = 1) { genreDao.getTopCategories() }
    }
}
