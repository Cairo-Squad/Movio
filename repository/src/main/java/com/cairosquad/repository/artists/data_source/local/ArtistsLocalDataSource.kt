package com.cairosquad.repository.artists.data_source.local

import com.cairosquad.repository.artists.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.artists.data_source.local.dto.CacheCodeWithArtistsCacheDto

interface ArtistsLocalDataSource {
    suspend fun insertCacheCodeWithArtists(cacheCodeWithArtists: CacheCodeWithArtistsCacheDto)

    suspend fun getArtistsByCacheCode(cacheCode: String): List<ArtistCacheDto>

    suspend fun deleteExpiredCache(timestamp: Long)
}