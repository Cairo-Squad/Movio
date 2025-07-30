package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SeriesCacheDto(
    @Embedded
    val seriesWithoutGenre: SeriesWithoutGenreCacheDto,
    @Relation(
        parentColumn = "series_id",
        entityColumn = "genre_id",
        associateBy = Junction(SeriesGenreCacheCrossRef::class)
    )
    val genres: List<GenreOfSeriesCacheDto>
)