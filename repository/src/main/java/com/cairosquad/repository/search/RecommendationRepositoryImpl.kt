package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.RecommendationRepository
import com.cairosquad.entity.Movie
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.remote.RemoteRecommendationDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity

class RecommendationRepositoryImpl(
    private val remoteRecommendationDataSource: RemoteRecommendationDataSource
) : RecommendationRepository {
    override suspend fun getForYouMovies(): List<Movie> {
        return tryToCall {
            remoteRecommendationDataSource.getForYouMovies().toEntity()
        }
    }

    override suspend fun getExploreMoreMovies(): List<Movie> {
        return tryToCall {
            remoteRecommendationDataSource.getExploreMoreMovies().toEntity()
        }
    }
}