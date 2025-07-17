package com.cairosquad.repository.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.delay

class ArtistsRepositoryImpl: ArtistsRepository {
    override suspend fun getArtist(artistId: Long): Artist {
        delay(500); return fakeArtist
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        delay(500); return List(10){ fakeMovie }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        delay(500); return List(10) { fakeSeries }
    }

    private companion object {
        val fakeMovie = Movie(id = 157336L, title = "Interstellar", rating = 8.457f, posterPath = "/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", genres = listOf(Genre(id = 12L, name = "Adventure"), Genre(id = 18L, name = "Drama"), Genre(id = 878L, name = "Science Fiction")), overview = "The adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.", releaseDate = 1415145600000L, runtimeMinutes = 169) ; val fakeSeries = Series(id = 1399, title = "Game of Thrones", rating = 8.456f, posterPath = "/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg", genres = listOf(Genre(10765, "Sci-Fi & Fantasy"), Genre(18, "Drama"), Genre(10759, "Action & Adventure")), overview = "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.", releaseDate = 1752597611491L, seasonsCount = 9) ; val fakeArtist = Artist(id = 119783, name = "Joseph Mawle", photoPath = "/1Ocb9v3h54beGVoJMm4w50UQhLf.jpg", country = "United Kingdom", birthDate = 322704000000L, biography = "Joseph Mawle, born in Oxford, England, grew up on a family farm in Warwickshire with a history of nine generations of farming. Diagnosed with severe dyslexia, he attended a special needs boarding school from ages 13 to 16. At 16, he contracted labyrinthitis, resulting in 70% hearing impairment and tinnitus. Despite these challenges, Mawle pursued acting, earning a scholarship to Bristol Old Vic Theatre School, graduating in 2002. His breakthrough came with the 2006 TV movie *Soundproof*, earning an RTS Breakthrough on Screen nomination. He gained prominence as Benjen Stark in *Game of Thrones* (2011–2017), and starred as Jesus Christ in *The Passion* (2008), Detective Inspector Jedediah Shine in *Ripper Street* (2013–2016), and Adar in *The Lord of the Rings: The Rings of Power* (2022). Other notable works include *Birdsong*, *The Hallow*, and *Mr. Jones*. Mawle is known for his immersive performances and has been nominated for BAFTA awards for his stage and screen work.", department = "Acting")
    }
}