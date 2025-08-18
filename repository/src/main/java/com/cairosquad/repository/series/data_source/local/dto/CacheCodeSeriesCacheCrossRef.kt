package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto


@Entity(
    tableName = "CacheCodeSeriesCacheCrossRef",
    primaryKeys = ["cacheCode", "series_id_language"]
)
data class CacheCodeSeriesCacheCrossRef(
    @ColumnInfo(name = "cacheCode")
    val cacheCode: String,
    @ColumnInfo(name = "series_id_language")
    val seriesIdWithLanguage: Long,
) {
    companion object{
        fun fromRequestAndSeriesList(
            cacheCode: CacheCodeDto,
            seriesList: List<SeriesCacheDto>
        ): List<CacheCodeSeriesCacheCrossRef> {
            return seriesList.map { series ->
                CacheCodeSeriesCacheCrossRef(
                    cacheCode.cacheCode,
                    series.seriesWithoutGenre.id
                )
            }
        }
    }
}
