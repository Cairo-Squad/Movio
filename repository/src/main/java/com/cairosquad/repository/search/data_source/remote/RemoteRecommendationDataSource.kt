package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.ApiMovieDto

interface RemoteRecommendationDataSource {
    suspend fun getForYouMovies(): List<ApiMovieDto>

    suspend fun getExploreMoreMovies(): List<ApiMovieDto>
}