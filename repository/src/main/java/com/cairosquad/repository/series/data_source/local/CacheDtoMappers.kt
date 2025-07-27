package com.cairosquad.repository.series.data_source.local

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Series
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesWithoutGenreCacheDto
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeDto


fun List<Series>.toCacheCodeWithSeriesCacheDto(request: String): CacheCodeWithSeriesCacheDto {
    return CacheCodeWithSeriesCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        series = this.map { it.toCacheDto() }
    )
}


fun SeriesCacheDto.toEntity(): Series {
    return Series(
        id = seriesWithoutGenre.id,
        title = seriesWithoutGenre.title,
        posterPath = seriesWithoutGenre.posterPath,
        rating = seriesWithoutGenre.rating,
        trailerPath = seriesWithoutGenre.trailerPath,
        genres = genres.toEntityList(),
        overview = seriesWithoutGenre.overview,
        releaseDate = seriesWithoutGenre.releaseDate,
        seasonsCount = seriesWithoutGenre.seasonsCount
    )
}

@JvmName("toEntitySeries")
fun List<SeriesCacheDto>.toEntityList(): List<Series> {
    return map { it.toEntity() }
}

fun Series.toCacheDto(): SeriesCacheDto {
    return SeriesCacheDto(
        seriesWithoutGenre = SeriesWithoutGenreCacheDto(
            id = id,
            title = title,
            posterPath = posterPath,
            rating = rating,
            overview = overview,
            releaseDate = releaseDate,
            trailerPath = trailerPath,
            seasonsCount = seasonsCount,
        ),
        genres = genres.toCacheDtoList()
    )
}

fun GenreOfSeriesCacheDto.toEntity(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

@JvmName("toEntityGenre")
fun List<GenreOfSeriesCacheDto>.toEntityList(): List<Genre> {
    return map { it.toEntity() }
}

fun Genre.toCacheDto(): GenreOfSeriesCacheDto {
    return GenreOfSeriesCacheDto(
        id = id,
        name = name
    )
}

@JvmName("toCacheGenre")
fun List<Genre>.toCacheDtoList(): List<GenreOfSeriesCacheDto> {
    return map { it.toCacheDto() }
}