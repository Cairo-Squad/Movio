package com.cairosquad.local.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto

@Database(
    entities = [
        MovieCacheDto::class,
        SeriesCacheDto::class,
        ArtistCacheDto::class,
        RecentSearchEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun recentSearchDao(): LocalRecentSearchDao

}