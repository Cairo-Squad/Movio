package com.cairosquad.repository.artists.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

@Entity(
    tableName = "CacheCodeArtistCrossRef",
    primaryKeys = ["cacheCode", "artist_id"]
)
data class CacheCodeArtistCrossRef(
    @ColumnInfo(name = "cacheCode")
    val cacheCode: String,
    @ColumnInfo(name = "artist_id")
    val artistId: Long,
) {
    companion object {
        fun fromCacheCodeAndArtistList(
            cacheCode: CacheCodeDto,
            artists: List<ArtistCacheDto>
        ): List<CacheCodeArtistCrossRef> {
            return artists.map { artist ->
                CacheCodeArtistCrossRef(
                    cacheCode.cacheCode,
                    artist.id
                )
            }
        }
    }
}