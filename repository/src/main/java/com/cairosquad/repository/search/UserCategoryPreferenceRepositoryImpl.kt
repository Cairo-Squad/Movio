package com.cairosquad.repository.search

import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.entity.Genre
import com.cairosquad.entity.UserCategoryPreference
import com.cairosquad.repository.search.data_source.local.UserCategoryPreferenceLocalDataSource
import com.cairosquad.repository.search.data_source.local.dto.GenreDto
import com.cairosquad.repository.search.data_source.local.dto.UserCategoryPreferenceDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserCategoryPreferenceRepositoryImpl (
    private val localDataSource: UserCategoryPreferenceLocalDataSource,
) : UserCategoryPreferenceRepository {
    override suspend fun updatePreferences(genres: List<Genre>) {
        localDataSource.updatePreferences(genres.map { it.toDto() })
    }

    override suspend fun getTopPreferences(): Genre?{
       return localDataSource.getTopPreferences()?.toDomain()
    }

    override suspend fun cleanOldPreferences() {
        TODO("Not yet implemented")
    }

}

fun GenreDto.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun Genre.toDto(
    clickCount: Int = 0,
    lastUpdated: Long = System.currentTimeMillis()
): GenreDto {
    return GenreDto(
        id = id,
        name = name,
        clickCount = clickCount,
        lastUpdated = lastUpdated
    )
}

