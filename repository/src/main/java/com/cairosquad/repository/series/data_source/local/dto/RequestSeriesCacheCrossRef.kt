package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.RequestCacheDto


@Entity(
    tableName = "RequestSeriesCacheCrossRef",
    primaryKeys = ["request", "series_id"]
)
data class RequestSeriesCacheCrossRef(
    @ColumnInfo(name = "request")
    val request: String,
    @ColumnInfo(name = "series_id")
    val seriesId: Long,
) {
    companion object{
        fun fromRequestAndSeriesList(
            request: RequestCacheDto,
            seriesList: List<SeriesCacheDto>
        ): List<RequestSeriesCacheCrossRef> {
            return seriesList.map { series ->
                RequestSeriesCacheCrossRef(
                    request.request,
                    series.seriesWithoutGenre.id
                )
            }
        }
    }
}
