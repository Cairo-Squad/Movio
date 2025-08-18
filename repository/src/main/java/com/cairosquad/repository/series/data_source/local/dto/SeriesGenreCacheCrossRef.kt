package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "SeriesGenreCacheCrossRef",
    primaryKeys = ["series_id_language", "genre_id_language"]
)
data class SeriesGenreCacheCrossRef(
    @ColumnInfo(name = "series_id_language")
    val seriesId: Long,
    @ColumnInfo(name = "genre_id_language")
    val genreId: Long
) {
    companion object {
        fun fromSeries(
            series: SeriesCacheDto
        ): List<SeriesGenreCacheCrossRef> {
            return series.genres.map { genre ->
                SeriesGenreCacheCrossRef(
                    series.seriesWithoutGenre.id,
                    genre.id
                )
            }
        }
    }
}