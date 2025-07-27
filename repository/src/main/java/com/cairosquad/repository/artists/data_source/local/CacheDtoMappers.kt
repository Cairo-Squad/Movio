package com.cairosquad.repository.artists.data_source.local

import com.cairosquad.entity.Artist
import com.cairosquad.repository.artists.data_source.local.dto.ArtistCacheDto
import java.util.Date

fun Artist.toCacheDto(): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        name = name,
        photoPath = photoPath,
        cachingTimestamp = Date().time,
        country = country,
        birthDate = birthDate,
        biography = biography,
        department = department,
    )
}

@JvmName("toCacheArtistDto")
fun List<Artist>.toCacheDto(): List<ArtistCacheDto> {
    return map { it.toCacheDto() }
}

fun ArtistCacheDto.toEntity(): Artist {
    return Artist(
        id = id.toLong(),
        name = name,
        photoPath = photoPath,
        country = country,
        birthDate = birthDate,
        biography = biography,
        department = department,
    )
}

@JvmName("toEntityArtist")
fun List<ArtistCacheDto>.toEntity(): List<Artist> {
    return map { it.toEntity() }
}