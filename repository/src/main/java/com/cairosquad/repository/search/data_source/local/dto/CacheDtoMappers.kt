package com.cairosquad.repository.search.data_source.local.dto

import com.cairosquad.entity.Artist
import java.util.Date

fun Artist.toCacheDto(query: String,page: Int): ArtistCacheDto {
    return ArtistCacheDto(
        id = id.toInt(),
        page =page ,
        name = name,
        photoPath = photoPath,
        query = query,
        timestamp = Date().time,
        country = country,
        birthDate = birthDate,
        biography = biography,
        department = department,
    )
}

@JvmName("toCacheArtistDto")
fun List<Artist>.toCacheDto(query: String,page: Int): List<ArtistCacheDto> {
    return map { it.toCacheDto(query,page) }
}

fun ArtistCacheDto.toEntity(): Artist {
    return Artist(
        id = id.toLong(),
        name = name ?: "",
        photoPath = photoPath ?: "",
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