package com.cairosquad.repository.artists.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto

@Entity(
    tableName = "CacheCodeArtistCrossRef",
    primaryKeys = ["cacheCode", "artist_id_language"]
)
data class CacheCodeArtistCrossRef(
    @ColumnInfo(name = "cacheCode")
    val cacheCode: String,
    @ColumnInfo(name = "artist_id_language")
    val artistIdWithLanguage: String,
) {
    companion object {
        fun fromCacheCodeAndArtistList(
            cacheCode: CacheCodeDto,
            artists: List<ArtistCacheDto>
        ): List<CacheCodeArtistCrossRef> {
            return artists.map { artist ->
                CacheCodeArtistCrossRef(
                    cacheCode.cacheCode,
                    artist.artistIdWithLanguage
                )
            }
        }
    }
}