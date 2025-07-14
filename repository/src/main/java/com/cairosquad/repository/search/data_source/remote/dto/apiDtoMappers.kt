package com.cairosquad.repository.search.data_source.remote.dto

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series


fun ApiArtistDto.toEntity(): Artist {
    return Artist(
        id = id?.toLong() ?: 0L,
        name = name ?: "",
        photoPath = profilePath ?: ""
    )
}

fun ApiMovieDto.toEntity(): Movie {
    return Movie(
        id = id?.toLong() ?: 0L,
        title = title ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath ?: "",
    )
}

fun ApiSeriesDto.toEntity(): Series {
    return Series(
        id = id ?: 0L,
        title = name ?: "",
        rating = voteAverage?.toFloat() ?: 0f,
        posterPath = posterPath ?: "",
    )
}
