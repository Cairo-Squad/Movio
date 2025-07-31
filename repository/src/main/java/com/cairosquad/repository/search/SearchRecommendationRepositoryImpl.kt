package com.cairosquad.repository.search

import com.cairosquad.domain.repository.SearchRecommendationRepository
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.utils.mappers.tryToCall
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SearchRecommendationRepositoryImpl @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val seriesRemoteDataSource: SeriesRemoteDataSource,
    private val artistsRemoteDataSource: ArtistsRemoteDataSource,
) : SearchRecommendationRepository {
    override suspend fun getSearchRecommendation(query: String): List<String> {
        return tryToCall {
            query.takeIf { it.isNotBlank() }
                ?.let { query ->
                    coroutineScope {
                        val series = async {
                            runCatching { seriesRemoteDataSource.getSeriesByQuery(query, 1) }
                                .getOrNull() ?: emptyList()
                        }.await()
                        val movies = async {
                            runCatching { moviesRemoteDataSource.getMoviesByQuery(query, 1) }
                                .getOrNull() ?: emptyList()
                        }.await()
                        val artist = async {
                            runCatching { artistsRemoteDataSource.getArtistsByQuery(query, 1) }
                                .getOrNull() ?: emptyList()
                        }.await()
                        buildList {
                            addAll(movies.mapNotNull { it.title?.takeIf { title -> title.isNotBlank() } })
                            addAll(artist.mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } })
                            addAll(series.mapNotNull { it.name?.takeIf { name -> name.isNotBlank() } })
                        }
                            .distinct()
                            .shuffled()
                            .take(20)
                    }
                }
                ?: emptyList()
        }
    }
}