package com.cairosquad.local.search.discovery.dto

import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto


fun MovieCacheDto.toPersonalizedMoviesIdsDto(): PersonalizedMoviesIdsDto {
    return PersonalizedMoviesIdsDto(id)
}

fun List<MovieCacheDto>.toPersonalizedMoviesIdsDto(): List<PersonalizedMoviesIdsDto> {
    return map { it.toPersonalizedMoviesIdsDto() }
}

fun MovieCacheDto.toSuggestedMoviesIds(): SuggestedMoviesIdsDto {
    return SuggestedMoviesIdsDto(id)
}

fun List<MovieCacheDto>.toSuggestedMoviesIds(): List<SuggestedMoviesIdsDto> {
    return map { it.toSuggestedMoviesIds() }
}