package com.cairosquad.repository.movie.data_source.local.dto

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.repository.utils.sharedDto.local.RequestCacheDto


fun List<Movie>.toRequestWithMoviesCacheDto(request: String): RequestWithMoviesCacheDto {
    return RequestWithMoviesCacheDto(
        request = RequestCacheDto(request = request),
        movies = this.map { it.toCacheDto() }
    )
}


fun MovieCacheDto.toEntity(): Movie {
    return Movie(
        id = movieWithoutGenre.id.toLong(),
        title = movieWithoutGenre.title,
        posterPath = movieWithoutGenre.posterPath ?: "",
        rating = movieWithoutGenre.voteAverage,
        trailerPath = movieWithoutGenre.trailerPath,
        genres = genres.toEntityList(),
        overview = movieWithoutGenre.overview,
        releaseDate = movieWithoutGenre.releaseDate,
        runtimeMinutes = movieWithoutGenre.runtime ?: 0,
    )
}

@JvmName("toEntityMovie")
fun List<MovieCacheDto>.toEntityList(): List<Movie> {
    return map { it.toEntity() }
}

fun Movie.toCacheDto(): MovieCacheDto {
    return MovieCacheDto(
        movieWithoutGenre = MovieWithoutGenreCacheDto(
            id = id.toLong(),
            title = title,
            posterPath = posterPath,
            voteAverage = rating,
            overview = overview,
            releaseDate = releaseDate,
            runtime = runtimeMinutes,
            trailerPath = trailerPath
        ),
        genres = genres.toCacheDtoList()
    )
}

fun MovieGenreCacheDto.toEntity(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

@JvmName("toEntityGenre")
fun List<MovieGenreCacheDto>.toEntityList(): List<Genre> {
    return map { it.toEntity() }
}

fun Genre.toCacheDto(): MovieGenreCacheDto {
    return MovieGenreCacheDto(
        id = id,
        name = name
    )
}

@JvmName("toCacheGenre")
fun List<Genre>.toCacheDtoList(): List<MovieGenreCacheDto> {
    return map { it.toCacheDto() }
}