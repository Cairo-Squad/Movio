package com.cairosquad.repository.movie.data_source.remote.dto

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class RemoteDtoTest {
    @Test
    fun `test MovieRemoteDto toEntity mapping`() {
        val movieRemoteDto = MovieRemoteDto(
            id = 1,
            title = "Movie 1",
            posterPath = "poster1.jpg",
            voteAverage = 8.5
        )

        val movieEntity = movieRemoteDto.toEntity()

        assertEquals(1, movieEntity.id)
        assertEquals("Movie 1", movieEntity.title)
        assertEquals("poster1.jpg", movieEntity.posterPath)
    }

    @Test
    fun `test MovieDetailsRemoteDto toEntity mapping`() {
        val movieDetailsRemoteDto = MovieDetailsRemoteDto(
            id = 1,
            title = "Movie 1",
            posterPath = "poster1.jpg",
            voteAverage = 8.5,
            genres = emptyList(),
            releaseDate = "2023-01-01",
        )

        val movieEntity = movieDetailsRemoteDto.toEntity("")

        assertEquals(1, movieEntity.id)
        assertEquals("Movie 1", movieEntity.title)
    }

    @Test
    fun `test GenreDto toEntity mapping`() {
        val genreDto = GenreDto(
            id = 1,
            name = "Action"
        )
        val genreEntity = genreDto.toEntity()
        assertEquals(1, genreEntity.id)
        assertEquals("Action", genreEntity.name)
    }

}