package com.cairosquad.local.cache.artist

import com.cairosquad.local.cache.cacheCode.CacheCodeDao
import com.cairosquad.repository.artists.data_source.local.ArtistsLocalDataSource
import com.cairosquad.repository.artists.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.artists.data_source.local.dto.CacheCodeArtistCrossRef
import com.cairosquad.repository.artists.data_source.local.dto.CacheCodeWithArtistsCacheDto

class ArtistsLocalDataSourceImpl(
    private val artistsCacheDao: ArtistsCacheDao,
    private val cacheCodeDao: CacheCodeDao
) : ArtistsLocalDataSource {

    override suspend fun insertCacheCodeWithArtists(cacheCodeWithArtists: CacheCodeWithArtistsCacheDto) {
        artistsCacheDao.insertArtists(cacheCodeWithArtists.artists)
        cacheCodeDao.insertCacheCode(cacheCodeWithArtists.cacheCode)
        artistsCacheDao.insertCrossRefForCacheCodeAndArtist(
            CacheCodeArtistCrossRef.fromCacheCodeAndArtistList(
                cacheCode = cacheCodeWithArtists.cacheCode,
                artists = cacheCodeWithArtists.artists
            )
        )
    }

    override suspend fun getArtistsByCacheCode(cacheCode: String): List<ArtistCacheDto> {
        return artistsCacheDao.getArtistsByCacheCode(cacheCode)?.artists ?: emptyList()
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        cacheCodeDao.deleteExpiredCacheCode(timestamp)
        artistsCacheDao.deleteExpiredArtistCache(timestamp)
        artistsCacheDao.deleteCrossRefForNonExistingCacheCodeAndArtist()
    }
}