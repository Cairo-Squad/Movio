package com.cairosquad.local.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.recent.dao.RecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
import com.cairosquad.repository.search.data_source.local.dto.CachedSeriesDto
import com.cairosquad.repository.search.data_source.local.dto.CachedArtistDto
import com.cairosquad.repository.search.data_source.local.dto.CachedMovieDto

@Database(
    entities = [
        CachedMovieDto::class,
        CachedSeriesDto::class,
        CachedArtistDto::class,
        RecentSearchEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun recentSearchDao(): RecentSearchDao

}