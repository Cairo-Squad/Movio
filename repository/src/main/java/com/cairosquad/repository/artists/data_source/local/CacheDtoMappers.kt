package com.cairosquad.repository.artists.data_source.local

import com.cairosquad.entity.Artist
import com.cairosquad.repository.artists.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.artists.data_source.local.dto.CacheCodeWithArtistsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto
import java.util.Date

fun Artist.toCacheDto(language: String): ArtistCacheDto {
    return ArtistCacheDto(
        id = id,
        name = name,
        photoPath = photoPath,
        cachingTimestamp = Date().time,
        country = country,
        birthDate = birthDate,
        biography = biography,
        department = department,
        artistIdWithLanguage = "$id$language"
    )
}

@JvmName("toCacheArtistDto")
fun List<Artist>.toCacheDtoList(language: String): List<ArtistCacheDto> {
    return map { it.toCacheDto(language = language) }
}

fun ArtistCacheDto.toEntity(): Artist {
    return Artist(
        id = id,
        name = name,
        photoPath = photoPath,
        country = country,
        birthDate = birthDate,
        biography = biography,
        department = department,
    )
}

@JvmName("toEntityArtist")
fun List<ArtistCacheDto>.toEntityList(): List<Artist> {
    return map { it.toEntity() }
}

fun List<Artist>.toCacheCodeWithArtistsCacheDto(cacheCode: String, language: String): CacheCodeWithArtistsCacheDto {
    return CacheCodeWithArtistsCacheDto(
        cacheCode = CacheCodeDto(cacheCode = cacheCode),
        artists = this.map { it.toCacheDto(language = language) }
    )
}