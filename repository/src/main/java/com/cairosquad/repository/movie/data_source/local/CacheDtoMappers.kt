package com.cairosquad.repository.movie.data_source.local

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieWithoutGenreCacheDto
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto


fun List<Movie>.toCacheCodeWithMoviesCacheDto(request: String, language: String): CacheCodeWithMoviesCacheDto {
    return CacheCodeWithMoviesCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        movies = this.map { it.toCacheDto(language = language) }
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

fun Movie.toCacheDto(language: String): MovieCacheDto {
    return MovieCacheDto(
        movieWithoutGenre = MovieWithoutGenreCacheDto(
            id = id,
            title = title,
            posterPath = posterPath,
            rating = rating,
            overview = overview,
            releaseDate = releaseDate,
            runtime = runtimeMinutes,
            trailerPath = trailerPath,
            movieIdWithLanguage = id.toString() + language
        ),
        genres = genres.toCacheDtoList(language = language)
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

fun Genre.toCacheDto(language: String): GenreOfMovieCacheDto {
    return GenreOfMovieCacheDto(
        id = id,
        name = name,
        genreIdWithLanguage = id.toString() + language
    )
}

@JvmName("toCacheGenre")
fun List<Genre>.toCacheDtoList(language: String): List<GenreOfMovieCacheDto> {
    return map { it.toCacheDto(language = language) }
}