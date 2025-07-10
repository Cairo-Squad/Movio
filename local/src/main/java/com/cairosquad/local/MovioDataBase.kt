package com.cairosquad.local

import SearchHistoryDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.cacheSearch.CacheDao
import com.cairosquad.local.cacheSearch.entity.ArtistCacheEntity
import com.cairosquad.local.cacheSearch.entity.MovieCacheEntity
import com.cairosquad.local.cacheSearch.entity.SeriesCacheEntity
import com.cairosquad.local.roomentity.SearchHistoryEntity

@Database(
    entities = [MovieCacheEntity::class, SeriesCacheEntity::class, ArtistCacheEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun searchHistoryDao(): SearchHistoryDao

}