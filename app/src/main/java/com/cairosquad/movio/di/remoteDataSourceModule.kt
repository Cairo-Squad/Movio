package com.cairosquad.movio.di

import com.cairosquad.remote.common.HttpClientFactory
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single {
        HttpClientFactory.create(
            OkHttp.create {
                preconfigured = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    )
                    .build()
            }
        )
    }
}