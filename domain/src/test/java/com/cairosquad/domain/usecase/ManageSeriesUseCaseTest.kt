package com.cairosquad.domain.usecase

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ManageSeriesUseCaseTest {

    private lateinit var seriesRepository: SeriesRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var artistRepository: ArtistsRepository
    private lateinit var useCase: ManageSeriesUseCase

    @BeforeTest
    fun setUp() {
        seriesRepository = mockk()
        searchRepository = mockk()
        artistRepository = mockk()
        useCase = ManageSeriesUseCase(
            seriesRepository,
            searchRepository,
            artistRepository
        )
    }

    @Test
    fun `should return expectedSeriesSciFi and add query when getSeriesByQuery is called`() = runTest {
        val query = "sci-fi"
        val page = 1
        coEvery { seriesRepository.getSeriesByQuery(query, page) } returns expectedSeriesSciFi
        coEvery { searchRepository.addQuery(query) } returns Unit

        val result = useCase.getSeriesByQuery(query, page)

        assertEquals(expectedSeriesSciFi, result)
        coVerify { seriesRepository.getSeriesByQuery(query, page) }
        coVerify { searchRepository.addQuery(query) }
    }

    @Test
    fun `should throw RuntimeException when getSeriesByQuery fails`() = runTest {
        val query = "sci-fi"
        val page = 1
        val exception = RuntimeException("Search failed")
        coEvery { seriesRepository.getSeriesByQuery(query, page) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getSeriesByQuery(query, page)
        }

        assertEquals("Search failed", thrown.message)
        coVerify { seriesRepository.getSeriesByQuery(query, page) }
        coVerify(exactly = 0) { searchRepository.addQuery(query) }
    }

    @Test
    fun `should return expectedSeriesSorted when getAllSeries is called with category and sort type`() = runTest {
        val page = 1
        val genreId = 10765L
        val sortType = SortType.LATEST
        coEvery { seriesRepository.getAllSeries(page, genreId, sortType) } returns expectedSeriesSorted

        val result = useCase.getAllSeries(page, genreId, sortType)

        assertEquals(expectedSeriesSorted, result)
        coVerify { seriesRepository.getAllSeries(page, genreId, sortType) }
    }

    @Test
    fun `should return expectedSeriesUnfiltered when getAllSeries is called with null category and sort`() = runTest {
        val page = 2
        val genreId: Long? = null
        val sortType: SortType? = null
        coEvery { seriesRepository.getAllSeries(page, genreId, sortType) } returns expectedSeriesUnfiltered

        val result = useCase.getAllSeries(page, genreId, sortType)

        assertEquals(expectedSeriesUnfiltered, result)
        coVerify { seriesRepository.getAllSeries(page, genreId, sortType) }
    }

    @Test
    fun `should return expectedSeriesUnfiltered when getAllSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getAllSeries(page, null, null) } returns expectedSeriesUnfiltered

        val result = useCase.getAllSeries(page)

        assertEquals(expectedSeriesUnfiltered, result)
        coVerify { seriesRepository.getAllSeries(page, null, null) }
    }

    @Test
    fun `should throw RuntimeException when getAllSeries fails`() = runTest {
        val page = 3
        val genreId = 18L
        val sortType = SortType.POPULAR
        val exception = RuntimeException("Failed to fetch series")
        coEvery { seriesRepository.getAllSeries(page, genreId, sortType) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getAllSeries(page, genreId, sortType)
        }

        assertEquals("Failed to fetch series", thrown.message)
        coVerify { seriesRepository.getAllSeries(page, genreId, sortType) }
    }

    @Test
    fun `should return expectedSeriesWithCategory when getAiringTodaySeries is called with category`() = runTest {
        val page = 1
        val genreId = 10759L
        coEvery { seriesRepository.getAiringTodaySeries(page, genreId) } returns expectedSeriesWithCategory

        val result = useCase.getAiringTodaySeries(page, genreId)

        assertEquals(expectedSeriesWithCategory, result)
        coVerify { seriesRepository.getAiringTodaySeries(page, genreId) }
    }

    @Test
    fun `should return expectedSeriesWithoutCategory when getAiringTodaySeries is called with null category`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getAiringTodaySeries(page, genreId) } returns expectedSeriesWithoutCategory

        val result = useCase.getAiringTodaySeries(page, genreId)

        assertEquals(expectedSeriesWithoutCategory, result)
        coVerify { seriesRepository.getAiringTodaySeries(page, genreId) }
    }

    @Test
    fun `should return expectedSeriesWithoutCategory when getAiringTodaySeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getAiringTodaySeries(page, null) } returns expectedSeriesWithoutCategory

        val result = useCase.getAiringTodaySeries(page)

        assertEquals(expectedSeriesWithoutCategory, result)
        coVerify { seriesRepository.getAiringTodaySeries(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getAiringTodaySeries fails`() = runTest {
        val page = 3
        val genreId = 18L
        val exception = RuntimeException("Failed to fetch airing today series")
        coEvery { seriesRepository.getAiringTodaySeries(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getAiringTodaySeries(page, genreId)
        }

        assertEquals("Failed to fetch airing today series", thrown.message)
        coVerify { seriesRepository.getAiringTodaySeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithCategory when getFreeToWatchSeries is called with category`() = runTest {
        val page = 1
        val genreId = 16L
        coEvery { seriesRepository.getFreeToWatchSeries(page, genreId) } returns expectedWithCategory

        val result = useCase.getFreeToWatchSeries(page, genreId)

        assertEquals(expectedWithCategory, result)
        coVerify { seriesRepository.getFreeToWatchSeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithoutCategory when getFreeToWatchSeries is called with null category`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getFreeToWatchSeries(page, genreId) } returns expectedWithoutCategory

        val result = useCase.getFreeToWatchSeries(page, genreId)

        assertEquals(expectedWithoutCategory, result)
        coVerify { seriesRepository.getFreeToWatchSeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithoutCategory when getFreeToWatchSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getFreeToWatchSeries(page, null) } returns expectedWithoutCategory

        val result = useCase.getFreeToWatchSeries(page)

        assertEquals(expectedWithoutCategory, result)
        coVerify { seriesRepository.getFreeToWatchSeries(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getFreeToWatchSeries fails`() = runTest {
        val page = 3
        val genreId = 35L
        val exception = RuntimeException("Failed to fetch free-to-watch series")
        coEvery { seriesRepository.getFreeToWatchSeries(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getFreeToWatchSeries(page, genreId)
        }

        assertEquals("Failed to fetch free-to-watch series", thrown.message)
        coVerify { seriesRepository.getFreeToWatchSeries(page, genreId) }
    }

    @Test
    fun `should return expectedRecommendedWithCategory when getMoreRecommendedSeries is called with category`() = runTest {
        val page = 1
        val genreId = 9648L
        coEvery { seriesRepository.getMoreRecommendedSeries(page, genreId) } returns expectedRecommendedWithCategory

        val result = useCase.getMoreRecommendedSeries(page, genreId)

        assertEquals(expectedRecommendedWithCategory, result)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, genreId) }
    }

    @Test
    fun `should return expectedRecommendedWithoutCategory when getMoreRecommendedSeries is called with null category`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getMoreRecommendedSeries(page, genreId) } returns expectedRecommendedWithoutCategory

        val result = useCase.getMoreRecommendedSeries(page, genreId)

        assertEquals(expectedRecommendedWithoutCategory, result)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, genreId) }
    }

    @Test
    fun `should return expectedRecommendedWithoutCategory when getMoreRecommendedSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getMoreRecommendedSeries(page, null) } returns expectedRecommendedWithoutCategory

        val result = useCase.getMoreRecommendedSeries(page)

        assertEquals(expectedRecommendedWithoutCategory, result)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getMoreRecommendedSeries fails`() = runTest {
        val page = 3
        val genreId = 10751L
        val exception = RuntimeException("Failed to fetch recommended series")
        coEvery { seriesRepository.getMoreRecommendedSeries(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getMoreRecommendedSeries(page, genreId)
        }

        assertEquals("Failed to fetch recommended series", thrown.message)
        coVerify { seriesRepository.getMoreRecommendedSeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithGenre when getOnTvSeries is called with genre`() = runTest {
        val page = 1
        val genreId = 10766L
        coEvery { seriesRepository.getOnTvSeries(page, genreId) } returns expectedWithGenre

        val result = useCase.getOnTvSeries(page, genreId)

        assertEquals(expectedWithGenre, result)
        coVerify { seriesRepository.getOnTvSeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithoutGenre when getOnTvSeries is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getOnTvSeries(page, genreId) } returns expectedWithoutGenre

        val result = useCase.getOnTvSeries(page, genreId)

        assertEquals(expectedWithoutGenre, result)
        coVerify { seriesRepository.getOnTvSeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithoutGenre when getOnTvSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getOnTvSeries(page, null) } returns expectedWithoutGenre

        val result = useCase.getOnTvSeries(page)

        assertEquals(expectedWithoutGenre, result)
        coVerify { seriesRepository.getOnTvSeries(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getOnTvSeries fails`() = runTest {
        val page = 3
        val genreId = 99L
        val exception = RuntimeException("Failed to fetch On TV series")
        coEvery { seriesRepository.getOnTvSeries(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getOnTvSeries(page, genreId)
        }

        assertEquals("Failed to fetch On TV series", thrown.message)
        coVerify { seriesRepository.getOnTvSeries(page, genreId) }
    }

    @Test
    fun `should return expectedWithCategory when getPopularSeries is called with category`() = runTest {
        val page = 1
        val genreId = 80L
        coEvery { seriesRepository.getPopularSeries(page, genreId) } returns expectedPopularWithCategory

        val result = useCase.getPopularSeries(page, genreId)

        assertEquals(expectedPopularWithCategory, result)
        coVerify { seriesRepository.getPopularSeries(page, genreId) }
    }

    @Test
    fun `should return expectedPopularWithoutCategory when getPopularSeries is called with null category`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getPopularSeries(page, genreId) } returns expectedPopularWithoutCategory

        val result = useCase.getPopularSeries(page, genreId)

        assertEquals(expectedPopularWithoutCategory, result)
        coVerify { seriesRepository.getPopularSeries(page, genreId) }
    }

    @Test
    fun `should return expectedPopularWithoutCategory when getPopularSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getPopularSeries(page, null) } returns expectedPopularWithoutCategory

        val result = useCase.getPopularSeries(page)

        assertEquals(expectedPopularWithoutCategory, result)
        coVerify { seriesRepository.getPopularSeries(page, null) }
    }

    @Test
    fun `should throw RuntimeException when getPopularSeries fails`() = runTest {
        val page = 3
        val genreId = 10767L
        val exception = RuntimeException("Failed to fetch popular series")
        coEvery { seriesRepository.getPopularSeries(page, genreId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getPopularSeries(page, genreId)
        }

        assertEquals("Failed to fetch popular series", thrown.message)
        coVerify { seriesRepository.getPopularSeries(page, genreId) }
    }

    @Test
    fun `should return expectedSeries when getSeriesByCategory is called`() = runTest {
        val genreId = 10768L
        val page = 1
        coEvery { seriesRepository.getSeriesByCategory(genreId, page) } returns expectedSeries

        val result = useCase.getSeriesByCategory(genreId, page)

        assertEquals(expectedSeries, result)
        coVerify { seriesRepository.getSeriesByCategory(genreId, page) }
    }

    @Test
    fun `should throw RuntimeException when getSeriesByCategory fails`() = runTest {
        val genreId = 99L
        val page = 2
        val exception = RuntimeException("Failed to load series by category")
        coEvery { seriesRepository.getSeriesByCategory(genreId, page) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getSeriesByCategory(genreId, page)
        }

        assertEquals("Failed to load series by category", thrown.message)
        coVerify { seriesRepository.getSeriesByCategory(genreId, page) }
    }

    @Test
    fun `should return series when getSeriesById is called`() = runTest {
        val seriesId = 1L
        coEvery { seriesRepository.getSeriesById(seriesId) } returns series1

        val result = useCase.getSeriesById(seriesId)

        assertThat(result).isEqualTo(series1)
        coVerify(exactly = 1) { seriesRepository.getSeriesById(seriesId) }
    }

    @Test
    fun `should throw RuntimeException when getSeriesById fails`() = runTest {
        val seriesId = 1L
        val exception = RuntimeException("Failed to fetch")
        coEvery { seriesRepository.getSeriesById(seriesId) } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getSeriesById(seriesId)
        }

        assertEquals("Failed to fetch", thrown.message)
        coVerify(exactly = 1) { seriesRepository.getSeriesById(seriesId) }
    }

    @Test
    fun `should return reviews when getSeriesReviews is called`() = runTest {
        val seriesId = 1L
        val page = 1
        coEvery { seriesRepository.getSeriesReviews(seriesId, page) } returns listOf(review)

        val result = useCase.getSeriesReviews(seriesId, page)

        assertThat(result).containsExactly(review)
        coVerify(exactly = 1) { seriesRepository.getSeriesReviews(seriesId, page) }
    }

    @Test
    fun `should return reviews when getSeriesReviews is called with only seriesId`() = runTest {
        val seriesId = 1L
        coEvery { seriesRepository.getSeriesReviews(seriesId, 1) } returns listOf(review)

        val result = useCase.getSeriesReviews(seriesId, 1)

        assertThat(result).containsExactly(review)
        coVerify(exactly = 1) { seriesRepository.getSeriesReviews(seriesId, 1) }
    }

    @Test
    fun `should return seasons when getSeriesSeasons is called`() = runTest {
        val seriesId = 1L
        coEvery { seriesRepository.getSeriesSeasons(seriesId) } returns listOf(season)

        val result = useCase.getSeriesSeasons(seriesId)

        assertThat(result).containsExactly(season)
        coVerify(exactly = 1) { seriesRepository.getSeriesSeasons(seriesId) }
    }

    @Test
    fun `should return episodes when getEpisodes is called`() = runTest {
        val seriesId = 1L
        val seasonNumber = 1
        coEvery { seriesRepository.getEpisodes(seriesId, seasonNumber) } returns listOf(episode)

        val result = useCase.getEpisodes(seriesId, seasonNumber)

        assertThat(result).containsExactly(episode)
        coVerify(exactly = 1) { seriesRepository.getEpisodes(seriesId, seasonNumber) }
    }

    @Test
    fun `should return similar series when getSimilarSeries is called`() = runTest {
        val seriesId = 1L
        val page = 1
        coEvery { seriesRepository.getSimilarSeries(seriesId, page) } returns listOf(series2)

        val result = useCase.getSimilarSeries(seriesId, page)

        assertThat(result).containsExactly(series2)
        coVerify(exactly = 1) { seriesRepository.getSimilarSeries(seriesId, page) }
    }

    @Test
    fun `should return similar series when getSimilarSeries is called with only seriesId`() = runTest {
        val seriesId = 1L
        coEvery { seriesRepository.getSimilarSeries(seriesId, 1) } returns listOf(series2)

        val result = useCase.getSimilarSeries(seriesId, 1)

        assertThat(result).containsExactly(series2)
        coVerify(exactly = 1) { seriesRepository.getSimilarSeries(seriesId, 1) }
    }

    @Test
    fun `should return top cast when getSeriesTopCast is called`() = runTest {
        val seriesId = 1L
        val page = 1
        coEvery { artistRepository.getSeriesTopCast(seriesId, page) } returns listOf(actor)

        val result = useCase.getSeriesTopCast(seriesId, page)

        assertThat(result).containsExactly(actor)
        coVerify(exactly = 1) { artistRepository.getSeriesTopCast(seriesId, page) }
    }

    @Test
    fun `should return top cast when getSeriesTopCast is called with only seriesId`() = runTest {
        val seriesId = 1L
        coEvery { artistRepository.getSeriesTopCast(seriesId, 1) } returns listOf(actor)

        val result = useCase.getSeriesTopCast(seriesId, 1)

        assertThat(result).containsExactly(actor)
        coVerify(exactly = 1) { artistRepository.getSeriesTopCast(seriesId, 1) }
    }

    @Test
    fun `should return expectedGenres when getSeriesGenres is called`() = runTest {
        coEvery { seriesRepository.getSeriesGenres() } returns expectedGenres

        val result = useCase.getSeriesGenres()

        assertEquals(expectedGenres, result)
        coVerify { seriesRepository.getSeriesGenres() }
    }

    @Test
    fun `should throw RuntimeException when getSeriesGenres fails`() = runTest {
        val exception = RuntimeException("Failed to load genres")
        coEvery { seriesRepository.getSeriesGenres() } throws exception

        val thrown = assertFailsWith<RuntimeException> {
            useCase.getSeriesGenres()
        }

        assertEquals("Failed to load genres", thrown.message)
        coVerify { seriesRepository.getSeriesGenres() }
    }

    @Test
    fun `should return expectedTopRatingWithGenre when getTopRatingSeries is called with genre`() = runTest {
        val page = 1
        val genreId = 10765L
        coEvery { seriesRepository.getTopRatingSeries(page, genreId) } returns expectedTopRatingWithGenre

        val result = useCase.getTopRatingSeries(page, genreId)

        assertEquals(expectedTopRatingWithGenre, result)
        coVerify { seriesRepository.getTopRatingSeries(page, genreId) }
    }

    @Test
    fun `should return expectedTopRatingWithoutGenre when getTopRatingSeries is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getTopRatingSeries(page, genreId) } returns expectedTopRatingWithoutGenre

        val result = useCase.getTopRatingSeries(page, genreId)

        assertEquals(expectedTopRatingWithoutGenre, result)
        coVerify { seriesRepository.getTopRatingSeries(page, genreId) }
    }

    @Test
    fun `should return expectedTopRatingWithoutGenre when getTopRatingSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getTopRatingSeries(page, null) } returns expectedTopRatingWithoutGenre

        val result = useCase.getTopRatingSeries(page)

        assertEquals(expectedTopRatingWithoutGenre, result)
        coVerify { seriesRepository.getTopRatingSeries(page, null) }
    }

    @Test
    fun `should return expectedTrendingWithGenre when getTrendingSeries is called with genre`() = runTest {
        val page = 1
        val genreId = 10759L
        coEvery { seriesRepository.getTrendingSeries(page, genreId) } returns expectedTrendingWithGenre

        val result = useCase.getTrendingSeries(page, genreId)

        assertEquals(expectedTrendingWithGenre, result)
        coVerify { seriesRepository.getTrendingSeries(page, genreId) }
    }

    @Test
    fun `should return expectedTrendingWithoutGenre when getTrendingSeries is called with null genre`() = runTest {
        val page = 2
        val genreId: Long? = null
        coEvery { seriesRepository.getTrendingSeries(page, genreId) } returns expectedTrendingWithoutGenre

        val result = useCase.getTrendingSeries(page, genreId)

        assertEquals(expectedTrendingWithoutGenre, result)
        coVerify { seriesRepository.getTrendingSeries(page, genreId) }
    }

    @Test
    fun `should return expectedTrendingWithoutGenre when getTrendingSeries is called with only page`() = runTest {
        val page = 2
        coEvery { seriesRepository.getTrendingSeries(page, null) } returns expectedTrendingWithoutGenre

        val result = useCase.getTrendingSeries(page)

        assertEquals(expectedTrendingWithoutGenre, result)
        coVerify { seriesRepository.getTrendingSeries(page, null) }
    }

    private companion object {
        val expectedSeriesSciFi = listOf(
            Series(
                id = 700L,
                title = "Future Chronicles",
                rating = 9.0f,
                posterPath = "/sorted1.jpg",
                trailerPath = "https://youtube.com/futurechronicles",
                genres = listOf(Genre(10765, "Sci-Fi & Fantasy")),
                overview = "Top-rated science fiction series.",
                releaseDate = 1721347200000L,
                seasonsCount = 4
            )
        )

        val expectedSeriesSorted = listOf(
            Series(
                id = 700L,
                title = "Future Chronicles",
                rating = 9.0f,
                posterPath = "/sorted1.jpg",
                trailerPath = "https://youtube.com/futurechronicles",
                genres = listOf(Genre(10765, "Sci-Fi & Fantasy")),
                overview = "Top-rated science fiction series.",
                releaseDate = 1721347200000L,
                seasonsCount = 4
            )
        )

        val expectedSeriesUnfiltered = listOf(
            Series(
                id = 701L,
                title = "General Series",
                rating = 7.2f,
                posterPath = "/unfiltered1.jpg",
                trailerPath = "https://youtube.com/generalseries",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A light-hearted show for everyone.",
                releaseDate = 1721433600000L,
                seasonsCount = 2
            )
        )

        val expectedSeriesWithCategory = listOf(
            Series(
                id = 600L,
                title = "Adventures Unfold",
                rating = 8.1f,
                posterPath = "/series1.jpg",
                trailerPath = "https://youtube.com/adventuresunfold",
                genres = listOf(Genre(10759, "Action & Adventure")),
                overview = "Today's explosive episode reveals everything.",
                releaseDate = 1721174400000L,
                seasonsCount = 3
            )
        )

        val expectedSeriesWithoutCategory = listOf(
            Series(
                id = 601L,
                title = "Slice of Life",
                rating = 7.4f,
                posterPath = "/series2.jpg",
                trailerPath = "https://youtube.com/sliceoflife",
                genres = listOf(Genre(18, "Drama")),
                overview = "Ordinary stories, extraordinary feelings.",
                releaseDate = 1721260800000L,
                seasonsCount = 2
            )
        )

        val expectedWithCategory = listOf(
            Series(
                id = 800L,
                title = "Animated Dreams",
                rating = 7.8f,
                posterPath = "/free1.jpg",
                trailerPath = "https://youtube.com/animateddreams",
                genres = listOf(Genre(16, "Animation")),
                overview = "A fun animated series for all ages.",
                releaseDate = 1721520000000L,
                seasonsCount = 5
            )
        )

        val expectedWithoutCategory = listOf(
            Series(
                id = 801L,
                title = "Unscripted Journey",
                rating = 8.0f,
                posterPath = "/free2.jpg",
                trailerPath = "https://youtube.com/unscriptedjourney",
                genres = listOf(Genre(99, "Documentary")),
                overview = "A powerful documentary series available freely.",
                releaseDate = 1721606400000L,
                seasonsCount = 1
            )
        )

        val expectedRecommendedWithCategory = listOf(
            Series(
                id = 900L,
                title = "Mystery Echoes",
                rating = 8.5f,
                posterPath = "/recommended1.jpg",
                trailerPath = "https://youtube.com/mysteryechoes",
                genres = listOf(Genre(9648, "Mystery")),
                overview = "Unravel the secrets one clue at a time.",
                releaseDate = 1721692800000L,
                seasonsCount = 3
            )
        )

        val expectedRecommendedWithoutCategory = listOf(
            Series(
                id = 901L,
                title = "Global Picks",
                rating = 7.9f,
                posterPath = "/recommended2.jpg",
                trailerPath = "https://youtube.com/globalpicks",
                genres = listOf(Genre(10751, "Family")),
                overview = "Loved by millions across the world.",
                releaseDate = 1721779200000L,
                seasonsCount = 4
            )
        )

        val expectedWithGenre = listOf(
            Series(
                id = 1000L,
                title = "Daily Drama",
                rating = 7.6f,
                posterPath = "/ontv1.jpg",
                trailerPath = "https://youtube.com/dailydrama",
                genres = listOf(Genre(10766, "Soap")),
                overview = "A gripping soap opera that airs every evening.",
                releaseDate = 1721865600000L,
                seasonsCount = 6
            )
        )

        val expectedWithoutGenre = listOf(
            Series(
                id = 1001L,
                title = "Prime Time Show",
                rating = 8.3f,
                posterPath = "/ontv2.jpg",
                trailerPath = "https://youtube.com/primetimeshow",
                genres = listOf(Genre(35, "Comedy")),
                overview = "The top comedy series airing this season.",
                releaseDate = 1721952000000L,
                seasonsCount = 3
            )
        )

        val expectedPopularWithCategory = listOf(
            Series(
                id = 1100L,
                title = "City Crime Files",
                rating = 8.7f,
                posterPath = "/popular1.jpg",
                trailerPath = "https://youtube.com/citycrimefiles",
                genres = listOf(Genre(80, "Crime")),
                overview = "Crime never sleeps in the big city.",
                releaseDate = 1722038400000L,
                seasonsCount = 5
            )
        )

        val expectedPopularWithoutCategory = listOf(
            Series(
                id = 1101L,
                title = "The People’s Favorite",
                rating = 8.0f,
                posterPath = "/popular2.jpg",
                trailerPath = "https://youtube.com/thepeoplesfavorite",
                genres = listOf(Genre(35, "Comedy")),
                overview = "The show that everyone is talking about.",
                releaseDate = 1722124800000L,
                seasonsCount = 4
            )
        )

        val expectedSeries = listOf(
            Series(
                id = 1200L,
                title = "Global Conflicts",
                rating = 8.6f,
                posterPath = "/seriesbycat1.jpg",
                trailerPath = "https://youtube.com/globalconflicts",
                genres = listOf(Genre(10768, "War & Politics")),
                overview = "A deep dive into international tensions and politics.",
                releaseDate = 1722211200000L,
                seasonsCount = 2
            )
        )

        val expectedTopRatingWithGenre = listOf(
            Series(
                id = 1300L,
                title = "Star Voyages",
                rating = 9.2f,
                posterPath = "/toprating1.jpg",
                trailerPath = "https://youtube.com/starvoyages",
                genres = listOf(Genre(10765, "Sci-Fi & Fantasy")),
                overview = "A critically acclaimed sci-fi epic.",
                releaseDate = 1722297600000L,
                seasonsCount = 3
            )
        )

        val expectedTopRatingWithoutGenre = listOf(
            Series(
                id = 1301L,
                title = "Timeless Classics",
                rating = 8.8f,
                posterPath = "/toprating2.jpg",
                trailerPath = "https://youtube.com/timelessclassics",
                genres = listOf(Genre(18, "Drama")),
                overview = "A beloved drama series.",
                releaseDate = 1722384000000L,
                seasonsCount = 5
            )
        )

        val expectedTrendingWithGenre = listOf(
            Series(
                id = 1400L,
                title = "Battlefront",
                rating = 8.4f,
                posterPath = "/trending1.jpg",
                trailerPath = "https://youtube.com/battlefront",
                genres = listOf(Genre(10759, "Action & Adventure")),
                overview = "A trending action series.",
                releaseDate = 1722470400000L,
                seasonsCount = 2
            )
        )

        val expectedTrendingWithoutGenre = listOf(
            Series(
                id = 1401L,
                title = "Viral Stories",
                rating = 7.9f,
                posterPath = "/trending2.jpg",
                trailerPath = "https://youtube.com/viralstories",
                genres = listOf(Genre(35, "Comedy")),
                overview = "A trending comedy series.",
                releaseDate = 1722556800000L,
                seasonsCount = 1
            )
        )

        val expectedGenres = listOf(
            Genre(id = 18, name = "Drama"),
            Genre(id = 10759, name = "Action & Adventure"),
            Genre(id = 35, name = "Comedy"),
            Genre(id = 10765, name = "Sci-Fi & Fantasy"),
            Genre(id = 9648, name = "Mystery")
        )

        private val series1 = Series(
            id = 1,
            title = "Series One",
            rating = 8f,
            posterPath = "/poster.png",
            trailerPath = "trailer",
            genres = listOf(Genre(id = 1L, "genre1"), Genre(id = 2L, "genre2")),
            overview = "overview",
            releaseDate = 321L,
            seasonsCount = 3
        )

        private val series2 = Series(
            id = 2,
            title = "Series Two",
            rating = 7.6f,
            posterPath = "/poster.png",
            trailerPath = "trailer",
            genres = listOf(Genre(id = 1L, "genre1"), Genre(id = 2L, "genre2")),
            overview = "overview",
            releaseDate = 321L,
            seasonsCount = 3
        )

        private val season = Season(
            seasonNumber = 1,
            seasonName = "season",
            seriesId = 1,
            episodesCount = 25,
            rating = 6.5f,
            posterPath = "/poster.png",
            overview = "overVIew",
            airDate = 321L
        )

        private val episode = Episode(
            id = 123,
            episodeNumber = 2,
            photoPath = "/photo.png",
            episodeName = "episode name",
            runtimeMinutes = 45,
            rating = 9f,
            seasonNumber = 2,
            seriesId = 123
        )

        private val actor = Artist(
            id = 312,
            name = "Keanu",
            photoPath = "/keanu.jpeg",
            country = "Japan",
            birthDate = 1234L,
            biography = "bio graphy",
            department = "actor"
        )

        private val review = Review(
            id = "123",
            author = "Ana",
            authorPhotoPath = "/poster.png",
            rating = 4.0f,
            date = 123,
            description = ""
        )
    }
}