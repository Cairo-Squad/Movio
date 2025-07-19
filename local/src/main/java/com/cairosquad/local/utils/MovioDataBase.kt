package com.cairosquad.local.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.discovery.dao.DiscoveryDao
import com.cairosquad.repository.search.data_source.local.dto.PersonalizedMoviesIdsDto
import com.cairosquad.repository.search.data_source.local.dto.SuggestedMoviesIdsDto
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.search.recommendation.dao.UserCategoryPreferenceDao
import com.cairosquad.repository.artists.dto.ArtistMovieCachedDto
import com.cairosquad.repository.artists.dto.ArtistSeriesCachedDto
import com.cairosquad.repository.search.data_source.local.dto.RecentSearchEntity
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.GenreDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto

@Database(
    entities = [
        MovieCacheDto::class,
        SeriesCacheDto::class,
        ArtistCacheDto::class,
        RecentSearchEntity::class,
        PersonalizedMoviesIdsDto::class,
        SuggestedMoviesIdsDto::class,
        GenreDto::class,
        ArtistMovieCachedDto::class,
        ArtistSeriesCachedDto::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun recentSearchDao(): LocalRecentSearchDao

    abstract fun discoveryDao(): DiscoveryDao

    abstract fun userCategoryPreferenceDao(): UserCategoryPreferenceDao


}