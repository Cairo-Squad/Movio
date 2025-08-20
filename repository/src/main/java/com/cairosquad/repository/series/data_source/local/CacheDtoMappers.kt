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


fun List<Series>.toCacheCodeWithSeriesCacheDto(request: String, language: String): CacheCodeWithSeriesCacheDto {
    return CacheCodeWithSeriesCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        series = this.map { it.toCacheDto(language = language) }
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

fun Series.toCacheDto(language: String): SeriesCacheDto {
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
            seriesIdWithLanguage = "$id$language"
        ),
        genres = genres.toCacheDtoList(language = language)
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

fun Genre.toCacheDto(language: String): GenreOfSeriesCacheDto {
    return GenreOfSeriesCacheDto(
        id = id,
        name = name,
        genreIdWithLanguage = "$id$language",
        language = language
    )
}

@JvmName("toCacheGenre")
fun List<Genre>.toCacheDtoList(language: String): List<GenreOfSeriesCacheDto> {
    return map { it.toCacheDto(language = language) }
}

fun Season.toCacheDto(language: String): SeasonCacheDto {
    return SeasonCacheDto(
        id = seriesId * 1000L + seasonNumber,
        seriesId = seriesId,
        seasonNumber = seasonNumber,
        seasonName = seasonName,
        episodesCount = episodesCount,
        rating = rating,
        posterPath = posterPath,
        overview = overview,
        airDate = airDate,
        seasonIdWithLanguage = "${seriesId * 1000L + seasonNumber}$language"
    )
}

@JvmName("toCacheDtoListSeason")
fun List<Season>.toCacheDtoList(language: String): List<SeasonCacheDto> {
    return map { it.toCacheDto(language = language) }
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

fun Episode.toCacheDto(language: String): EpisodeCacheDto {
    return EpisodeCacheDto(
        id = id,
        episodeNumber = episodeNumber,
        photoPath = photoPath,
        episodeName = episodeName,
        runtimeMinutes = runtimeInMinutes,
        rating = rating,
        seasonNumber = seasonNumber,
        seriesId = seriesId,
        episodeIdWithLanguage = "$id$language"
    )
}

@JvmName("toCacheDtoListEpisode")
fun List<Episode>.toCacheDtoList(language: String): List<EpisodeCacheDto> {
    return map { it.toCacheDto(language = language) }
}

fun EpisodeCacheDto.toEntity(): Episode {
    return Episode(
        id = id,
        episodeNumber = episodeNumber,
        photoPath = photoPath,
        episodeName = episodeName,
        runtimeInMinutes = runtimeMinutes,
        rating = rating,
        seasonNumber = seasonNumber,
        seriesId = seriesId
    )
}

@JvmName("toEntityListEpisodeCacheDto")
fun List<EpisodeCacheDto>.toEntityList(): List<Episode> {
    return map { it.toEntity() }
}

fun List<Season>.toCacheCodeWithSeasonsCacheDto(request: String, language: String): CacheCodeWithSeasonsCacheDto {
    return CacheCodeWithSeasonsCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        seasons = this.toCacheDtoList(language = language)
    )
}

fun List<Episode>.toCacheCodeWithEpisodesCacheDto(request: String, language: String): CacheCodeWithEpisodesCacheDto {
    return CacheCodeWithEpisodesCacheDto(
        cacheCode = CacheCodeDto(cacheCode = request),
        episodes = this.toCacheDtoList(language)
    )
}