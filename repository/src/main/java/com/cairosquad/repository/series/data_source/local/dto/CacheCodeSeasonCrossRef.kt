package com.cairosquad.repository.series.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

@Entity(
    tableName = "CacheCodeSeasonCrossRef",
    primaryKeys = ["cacheCode", "season_id"]
)
data class CacheCodeSeasonCrossRef(
    @ColumnInfo(name = "cacheCode") val cacheCode: String,
    @ColumnInfo(name = "season_id") val seasonId: Long
) {
    companion object {
        fun fromCacheCodeAndSeasonList(
            cacheCode: CacheCodeDto,
            seasons: List<SeasonCacheDto>
        ): List<CacheCodeSeasonCrossRef> {
            return seasons.map { season ->
                CacheCodeSeasonCrossRef(
                    cacheCode = cacheCode.cacheCode,
                    seasonId = season.id
                )
            }
        }
    }
}
