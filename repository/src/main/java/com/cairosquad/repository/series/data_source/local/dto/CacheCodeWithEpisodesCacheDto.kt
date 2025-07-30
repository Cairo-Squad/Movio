package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

data class CacheCodeWithEpisodesCacheDto(
    @Embedded
    val cacheCode: CacheCodeDto,
    @Relation(
        parentColumn = "cacheCode",
        entityColumn = "episode_id",
        associateBy = Junction(CacheCodeEpisodeCrossRef::class)
    )
    val episodes: List<EpisodeCacheDto>
)