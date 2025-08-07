package com.cairosquad.movio.di

import android.content.Context
import androidx.room.Room
import com.cairosquad.local.account.AccountCacheDao
import com.cairosquad.local.account.AccountLocalDataSourceImpl
import com.cairosquad.local.cache.artist.ArtistsCacheDao
import com.cairosquad.local.cache.artist.ArtistsLocalDataSourceImpl
import com.cairosquad.local.cache.cacheCode.CacheCodeDao
import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.movie.MoviesCacheDao
import com.cairosquad.local.cache.movie.MoviesLocalDataSourceImpl
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.local.cache.series.SeasonEpisodeCacheDao
import com.cairosquad.local.cache.series.SeasonEpisodeLocalDataSourceImpl
import com.cairosquad.local.cache.series.SeriesCacheDao
import com.cairosquad.local.cache.series.SeriesLocalDataSourceImpl
import com.cairosquad.local.login.LocalAuthenticationDataSourceImpl
import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.local.onboarding.OnboardingDataSourceImpl
import com.cairosquad.local.search.recent.LocalRecentSearchDataSourceImpl
import com.cairosquad.local.search.recent.dao.LocalRecentSearchDao
import com.cairosquad.local.utils.MovioDataBase
import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.artists.data_source.local.ArtistsLocalDataSource
import com.cairosquad.repository.login.data_source.local.LocalAuthenticationDataSource
import com.cairosquad.repository.movie.data_source.local.MoviesLocalDataSource
import com.cairosquad.repository.onboarding.data_source.local.OnboardingDataSource
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.series.data_source.local.SeasonEpisodeLocalDataSource
import com.cairosquad.repository.series.data_source.local.SeriesLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovioDataBase {
        return Room.databaseBuilder(
            context,
            MovioDataBase::class.java,
            "MovioDataBase"
        ).build()
    }

    @Provides
    fun provideLoginDao(db: MovioDataBase): LoginDao = db.loginDao()

    @Provides
    fun provideMoviesCacheDao(db: MovioDataBase): MoviesCacheDao = db.moviesCacheDao()

    @Provides
    fun provideSeriesCacheDao(db: MovioDataBase): SeriesCacheDao = db.seriesCacheDao()

    @Provides
    fun provideSeasonEpisodeCacheDao(db: MovioDataBase): SeasonEpisodeCacheDao =
        db.seasonEpisodeCacheDao()

    @Provides
    fun provideArtistsCacheDao(db: MovioDataBase): ArtistsCacheDao = db.artistsCacheDao()

    @Provides
    fun provideGenreDao(db: MovioDataBase): GenreDao = db.genreDao()

    @Provides
    fun provideCacheCodeDao(db: MovioDataBase): CacheCodeDao = db.cacheCodeDao()

    @Provides
    fun provideReviewDao(db: MovioDataBase): ReviewDao = db.reviewDao()

    @Provides
    fun provideLocalRecentSearchDao(db: MovioDataBase): LocalRecentSearchDao = db.recentSearchDao()

    @Provides
    fun provideAccountCacheDao(db: MovioDataBase): AccountCacheDao = db.accountCacheDao()

    @Provides
    @Singleton
    fun provideLocalAuthenticationDataSource(
        loginDao: LoginDao
    ): LocalAuthenticationDataSource = LocalAuthenticationDataSourceImpl(loginDao)

    @Provides
    @Singleton
    fun provideLocalRecentSearchDataSource(
        dao: LocalRecentSearchDao
    ): LocalRecentSearchDataSource = LocalRecentSearchDataSourceImpl(dao)

    @Provides
    @Singleton
    fun provideMoviesLocalDataSource(
        movieDao: MoviesCacheDao,
        cacheCodeDao: CacheCodeDao,
        genreDao: GenreDao,
        reviewDao: ReviewDao
    ): MoviesLocalDataSource = MoviesLocalDataSourceImpl(
        moviesCacheDao = movieDao,
        cacheCodeDao = cacheCodeDao,
        genreDao = genreDao,
        reviewDao = reviewDao
    )

    @Provides
    @Singleton
    fun provideSeriesLocalDataSource(
        seriesDao: SeriesCacheDao,
        cacheCodeDao: CacheCodeDao,
        genreDao: GenreDao,
        reviewDao: ReviewDao
    ): SeriesLocalDataSource = SeriesLocalDataSourceImpl(
        seriesCacheDao = seriesDao,
        cacheCodeDao = cacheCodeDao,
        genreDao = genreDao,
        reviewDao = reviewDao
    )

    @Provides
    @Singleton
    fun provideSeasonEpisodeLocalDataSource(
        seasonEpisodeCacheDao: SeasonEpisodeCacheDao,
        cacheCodeDao: CacheCodeDao
    ): SeasonEpisodeLocalDataSource = SeasonEpisodeLocalDataSourceImpl(
        seasonEpisodeCacheDao = seasonEpisodeCacheDao,
        cacheCodeDao = cacheCodeDao
    )

    @Provides
    @Singleton
    fun provideArtistsLocalDataSource(
        artistsCacheDao: ArtistsCacheDao,
        cacheCodeDao: CacheCodeDao
    ): ArtistsLocalDataSource = ArtistsLocalDataSourceImpl(
        artistsCacheDao = artistsCacheDao,
        cacheCodeDao = cacheCodeDao
    )

    @Provides
    @Singleton
    fun provideOnboardingDataSource(
        @ApplicationContext context: Context
    ): OnboardingDataSource = OnboardingDataSourceImpl(
        context = context
    )

    @Provides
    @Singleton
    fun provideAccountLocalDataSource(
        accountCacheDao: AccountCacheDao
    ): AccountLocalDataSource = AccountLocalDataSourceImpl(accountCacheDao)
}