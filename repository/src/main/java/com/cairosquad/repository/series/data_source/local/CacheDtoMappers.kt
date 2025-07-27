package com.cairosquad.repository.series.data_source.local

import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithEpisodesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeasonsCacheDto
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.EpisodeCacheDto
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeasonCacheDto
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

fun Season.toCacheDto(): SeasonCacheDto {
    return SeasonCacheDto(
        id = seriesId * 1000L + seasonNumber, // Unique ID combining seriesId and seasonNumber
        seriesId = seriesId,
        seasonNumber = seasonNumber,
        seasonName = seasonName,
        episodesCount = episodesCount,
        rating = rating,
        posterPath = posterPath,
        overview = overview,
        airDate = airDate
    )
}


@JvmName("toCacheDtoListSeason")
fun List<Season>.toCacheDtoList(): List<SeasonCacheDto> {
    return map { it.toCacheDto() }
}

fun SeasonCacheDto.toEntity(): Season {
    return Season(
        seriesId = seriesId,
        seasonNumber = seasonNumber,
        seasonName = seasonName,
        episodesCount = episodesCount,
        rating = rating,
        posterPath = posterPath,
        overview = overview,
        airDate = airDate
    )
}

@JvmName("toEntityListSeasonCacheDto")
fun List<SeasonCacheDto>.toEntityList(): List<Season> {
    return map { it.toEntity() }
}

fun Episode.toCacheDto(): EpisodeCacheDto {
    return EpisodeCacheDto(
        id = id,
        episodeNumber = episodeNumber,
        photoPath = photoPath,
        episodeName = episodeName,
        runtimeMinutes = runtimeMinutes,
        rating = rating,
        seasonNumber = seasonNumber,
        seriesId = seriesId
    )
}

@JvmName("toCacheDtoListEpisode")
fun List<Episode>.toCacheDtoList(): List<EpisodeCacheDto> {
    return map { it.toCacheDto() }
}

fun EpisodeCacheDto.toEntity(): Episode {
    return Episode(
        id = id,
        episodeNumber = episodeNumber,
        photoPath = photoPath,
        episodeName = episodeName,
        runtimeMinutes = runtimeMinutes,
        rating = rating,
        seasonNumber = seasonNumber,
        seriesId = seriesId
    )
}

@JvmName("toEntityListEpisodeCacheDto")
fun List<EpisodeCacheDto>.toEntityList(): List<Episode> {
    return map { it.toEntity() }
}

fun List<Season>.toCacheCodeWithSeasonsCacheDto(request: String): CacheCodeWithSeasonsCacheDto {
    return CacheCodeWithSeasonsCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        seasons = this.toCacheDtoList()
    )
}

fun List<Episode>.toCacheCodeWithEpisodesCacheDto(request: String): CacheCodeWithEpisodesCacheDto {
    return CacheCodeWithEpisodesCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        episodes = this.toCacheDtoList()
    )
}