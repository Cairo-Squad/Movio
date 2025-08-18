package com.cairosquad.repository.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.LanguageRepository
import com.cairosquad.entity.Artist
import com.cairosquad.repository.artists.data_source.local.ArtistsLocalDataSource
import com.cairosquad.repository.artists.data_source.local.toCacheCodeWithArtistsCacheDto
import com.cairosquad.repository.artists.data_source.local.toEntityList
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.toEntity
import com.cairosquad.repository.artists.data_source.remote.toEntityList
import com.cairosquad.repository.utils.mappers.tryToCall
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfArtist
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMovieTopCast
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesTopCast
import java.util.Date
import javax.inject.Inject

class ArtistsRepositoryImpl @Inject constructor(
    private val artistsRemoteDataSource: ArtistsRemoteDataSource,
    private val artistsLocalDataSource: ArtistsLocalDataSource,
    private val languageRepository: LanguageRepository
) : ArtistsRepository {

    override suspend fun getArtistsByQuery(query: String, page: Int): List<Artist> {
        return tryToCall {
            artistsRemoteDataSource.getArtistsByQuery(query, page).toEntityList()
        }
    }

    override suspend fun getArtistById(id: Long): Artist {
        artistsLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return artistsLocalDataSource
            .getArtistsByCacheCode(cacheCode = getCacheCodeOfArtist(id,
                languageRepository.getLanguage()
            ))
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                artistsRemoteDataSource.getArtistById(id)
                    .toEntity()
                    .also { artist ->
                        artistsLocalDataSource.insertCacheCodeWithArtists(
                            listOf(artist).toCacheCodeWithArtistsCacheDto(
                                cacheCode = getCacheCodeOfArtist(id,
                                    languageRepository.getLanguage()
                                ),
                                languageRepository.getLanguage()
                            )
                        )
                    }
            }
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist> {
        return getArtists(
            remoteFetcher = {
                artistsRemoteDataSource.getMovieTopCast(movieId, page).map { it.toEntity() }
            },
            cacheCode = getCacheCodeOfMovieTopCast(movieId, page,
                languageRepository.getLanguage()
            )
        )
    }

    override suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<Artist> {
        return getArtists(
            remoteFetcher = {
                artistsRemoteDataSource.getSeriesTopCast(seriesId, page).map { it.toEntity() }
            },
            cacheCode = getCacheCodeOfSeriesTopCast(seriesId, page,
                languageRepository.getLanguage()
            )
        )
    }

    private suspend fun getArtists(
        remoteFetcher: suspend () -> List<Artist>,
        cacheCode: String
    ): List<Artist> {
        artistsLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return artistsLocalDataSource
            .getArtistsByCacheCode(cacheCode = cacheCode)
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                remoteFetcher()
                    .also { artists ->
                        artistsLocalDataSource.insertCacheCodeWithArtists(
                            artists.toCacheCodeWithArtistsCacheDto(
                                cacheCode = cacheCode,
                                languageRepository.getLanguage()

                            )
                        )
                    }
            }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 86_400_000
    }
}