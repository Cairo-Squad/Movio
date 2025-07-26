package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cairosquad.repository.utils.sharedDto.local.RequestCacheDto

data class RequestWithSeriesCacheDto(
    @Embedded
    val request: RequestCacheDto,
    @Relation(
        parentColumn = "request",
        entity = SeriesWithoutGenreCacheDto::class,
        entityColumn = "series_id",
        associateBy = Junction(RequestSeriesCacheCrossRef::class)
    )
    val series: List<SeriesCacheDto>,
)