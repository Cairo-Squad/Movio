package com.cairosquad.repository.search.dataSource.remote

import com.cairosquad.repository.search.dataSource.remote.dto.MovieDto

interface RemoteRecommendationDataSource {
    suspend fun getForYouMovies(): List<MovieDto>

    suspend fun getExploreMoreMovies(): List<MovieDto>
}