package com.cairosquad.repository.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.utils.mappers.tryToCall
import java.util.Date

class MoviesRepositoryImpl(
    private val remoteMovieDiscoveryDataSource: RemoteMovieDiscoveryDataSource,
    private val discoveryDataSource: DiscoveryDataSource
) : MoviesRepository {
    override suspend fun getMovie(movieId: Long): Movie {
        return fakeMovie
    }

    override suspend fun getMovieReviews(movieId: Long): List<Review> {
        return fakeReviewsList
    }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return List(10) { fakeMovie }
    }

    override suspend fun getMovieTopCast(movieId: Long): List<Artist> {
        return fakeArtists
    }

    override suspend fun getPersonalizedMovies(): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getPersonalizedMovies()
                .takeIf { it.size >= PAGE_SIZE }?.toEntity()
                ?: remoteMovieDiscoveryDataSource.getPersonalizedMovies().toEntity()
                    .also { result -> discoveryDataSource.cachePersonalizedMovies(result.toCacheDto()) }
        }
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getSuggestedMovies()
                .takeIf { it.size >= PAGE_SIZE }?.toEntity()
                ?: remoteMovieDiscoveryDataSource.getSuggestedMovies().toEntity()
                    .also { result -> discoveryDataSource.cacheSuggestedMovies(result.toCacheDto()) }
        }
    }

    companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
        private const val PAGE_SIZE = 20

        val fakeMovie = Movie(id = 157336L, title = "Interstellar", rating = 8.457f, posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", genres = listOf(Genre(id = 12L, name = "Adventure"), Genre(id = 18L, name = "Drama"), Genre(id = 878L, name = "Science Fiction")), overview = "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.", releaseDate = 1415145600000L, runtimeMinutes = 169); val fakeReviewsList: List<Review> = listOf(Review(id = 0L, author = "lmao7", authorPhotoPath = "/ekmYOUU4tfx9zGGadjRdE7UPce.jpg", rating = "9", date = 1487569648872L, description = "I started watching when it came out as I heard that fans of LOTR also liked this. I stopped watching after Season 1 as I was devastated lol kinda. Only 2015 I decided to continue watching and got addicted like it seemed complicated at first, too many stories and characters. I even used a guide from internet like family tree per house while watching or GOT wiki so I can have more background on the characters. For a TV series, this show can really take you to a different world and never knowing what will happen. It is very daring that any time anybody can just die (I learned not to be attached and have accepted that they will all die so I won't be devastated hehe). I have never read the books but the show is entertaining and you will really root for your faves and really hate on those you hate. \r\n\r\nFantasy, action, drama, comedy, love...and lots of surprises!"), Review(id = 1L, author = "Vlad Ulbricht", authorPhotoPath = "/srVsbbWgrmA4lmpqsrIYRYxJerc.jpg", rating = "9", date = 1494474799211L, description = "Cruel, bloody, vulgar, Machiavellian, unrepentant. And that is just the writing. The camera angles, the score, the pacing mesh together for grand storytelling: a mix of horror, swords and sorcery, and endless treachery. \r\n\r\nAnd all of that would be somewhat squandered if it wasn't for the best casting I've ever seen. From Lena Headey as soft spoken Cersei to Peter Vaughan as ancient Maester Aemon, each character pulses with depth and believability. Peter Dinklage may have sacrificed a virgin princess to get this role; I've never seen a better fit, not in size (though there is that) but in the way his eyes convey shrewd arrogance coupled with unabashed debauchery."),); val fakeArtists = listOf(Artist(id = 119783, name = "Joseph Mawle", photoPath = "/1Ocb9v3h54beGVoJMm4w50UQhLf.jpg", country = "United Kingdom", birthDate = 322704000000L, biography = "", department = "Acting"), Artist(id = 1050248, name = "Art Parkinson", photoPath = "/ejAKOJME1DsvHECLWdQ7dEtXyyc.jpg", country = "Ireland", birthDate = 1009843200000L, biography = "", department = "Acting"), Artist(id = 1223792, name = "Kristian Nairn", photoPath = "/dlbq6cCW0xdpFY15q6flP6lDXWV.jpg", country = "Australia", birthDate = 567648000000L, biography = "", department = "Acting"),);
    }
}