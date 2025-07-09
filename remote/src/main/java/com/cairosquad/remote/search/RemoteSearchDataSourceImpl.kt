package com.cairosquad.remote.search

import com.cairosquad.repository.search.dataSource.remote.search.RemoteSearchDataSource
import io.ktor.client.HttpClient

class RemoteSearchDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteSearchDataSource {


}