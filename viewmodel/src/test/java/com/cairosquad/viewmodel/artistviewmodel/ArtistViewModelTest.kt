package com.cairosquad.viewmodel.artistviewmodel

import com.cairosquad.domain.exception.InternetConnectionException
import com.cairosquad.domain.exception.NetworkException
import com.cairosquad.domain.exception.UnknownException
import com.cairosquad.domain.usecase.artists.GetArtistDetailsUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import com.cairosquad.viewmodel.details.artist.ArtistScreenState
import com.cairosquad.viewmodel.details.artist.ArtistViewModel
import com.cairosquad.viewmodel.details.artist.ArtistEffect
import com.cairosquad.viewmodel.details.artist.toArtistUiState
import com.cairosquad.viewmodel.details.artist.toArtistMovieUiState
import com.cairosquad.viewmodel.details.artist.toArtistSeriesUiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getArtistDetailsUseCase: GetArtistDetailsUseCase
    private lateinit var viewModel: ArtistViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        getArtistDetailsUseCase = mockk(relaxed = true)

        viewModel = ArtistViewModel(
            getArtistDetailsUseCase = getArtistDetailsUseCase
        )
    }

    @Test
    fun `should load artist details when loadArtistDetails is called`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getArtist(artistId) } returns artist

        viewModel.loadArtistDetails(artistId)

        delay(400)

        assertThat(viewModel.screenState.value.artist).isEqualTo(artist.toArtistUiState())
    }

    @Test
    fun `should set loading status when loadArtistDetails is called`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getArtist(artistId) } returns artist

        viewModel.loadArtistDetails(artistId)

        delay(100)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.LOADING)
    }

    @Test
    fun `should set error status when loadArtistDetails fails`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getArtist(artistId) } throws IOException()

        viewModel.loadArtistDetails(artistId)

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isIn(
            listOf(
                ErrorStatus.NO_INTERNET,
                ErrorStatus.NETWORK_ERROR,
                ErrorStatus.UNKNOWN_ERROR
            )
        )
    }

    @Test
    fun `should load artist movies when loadArtistMovies is called`() = runBlocking {
        val artistId = 1L
        val movies = listOf(movie1, movie2)
        coEvery { getArtistDetailsUseCase.getMoviesOfArtist(artistId) } returns movies

        viewModel.loadArtistMovies(artistId)

        delay(400)

        assertThat(viewModel.screenState.value.knownForMovies).isEqualTo(movies.map { it.toArtistMovieUiState() })
    }

    @Test
    fun `should set error status when loadArtistMovies fails`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getMoviesOfArtist(artistId) } throws IOException()

        viewModel.loadArtistMovies(artistId)

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isIn(
            listOf(
                ErrorStatus.NO_INTERNET,
                ErrorStatus.NETWORK_ERROR,
                ErrorStatus.UNKNOWN_ERROR
            )
        )
    }

    @Test
    fun `should load artist series when loadArtistSeries is called`() = runBlocking {
        val artistId = 1L
        val series = listOf(series1)
        coEvery { getArtistDetailsUseCase.getSeriesOfArtist(artistId) } returns series

        viewModel.loadArtistSeries(artistId)

        delay(400)

        assertThat(viewModel.screenState.value.KnownForSeries).isEqualTo(series.map { it.toArtistSeriesUiState() })
    }

    @Test
    fun `should set error status when loadArtistSeries fails`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getSeriesOfArtist(artistId) } throws IOException()

        viewModel.loadArtistSeries(artistId)

        delay(400)

        assertThat(viewModel.screenState.value.screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
        assertThat(viewModel.screenState.value.errorStatus).isIn(
            listOf(
                ErrorStatus.NO_INTERNET,
                ErrorStatus.NETWORK_ERROR,
                ErrorStatus.UNKNOWN_ERROR
            )
        )
    }


    @Test
    fun `should set NETWORK_ERROR when loadArtistDetails fails with NetworkException`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getArtist(artistId) } throws NetworkException()

        viewModel.loadArtistDetails(artistId)

        delay(400)

        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        }
    }

    @Test
    fun `should set UNKNOWN_ERROR when loadArtistDetails fails with UnknownException`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getArtist(artistId) } throws UnknownException()

        viewModel.loadArtistDetails(artistId)

        delay(400)

        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
        }
    }

    @Test
    fun `should set NO_INTERNET when loadArtistDetails fails with InternetConnectionException`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getArtist(artistId) } throws InternetConnectionException()

        viewModel.loadArtistDetails(artistId)

        delay(400)

        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.NO_INTERNET)
        }
    }

    @Test
    fun `should map NetworkException to NETWORK_ERROR when handling artist exception`() {
        val networkEx = NetworkException()
        val status = viewModel.run {
            javaClass.getDeclaredMethod("handleArtistException", Throwable::class.java)
                .apply { isAccessible = true }.invoke(this, networkEx)
        } as ErrorStatus
        assertThat(status).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `should map generic exception to UNKNOWN_ERROR when handling artist exception`() {
        val genericEx = IllegalStateException("error")
        val status = viewModel.run {
            javaClass.getDeclaredMethod("handleArtistException", Throwable::class.java)
                .apply { isAccessible = true }.invoke(this, genericEx)
        } as ErrorStatus
        assertThat(status).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should map NetworkException to NETWORK_ERROR when converting exception`() {
        val status = exceptionToErrorStatus(NetworkException())
        assertThat(status).isEqualTo(ErrorStatus.NETWORK_ERROR)
    }

    @Test
    fun `should map UnknownException to UNKNOWN_ERROR when converting exception`() {
        val status = exceptionToErrorStatus(UnknownException())
        assertThat(status).isEqualTo(ErrorStatus.UNKNOWN_ERROR)
    }

    @Test
    fun `should map InternetConnectionException to NO_INTERNET when converting exception`() {
        val status = exceptionToErrorStatus(InternetConnectionException())
        assertThat(status).isEqualTo(ErrorStatus.NO_INTERNET)
    }

    @Test
    fun `should set NETWORK_ERROR when loadArtistMovies fails with NetworkException`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getMoviesOfArtist(artistId) } throws NetworkException()

        viewModel.loadArtistMovies(artistId)

        delay(400)

        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        }
    }

    @Test
    fun `should set NETWORK_ERROR when loadArtistSeries fails with NetworkException`() = runBlocking {
        val artistId = 1L
        coEvery { getArtistDetailsUseCase.getSeriesOfArtist(artistId) } throws NetworkException()

        viewModel.loadArtistSeries(artistId)

        delay(400)

        with(viewModel.screenState.value) {
            assertThat(screenStatus).isEqualTo(ArtistScreenState.ScreenStatus.FAILED)
            assertThat(errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        }
    }

    private companion object {
        val artist = Artist(
            id = 1,
            name = "Tom Hanks",
            photoPath = "/img.jpg"
        )

        val movie1 = Movie(
            id = 1,
            title = "Forrest Gump",
            rating = 4.5f,
            posterPath = "/img.jpg"
        )

        val movie2 = Movie(
            id = 2,
            title = "Cast Away",
            rating = 4.0f,
            posterPath = "/img.jpg"
        )

        val series1 = Series(
            id = 1,
            title = "Band of Brothers",
            rating = 4.8f,
            posterPath = "/img.jpg"
        )
    }
}