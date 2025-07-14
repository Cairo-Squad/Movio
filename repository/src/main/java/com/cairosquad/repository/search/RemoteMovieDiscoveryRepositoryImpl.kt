package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.entity.Movie
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource

class RemoteMovieDiscoveryRepositoryImpl(
    private val remoteRecommendationDataSource: RemoteMovieDiscoveryDataSource
) : MovieDiscoveryRepository {
    override suspend fun getPersonalizedMovies(): List<Movie> {
        return tryToCall { remoteRecommendationDataSource.getPersonalizedMovies().map { it.toEntity() } }
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return tryToCall { remoteRecommendationDataSource.getSuggestedMovies().map { it.toEntity() } }
    }
}