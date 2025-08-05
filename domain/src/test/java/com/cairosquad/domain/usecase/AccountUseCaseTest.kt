package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.AccountRepository
import com.cairosquad.entity.Account
import com.cairosquad.entity.MediaList
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
class AccountUseCaseTest {

    private val accountRepository: AccountRepository = mockk()
    private lateinit var useCase: AccountUseCase

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = AccountUseCase(accountRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAccountDetails returns account from repository`() = runTest {
        val expectedAccount = Account(
            id = 1, name = "Youssef",
            username = "pixelise",
            avatarPath = "/image.png"
        )

        coEvery { accountRepository.getAccountDetails() } returns expectedAccount

        val result = useCase.getAccountDetails()

        assertThat(result).isEqualTo(expectedAccount)
        coVerify { accountRepository.getAccountDetails() }
    }

    @Test
    fun `getSeriesLists returns series list from repository`() = runTest {
        val expectedLists = listOf(
            MediaList(id = 123, "seriesList1", mediaCount = 1),
            MediaList(id = 321, name = "seriesList2", mediaCount = 2)
        )

        coEvery { accountRepository.getSeriesLists(1) } returns expectedLists

        val result = useCase.getSeriesLists()

        assertThat(result).isEqualTo(expectedLists)
        coVerify { accountRepository.getSeriesLists(1) }
    }

    @Test
    fun `getMoviesLists returns movie list from repository`() = runTest {
        val expectedLists = listOf(
            MediaList(id = 123, "seriesList1", mediaCount = 1),
            MediaList(id = 321, name = "seriesList2", mediaCount = 2)
        )

        coEvery { accountRepository.getMovieLists(1) } returns expectedLists

        val result = useCase.getMoviesLists()

        assertThat(result).isEqualTo(expectedLists)
        coVerify { accountRepository.getMovieLists(1) }
    }
}
