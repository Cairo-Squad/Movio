package com.cairosquad.repository.artists.data_source.remote

import com.cairosquad.entity.Artist
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import java.time.LocalDate
import java.time.ZoneOffset


fun ArtistRemoteDto.toEntity(): Artist {
    return Artist(
        id = id?.toLong() ?: 0L,
        name = name ?: "",
        photoPath = profilePath ?: "",
        country = placeOfBirth ?: "",
        birthDate = birthday?.toMillisFromDate() ?: 0,
        biography = biography ?: "",
        department = department ?: "",
    )
}

@JvmName("toEntityArtist")
fun List<ArtistRemoteDto>.toEntityList(): List<Artist> {
    return map { it.toEntity() }
}
private fun String.toMillisFromDate(): Long {
    val localDate = LocalDate.parse(this) // Assumes input format yyyy-mm-dd
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}