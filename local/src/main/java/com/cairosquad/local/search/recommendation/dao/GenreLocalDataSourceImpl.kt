package com.cairosquad.local.search.recommendation.dao

import androidx.room.Transaction
import com.cairosquad.repository.search.data_source.local.UserCategoryPreferenceLocalDataSource
import com.cairosquad.repository.search.data_source.local.dto.GenreDto

class GenreLocalDataSourceImpl(
    private val genreDao: UserCategoryPreferenceDao
) : UserCategoryPreferenceLocalDataSource{

    @Transaction
    override suspend fun updatePreferences(genres: List<GenreDto>) {
        val names = genres.map { it.name }
        val existingGenres = genreDao.getGenresByNames(names)

        val existingMap = existingGenres.associateBy { it.name }

        val (toUpdate, toInsert) = genres.partition { existingMap.containsKey(it.name) }

        val updatedGenres = toUpdate.map {
            val old = existingMap[it.name]!!
            old.copy(
                clickCount = old.clickCount + 1,
                lastUpdated = System.currentTimeMillis()
            )
        }

        genreDao.updateGenres(updatedGenres)
        genreDao.insert(toInsert)
    }


    override suspend fun getTopPreferences(): GenreDto? {
        return genreDao.getTopCategories()
    }

    override suspend fun cleanOldPreferences() {
        TODO()
    }

}