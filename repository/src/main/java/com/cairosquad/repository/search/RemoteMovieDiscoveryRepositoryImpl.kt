package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.entity.Movie
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity

class RemoteMovieDiscoveryRepositoryImpl(
    private val remoteRecommendationDataSource: RemoteMovieDiscoveryDataSource
) : MovieDiscoveryRepository {
    override suspend fun getPersonalizedMovies(): List<Movie> {
        return tryToCall {
            remoteRecommendationDataSource.getPersonalizedMovies().toEntity()
        }
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return tryToCall { remoteRecommendationDataSource.getSuggestedMovies().toEntity() }
    }
}