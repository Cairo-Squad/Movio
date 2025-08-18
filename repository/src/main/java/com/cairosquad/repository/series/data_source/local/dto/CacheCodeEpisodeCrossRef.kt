package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

@Entity(
    tableName = "CacheCodeEpisodeCrossRef",
    primaryKeys = ["cacheCode", "episode_id_language"]
)
data class CacheCodeEpisodeCrossRef(
    @ColumnInfo(name = "cacheCode") val cacheCode: String,
    @ColumnInfo(name = "episode_id_language") val episodeIdWithLanguage: String
) {
    companion object {
        fun fromCacheCodeAndEpisodeList(
            cacheCode: CacheCodeDto,
            episodes: List<EpisodeCacheDto>
        ): List<CacheCodeEpisodeCrossRef> {
            return episodes.map { episode ->
                CacheCodeEpisodeCrossRef(
                    cacheCode = cacheCode.cacheCode,
                    episodeIdWithLanguage = episode.episodeIdWithLanguage
                )
            }
        }
    }
}