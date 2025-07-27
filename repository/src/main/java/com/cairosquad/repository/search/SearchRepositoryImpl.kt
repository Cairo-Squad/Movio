package com.cairosquad.repository.search

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.search.data_source.local.LocalRecentSearchDataSource
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.utils.mappers.tryToCall

class SearchRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val seriesRemoteDataSource: SeriesRemoteDataSource,
    private val artistsRemoteDataSource: ArtistsRemoteDataSource,
    private val dataSource: LocalRecentSearchDataSource
) : SearchRepository {
    override suspend fun getAllHistory(): List<String> {
        return tryToCall { dataSource.getAll() }
    }

    override suspend fun getAllHistoryByQuery(query: String): List<String> {
        return tryToCall {
            query.takeIf { it.isNotBlank() }
                ?.let { query ->
                    val series = seriesRemoteDataSource.getSeriesByQuery(query, 1)
                    val movies = moviesRemoteDataSource.getMoviesByQuery(query, 1)
                    val artist = artistsRemoteDataSource.getArtistsByQuery(query, 1)
                    val local = dataSource.getByQuery(query)
                    val merged = buildList {
                        addAll(local)
                        addAll(movies.map { it.title ?: "" })
                        addAll(artist.map { it.name ?: "" })
                        addAll(series.map { it.name ?: "" })
                    }
                    merged.distinct().shuffled().take(20)
                }
                ?: dataSource.getAll()
        }
    }

    override suspend fun clearAll() {
        tryToCall { dataSource.clearAll() }
    }

    override suspend fun removeQuery(query: String) {
        tryToCall { dataSource.removeQuery(query) }
    }

    override suspend fun addQuery(query: String) {
        tryToCall { dataSource.addQuery(query) }
    }
}