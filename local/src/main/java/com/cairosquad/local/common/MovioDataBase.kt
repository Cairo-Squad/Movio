package com.cairosquad.local.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.cache.entity.ArtistCacheEntity
import com.cairosquad.local.search.cache.entity.MovieCacheEntity
import com.cairosquad.local.search.cache.entity.SeriesCacheEntity
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.search.recent.entity.RecentSearchEntity

@Database(
    entities = [
        MovieCacheEntity::class,
        SeriesCacheEntity::class,
        ArtistCacheEntity::class,
        RecentSearchEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun recentSearchDao(): LocalRecentSearchDao

}