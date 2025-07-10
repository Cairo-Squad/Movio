package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.MovieDto

interface RemoteRecommendationDataSource {
    suspend fun getForYouMovies(): List<MovieDto>

    suspend fun getExploreMoreMovies(): List<MovieDto>
}