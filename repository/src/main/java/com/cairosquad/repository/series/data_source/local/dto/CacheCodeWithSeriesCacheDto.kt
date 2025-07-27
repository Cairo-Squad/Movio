package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

data class CacheCodeWithSeriesCacheDto(
    @Embedded
    val cacheCode: CacheCodeDto,
    @Relation(
        parentColumn = "cacheCode",
        entity = SeriesWithoutGenreCacheDto::class,
        entityColumn = "series_id",
        associateBy = Junction(CacheCodeSeriesCacheCrossRef::class)
    )
    val series: List<SeriesCacheDto>,
)