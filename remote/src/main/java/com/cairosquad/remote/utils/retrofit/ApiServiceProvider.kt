package com.cairosquad.remote.utils.retrofit

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.artists.ArtistsApiService
import com.cairosquad.remote.login.LoginApiService
import com.cairosquad.remote.movie.MovieApiService
import com.cairosquad.remote.search.SearchApiService
import com.cairosquad.remote.series.SeriesApiService
import com.cairosquad.remote.utils.retrofit.interceptor.ApiKeyInterceptor
import com.cairosquad.remote.utils.retrofit.interceptor.AuthInterceptor
import com.cairosquad.remote.utils.retrofit.interceptor.LanguageInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ApiServiceProvider {
    private var retrofit: Retrofit = retrofitProvider("")

    private var loginApiService: LoginApiService = retrofit.create(LoginApiService::class.java)
    private var seriesApiService: SeriesApiService = retrofit.create(SeriesApiService::class.java)
    private var searchApiService: SearchApiService = retrofit.create(SearchApiService::class.java)
    private var artistsApiService: ArtistsApiService = retrofit.create(ArtistsApiService::class.java)
    private var movieApiService: MovieApiService = retrofit.create(MovieApiService::class.java)

    fun updateToken(token: String) {
        retrofit = retrofitProvider(token)
        loginApiService = retrofit.create(LoginApiService::class.java)
        seriesApiService = retrofit.create(SeriesApiService::class.java)
        searchApiService = retrofit.create(SearchApiService::class.java)
        artistsApiService = retrofit.create(ArtistsApiService::class.java)
        movieApiService = retrofit.create(MovieApiService::class.java)
    }

    fun getLoginApiService() = loginApiService
    fun getSeriesApiService() = seriesApiService
    fun getSearchApiService() = searchApiService
    fun getArtistsApiService() = artistsApiService
    fun getMovieApiService() = movieApiService
}

private fun retrofitProvider(token: String): Retrofit {
    val contentType = "application/json".toMediaType()

    val client = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor(BuildConfig.API_KEY))
        .addInterceptor(AuthInterceptor(token))
        .addInterceptor(LanguageInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
}