package com.cairosquad.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.cacheSearch.CacheDao
import com.cairosquad.local.cacheSearch.entity.ArtistCacheEntity
import com.cairosquad.local.cacheSearch.entity.MovieCacheEntity
import com.cairosquad.local.cacheSearch.entity.SeriesCacheEntity

@Database(
    entities = [MovieCacheEntity::class, SeriesCacheEntity::class, ArtistCacheEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

}