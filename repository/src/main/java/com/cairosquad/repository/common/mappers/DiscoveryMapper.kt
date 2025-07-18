package com.cairosquad.repository.common.mappers

import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.PersonalizedMoviesIdsDto
import com.cairosquad.repository.search.data_source.local.dto.SuggestedMoviesIdsDto


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