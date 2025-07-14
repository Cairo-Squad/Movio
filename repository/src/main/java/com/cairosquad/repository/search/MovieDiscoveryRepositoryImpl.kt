package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.LocalMovieDiscoveryCacheDataSource
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.cairosquad.entity.Movie

class MovieDiscoveryRepositoryImpl(
    private val remote: RemoteMovieDiscoveryDataSource,
    private val local: LocalMovieDiscoveryCacheDataSource
) : MovieDiscoveryRepository {

    override suspend fun getPersonalizedMovies(): List<Movie> = withContext(Dispatchers.IO) {
        tryToCall {
            local.getCachedMovies("top_rated")
                .map { it.toEntity() }
                .takeIf { it.isNotEmpty() }
                ?: remote.getPersonalizedMovies()
                    .map { it.toEntity() }
                    .also { fresh ->
                        local.cacheMovies("top_rated", fresh.map { it.toMovieCacheDto("top_rated") })
                    }
        }
    }

    override suspend fun getSuggestedMovies(): List<Movie> = withContext(Dispatchers.IO) {
        tryToCall {
            local.getCachedMovies("now_playing")
                .map { it.toEntity() }
                .takeIf { it.isNotEmpty() }
                ?: remote.getSuggestedMovies()
                    .map { it.toEntity() }
                    .also { fresh ->
                        local.cacheMovies("now_playing", fresh.map { it.toMovieCacheDto("now_playing") })
                    }
        }
    }
}
