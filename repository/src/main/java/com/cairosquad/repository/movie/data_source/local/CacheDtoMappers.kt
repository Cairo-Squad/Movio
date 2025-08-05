package com.cairosquad.repository.movie.data_source.local

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto


fun List<Movie>.toCacheCodeWithMoviesCacheDto(request: String): CacheCodeWithMoviesCacheDto {
    return CacheCodeWithMoviesCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        movies = this.map { it.toCacheDto() }
    )
}


fun MovieCacheDto.toEntity(): Movie {
    return Movie(
        id = movieWithoutGenre.id,
        title = movieWithoutGenre.title,
        posterPath = movieWithoutGenre.posterPath ?: "",
        rating = movieWithoutGenre.rating,
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
            id = id,
            title = title,
            posterPath = posterPath,
            rating = rating,
            overview = overview,
            releaseDate = releaseDate,
            runtime = runtimeMinutes,
            trailerPath = trailerPath
        ),
        genres = genres.toCacheDtoList()
    )
}

fun GenreOfMovieCacheDto.toEntity(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

@JvmName("toEntityGenre")
fun List<GenreOfMovieCacheDto>.toEntityList(): List<Genre> {
    return map { it.toEntity() }
}

fun Genre.toCacheDto(): GenreOfMovieCacheDto {
    return GenreOfMovieCacheDto(
        id = id,
        name = name
    )
}

@JvmName("toCacheGenre")
fun List<Genre>.toCacheDtoList(): List<GenreOfMovieCacheDto> {
    return map { it.toCacheDto() }
}