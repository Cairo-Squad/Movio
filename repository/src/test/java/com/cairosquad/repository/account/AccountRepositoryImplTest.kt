package com.cairosquad.repository.account

import com.cairosquad.repository.account.data_source.local.AccountLocalDataSource
import com.cairosquad.repository.account.data_source.remote.AccountRemoteDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class AccountRepositoryImplTest {

    private val remoteDataSource: AccountRemoteDataSource = mockk()
    private val localDataSource: AccountLocalDataSource = mockk()
    private lateinit var repository: AccountRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = AccountRepositoryImpl(remoteDataSource, localDataSource)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovieLists returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getMovieLists(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getMovieLists(any(), any()) }
    }

    @Test
    fun `getSeriesLists returns empty list if account id is null`() = runTest {
        coEvery { localDataSource.getAccountId() } returns null

        val result = repository.getSeriesLists(1)

        assertThat(result).isEmpty()
        coVerify(exactly = 0) { remoteDataSource.getSeriesLists(any(), any()) }
    }
}