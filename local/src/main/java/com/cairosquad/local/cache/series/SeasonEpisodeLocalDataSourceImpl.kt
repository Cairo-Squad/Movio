package com.cairosquad.local.cache.series

import com.cairosquad.local.cache.cacheCode.CacheCodeDao
import com.cairosquad.repository.series.data_source.local.SeasonEpisodeLocalDataSource
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeEpisodeCrossRef
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeSeasonCrossRef
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithEpisodesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeasonsCacheDto
import com.cairosquad.repository.series.data_source.local.dto.EpisodeCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeasonCacheDto
import javax.inject.Inject

class SeasonEpisodeLocalDataSourceImpl @Inject constructor(
    private val seasonEpisodeCacheDao: SeasonEpisodeCacheDao,
    private val cacheCodeDao: CacheCodeDao
) : SeasonEpisodeLocalDataSource {

    override suspend fun insertCacheCodeWithSeasons(cacheCodeWithSeasons: CacheCodeWithSeasonsCacheDto) {
        seasonEpisodeCacheDao.insertSeasons(cacheCodeWithSeasons.seasons)
        cacheCodeDao.insertCacheCode(cacheCodeWithSeasons.cacheCode)
        seasonEpisodeCacheDao.insertCrossRefForCacheCodeAndSeason(
            CacheCodeSeasonCrossRef.fromCacheCodeAndSeasonList(
                cacheCode = cacheCodeWithSeasons.cacheCode,
                seasons = cacheCodeWithSeasons.seasons
            )
        )
    }

    override suspend fun insertCacheCodeWithEpisodes(cacheCodeWithEpisodes: CacheCodeWithEpisodesCacheDto) {
        seasonEpisodeCacheDao.insertEpisodes(cacheCodeWithEpisodes.episodes)
        cacheCodeDao.insertCacheCode(cacheCodeWithEpisodes.cacheCode)
        seasonEpisodeCacheDao.insertCrossRefForCacheCodeAndEpisode(
            CacheCodeEpisodeCrossRef.fromCacheCodeAndEpisodeList(
                cacheCode = cacheCodeWithEpisodes.cacheCode,
                episodes = cacheCodeWithEpisodes.episodes
            )
        )
    }

    override suspend fun getSeasonsByCacheCode(cacheCode: String): List<SeasonCacheDto> {
        return seasonEpisodeCacheDao.getSeasonsByCacheCode(cacheCode)?.seasons ?: emptyList()
    }

    override suspend fun getEpisodesByCacheCode(cacheCode: String): List<EpisodeCacheDto> {
        return seasonEpisodeCacheDao.getEpisodesByCacheCode(cacheCode)?.episodes ?: emptyList()
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        cacheCodeDao.deleteExpiredCacheCode(timestamp)
        seasonEpisodeCacheDao.deleteExpiredSeasonCache(timestamp)
        seasonEpisodeCacheDao.deleteExpiredEpisodeCache(timestamp)
        seasonEpisodeCacheDao.deleteCrossRefForNonExistingCacheCodeAndSeason()
        seasonEpisodeCacheDao.deleteCrossRefForNonExistingCacheCodeAndEpisode()
    }
}
