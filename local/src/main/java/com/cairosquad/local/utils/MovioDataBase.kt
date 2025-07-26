package com.cairosquad.local.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.movie.MoviesCacheDao
import com.cairosquad.local.cache.request.RequestCachedDao
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.RequestMovieCacheCrossRef
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.RecentSearchEntity
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.utils.sharedDto.local.RequestCacheDto
import com.cairosquad.repository.utils.sharedDto.local.RequestReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

@Database(
    entities = [
        SeriesCacheDto::class,
        ArtistCacheDto::class,
        RecentSearchEntity::class,

        SessionIdDto::class,

        RequestCacheDto::class,

        MovieWithoutGenreCacheDto::class,
        RequestMovieCacheCrossRef::class,

        GenreOfMovieCacheDto::class,
        MovieGenreCacheCrossRef::class,

        ReviewCacheDto::class,
        RequestReviewCacheCrossRef::class

    ],
    version = 1,
    exportSchema = true,
)

abstract class MovioDataBase : RoomDatabase() {

    abstract fun recentSearchDao(): LocalRecentSearchDao

    abstract fun loginDao(): LoginDao

    abstract fun moviesCacheDao(): MoviesCacheDao

    abstract fun genreDao(): GenreDao

    abstract fun requestCachedDao(): RequestCachedDao

    abstract fun reviewDao(): ReviewDao
}