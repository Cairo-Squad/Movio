package com.cairosquad.repository.movie.data_source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.repository.utils.RequestCacheDto
import java.util.Date

@Entity(tableName = "MovieWithoutGenreCacheDto")
data class MovieWithoutGenreCacheDto(
    @ColumnInfo(name = "movie_id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: Long,
    @ColumnInfo(name = "runtime")
    val runtime: Int?,
    @ColumnInfo(name = "trailerPath")
    val trailerPath: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Float,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = Date().time,
)

@Entity(tableName = "GenreCacheDtoNew")
data class GenreCacheDtoNew(
    @ColumnInfo(name = "genre_id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "timestamp")
val timestamp: Long = Date().time,
)

@Entity(
    tableName = "MovieGenreCacheCrossRef",
    primaryKeys = ["movie_id", "genre_id"]
)
data class MovieGenreCacheCrossRef(
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
    @ColumnInfo(name = "genre_id")
    val genreId: Long
)


data class MovieCacheDtoNew(
    @Embedded
    val movieWithoutGenre: MovieWithoutGenreCacheDto,
    @Relation(
        parentColumn = "movie_id",
        entityColumn = "genre_id",
        associateBy = Junction(MovieGenreCacheCrossRef::class)
    )
    val genres: List<GenreCacheDtoNew>
)

@Entity(
    tableName = "RequestMovieCacheCrossRef",
    primaryKeys = ["request", "movie_id"]
)
data class RequestMovieCacheCrossRef(
    @ColumnInfo(name = "request")
    val request: String,
    @ColumnInfo(name = "movie_id")
    val movieId: Long,
) {
    companion object{
        fun fromRequestAndMovieList(
            request: RequestCacheDto,
            movies: List<MovieCacheDtoNew>
        ): List<RequestMovieCacheCrossRef> {
            return movies.map { movie ->
                RequestMovieCacheCrossRef(
                    request.request,
                    movie.movieWithoutGenre.id
                )
            }
        }
    }
}

data class RequestWithMoviesCacheDto(
    @Embedded
    val request: RequestCacheDto,
    @Relation(
        parentColumn = "request",
        entity = MovieWithoutGenreCacheDto::class,
        entityColumn = "movie_id",
        associateBy = Junction(RequestMovieCacheCrossRef::class)
    )
    val movies: List<MovieCacheDtoNew>,
)

fun List<Movie>.toRequestWithMoviesCacheDto(request: String): RequestWithMoviesCacheDto {
    return RequestWithMoviesCacheDto(
        request = RequestCacheDto(request = request),
        movies = this.map { it.toCacheDtoNew() }
    )
}

fun GenreCacheDtoNew.toEntity(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

@JvmName("toEntityGenre")
fun List<GenreCacheDtoNew>.toEntityList(): List<Genre> {
    return map { it.toEntity() }
}

fun MovieCacheDtoNew.toEntity(): Movie {
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
fun List<MovieCacheDtoNew>.toEntityList(): List<Movie> {
    return map { it.toEntity() }
}

fun Movie.toCacheDtoNew(): MovieCacheDtoNew {
    return MovieCacheDtoNew(
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
        genres = genres.map { GenreCacheDtoNew(it.id, it.name) }
    )
}

@JvmName("toCacheDtoListMovieNew")
fun List<Movie>.toCacheDtoList(): List<MovieCacheDtoNew> {
    return map { it.toCacheDtoNew() }
}