//package com.cairosquad.repository.utils.mappers
//
//import com.google.common.truth.Truth.assertThat
//import org.junit.Test
//
//class MovieCacheDtoExtensionsTest {
//    @Test
//    fun `toPersonalizedMoviesIdsDto should map MovieCacheDto to PersonalizedMoviesIdsDto`() {
//        val movieCacheDto = MovieCacheDto(
//            id = 42,
//            title = "movie name",
//            posterPath = "/posterPath.jpg",
//            voteAverage = 4.5,
//            timestamp = 341324L,
//            page = 1,
//            query = ""
//        )
//
//        val result = movieCacheDto.toPersonalizedMoviesIdsDto()
//
//        assertThat(result).isEqualTo(PersonalizedMoviesIdsDto(42))
//    }
//
//    @Test
//    fun `toPersonalizedMoviesIdsDto should map list of MovieCacheDto to list of PersonalizedMoviesIdsDto`() {
//        val movieCacheList = listOf(
//            MovieCacheDto(
//                id = 1,
//                title = "movie name",
//                posterPath = "/posterPath.jpg",
//                voteAverage = 4.5,
//                timestamp = 341324L,
//                page = 1,
//                query = ""
//            ),
//            MovieCacheDto(
//                id = 2,
//                title = "movie name",
//                posterPath = "/posterPath.jpg",
//                voteAverage = 4.5,
//                timestamp = 341324L,
//                page = 1,
//                query = ""
//            ),
//            MovieCacheDto(
//                id = 3,
//                title = "movie name",
//                posterPath = "/posterPath.jpg",
//                voteAverage = 4.5,
//                timestamp = 341324L,
//                page = 1,
//                query = ""
//            )
//        )
//
//        val result = movieCacheList.toPersonalizedMoviesIdsDtoList()
//
//        assertThat(result).containsExactly(
//            PersonalizedMoviesIdsDto(1),
//            PersonalizedMoviesIdsDto(2),
//            PersonalizedMoviesIdsDto(3)
//        ).inOrder()
//    }
//
//    @Test
//    fun `toSuggestedMoviesIds should map MovieCacheDto to SuggestedMoviesIdsDto`() {
//        val movieCacheDto = MovieCacheDto(
//            id = 99,
//            title = "movie name",
//            posterPath = "/posterPath.jpg",
//            voteAverage = 4.5,
//            timestamp = 341324L,
//            page = 1,
//            query = ""
//        )
//
//        val result = movieCacheDto.toSuggestedMoviesIds()
//
//        assertThat(result).isEqualTo(SuggestedMoviesIdsDto(99))
//    }
//
//    @Test
//    fun `toSuggestedMoviesIds should map list of MovieCacheDto to list of SuggestedMoviesIdsDto`() {
//        val movieCacheList = listOf(
//            MovieCacheDto(
//                id = 7,
//                title = "movie name",
//                posterPath = "/posterPath.jpg",
//                voteAverage = 4.5,
//                timestamp = 341324L,
//                page = 1,
//                query = ""
//            ),
//            MovieCacheDto(
//                id = 8,
//                title = "movie name",
//                posterPath = "/posterPath.jpg",
//                voteAverage = 4.5,
//                timestamp = 341324L,
//                page = 1,
//                query = ""
//            ),
//            MovieCacheDto(
//                id = 9,
//                title = "movie name",
//                posterPath = "/posterPath.jpg",
//                voteAverage = 4.5,
//                timestamp = 341324L,
//                page = 1,
//                query = ""
//            )
//        )
//
//        val result = movieCacheList.toSuggestedMoviesIdsList()
//
//        assertThat(result).containsExactly(
//            SuggestedMoviesIdsDto(7),
//            SuggestedMoviesIdsDto(8),
//            SuggestedMoviesIdsDto(9)
//        ).inOrder()
//    }
//}