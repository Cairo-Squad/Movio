package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

data class CacheCodeWithSeasonsCacheDto(
    @Embedded
    val cacheCode: CacheCodeDto,
    @Relation(
        parentColumn = "cacheCode",
        entityColumn = "season_id_language",
        associateBy = Junction(CacheCodeSeasonCrossRef::class)
    )
    val seasons: List<SeasonCacheDto>
)
