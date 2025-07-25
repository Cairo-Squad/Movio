package com.cairosquad.local.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.movie.MoviesCacheDao
import com.cairosquad.local.cache.request.RequestCachedDao
import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.local.search.discovery.dao.DiscoveryDao
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.repository.artists.dto.ArtistMovieCachedDto
import com.cairosquad.repository.artists.dto.ArtistSeriesCachedDto
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto
import com.cairosquad.repository.movie.data_source.local.dto.GenreCacheDtoNew
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.RequestMovieCacheCrossRef
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.PersonalizedMoviesIdsDto
import com.cairosquad.repository.search.data_source.local.dto.RecentSearchEntity
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SuggestedMoviesIdsDto
import com.cairosquad.repository.utils.RequestCacheDto

@Database(
    entities = [
        MovieCacheDto::class,
        SeriesCacheDto::class,
        ArtistCacheDto::class,
        RecentSearchEntity::class,
        PersonalizedMoviesIdsDto::class,
        SuggestedMoviesIdsDto::class,
        ArtistMovieCachedDto::class,
        ArtistSeriesCachedDto::class,

        SessionIdDto::class,

        RequestCacheDto::class,
        MovieWithoutGenreCacheDto::class,
        GenreCacheDtoNew::class,

        RequestMovieCacheCrossRef::class,
        MovieGenreCacheCrossRef::class,

    ],
    version = 1,
    exportSchema = true,
)
abstract class MovioDataBase : RoomDatabase() {

    abstract fun cacheDao(): CacheDao

    abstract fun recentSearchDao(): LocalRecentSearchDao

    abstract fun discoveryDao(): DiscoveryDao

    abstract fun loginDao(): LoginDao

    abstract fun moviesCacheDao(): MoviesCacheDao

    abstract fun genreDao(): GenreDao

    abstract fun requestCachedDao(): RequestCachedDao
}