package com.cairosquad.repository.series

import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series

class SeriesRepositoryImpl : SeriesRepository {
    override suspend fun getSeries(seriesId: Long): Series {
        return fakeSeries
    }

    override suspend fun getSeriesReviews(seriesId: Long): List<Review> {
        return fakeReviewsList
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        return fakeSeasonsList
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<Episode> {
        return List(10) { fakeEpisode }
    }

    override suspend fun getSimilarSeries(seriesId: Long): List<Series> {
        return List(10) { fakeSeries }
    }

    override suspend fun getSeriesTopCast(seriesId: Long): List<Artist> {
        return fakeArtists
    }

    private companion object {
        val fakeSeries = Series(
            id = 1399,
            title = "Game of Thrones",
            rating = 8.456f,
            posterPath = "/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
            genres = listOf(
                Genre(10765, "Sci-Fi & Fantasy"),
                Genre(18, "Drama"),
                Genre(10759, "Action & Adventure")
            ),
            overview = "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
            releaseDate = 1752597611491L,
            seasonsCount = 9,
            trailerPath = ""
        )
        val fakeSeasonsList: List<Season> = listOf(
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
            Season(
                seasonNumber = 0,
                seasonName = "Specials",
                seriesId = 1399,
                episodesCount = 286,
                rating = 0.0f,
                posterPath = "/aos6lC1JGYt6ZRL85lgstNsfSeY.jpg",
                overview = "",
                airDate = 1291507200000L
            ),
            Season(
                seasonNumber = 1,
                seasonName = "Season 1",
                seriesId = 1399,
                episodesCount = 10,
                rating = 8.4f,
                posterPath = "/wgfKiqzuMrFIkU1M68DDDY8kGC1.jpg",
                overview = "Trouble is brewing in the Seven Kingdoms of Westeros. For the driven inhabitants of this visionary world, control of Westeros' Iron Throne holds the lure of great power. But in a land where the seasons can last a lifetime, winter is coming...and beyond the Great Wall that protects them, an ancient evil has returned. In Season One, the story centers on three primary areas: the Stark and the Lannister families, whose designs on controlling the throne threaten a tenuous peace; the dragon princess Daenerys, heir to the former dynasty, who waits just over the Narrow Sea with her malevolent brother Viserys; and the Great Wall--a massive barrier of ice where a forgotten danger is stirring.",
                airDate = 1302998400000L
            ),
        )
        val fakeReviewsList: List<Review> = listOf(
            Review(
                id = 0L,
                author = "lmao7",
                authorPhotoPath = "/ekmYOUU4tfx9zGGadjRdE7UPce.jpg",
                rating = "9",
                date = 1487569648872L,
                description = "I started watching when it came out as I heard that fans of LOTR also liked this. I stopped watching after Season 1 as I was devastated lol kinda. Only 2015 I decided to continue watching and got addicted like it seemed complicated at first, too many stories and characters. I even used a guide from internet like family tree per house while watching or GOT wiki so I can have more background on the characters. For a TV series, this show can really take you to a different world and never knowing what will happen. It is very daring that any time anybody can just die (I learned not to be attached and have accepted that they will all die so I won't be devastated hehe). I have never read the books but the show is entertaining and you will really root for your faves and really hate on those you hate. \r\n\r\nFantasy, action, drama, comedy, love...and lots of surprises!"
            ),
            Review(
                id = 1L,
                author = "Vlad Ulbricht",
                authorPhotoPath = "/srVsbbWgrmA4lmpqsrIYRYxJerc.jpg",
                rating = "9",
                date = 1494474799211L,
                description = "Cruel, bloody, vulgar, Machiavellian, unrepentant. And that is just the writing. The camera angles, the score, the pacing mesh together for grand storytelling: a mix of horror, swords and sorcery, and endless treachery. \r\n\r\nAnd all of that would be somewhat squandered if it wasn't for the best casting I've ever seen. From Lena Headey as soft spoken Cersei to Peter Vaughan as ancient Maester Aemon, each character pulses with depth and believability. Peter Dinklage may have sacrificed a virgin princess to get this role; I've never seen a better fit, not in size (though there is that) but in the way his eyes convey shrewd arrogance coupled with unabashed debauchery."
            )
        )
        val fakeEpisode = Episode(
            id = 63056L,
            episodeNumber = 1,
            photoPath = "/wrGWeW4WKxnaeA8sxJb2T9O6ryo.jpg",
            episodeName = "Winter Is Coming",
            runtimeMinutes = 62,
            rating = 8.08f,
            seasonNumber = 1,
            seriesId = 1399L
        )
        val fakeArtists = listOf(
            Artist(
                id = 119783,
                name = "Joseph Mawle",
                photoPath = "/1Ocb9v3h54beGVoJMm4w50UQhLf.jpg",
                country = "United Kingdom",
                birthDate = 322704000000L,
                biography = "",
                department = "Acting"
            ),
            Artist(
                id = 1050248,
                name = "Art Parkinson",
                photoPath = "/ejAKOJME1DsvHECLWdQ7dEtXyyc.jpg",
                country = "Ireland",
                birthDate = 1009843200000L,
                biography = "",
                department = "Acting"
            )
        )
    }
}