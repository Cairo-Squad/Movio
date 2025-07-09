package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.search.dataSource.remote.search.RemoteSearchDataSource

class SearchRepositoryImpl(
    private val remoteSearchDataSource: RemoteSearchDataSource
) : SearchRepository {
    override suspend fun searchSeries(query: String): List<Series> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun searchArtists(query: String): List<Artist> {
        TODO("Not yet implemented")
    }
}