package com.cairosquad.repository.artists

import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.artists.dto.ArtistMovieCachedDto
import com.cairosquad.repository.artists.dto.ArtistSeriesCachedDto

fun List<Movie>.toArtistMoviesCachedDto(artistId: Long): List<ArtistMovieCachedDto> {
    return map { ArtistMovieCachedDto(movieId = it.id, artistId = artistId) }
}

fun List<Series>.toArtistSeriesCachedDto(artistId: Long): List<ArtistSeriesCachedDto> {
    return map { ArtistSeriesCachedDto(seriesId = it.id, artistId = artistId) }
}