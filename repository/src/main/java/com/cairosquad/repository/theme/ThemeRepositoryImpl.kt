package com.cairosquad.repository.theme

import com.cairosquad.domain.repository.ThemeRepository
import com.cairosquad.entity.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val themeDataSource: ThemeDataSource
) : ThemeRepository {

    override suspend fun saveTheme(theme: Theme) {
        themeDataSource.saveTheme(theme.name.lowercase())
    }

    override fun getTheme(): Flow<Theme> {
        return themeDataSource.getTheme().map { themeString ->
            Theme.fromString(themeString)
        }
    }
}