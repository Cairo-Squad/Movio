package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.entity.Movie
import com.cairosquad.repository.search.data_source.remote.RemoteRecommendationDataSource

class RecommendationRepositoryImpl(
    private val remoteRecommendationDataSource: RemoteRecommendationDataSource
) : RecommendationRepository {
    override suspend fun getForYouMovies(): List<Movie> {
        return remoteRecommendationDataSource.getForYouMovies().map { it.toEntity() }
    }

    override suspend fun getExploreMoreMovies(): List<Movie> {
        return remoteRecommendationDataSource.getExploreMoreMovies().map { it.toEntity() }
    }
}