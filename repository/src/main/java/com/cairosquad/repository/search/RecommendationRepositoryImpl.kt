package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.MovieDiscoveryRepository
import com.cairosquad.entity.Movie
import com.cairosquad.repository.search.data_source.remote.RemoteRecommendationDataSource

class RecommendationRepositoryImpl(
    private val remoteRecommendationDataSource: RemoteRecommendationDataSource
) : MovieDiscoveryRepository {
    override suspend fun getPersonalizedMovies(): List<Movie> {
        return remoteRecommendationDataSource.getForYouMovies().map { it.toMovie() }
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return remoteRecommendationDataSource.getExploreMoreMovies().map { it.toMovie() }
    }
}