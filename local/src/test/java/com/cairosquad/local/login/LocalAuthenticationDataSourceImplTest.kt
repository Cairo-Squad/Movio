package com.cairosquad.local.login

import com.cairosquad.local.login.dao.LoginDao
import com.cairosquad.repository.login.data_source.local.LocalAuthenticationDataSource
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class LocalAuthenticationDataSourceImplTest {

    private val loginDao: LoginDao = mockk()
    private lateinit var authDataSource: LocalAuthenticationDataSource

    private val sampleSession = "abc123"
    private val sampleDto = SessionIdDto(sessionId = sampleSession)

    @Before
    fun setup() {
        authDataSource = LocalAuthenticationDataSourceImpl(loginDao)
    }

    @Test
    fun `saveSessionId SHOULD call dao with correct SessionIdDto`() = runTest {
        coEvery { loginDao.saveSessionId(any()) } returns Unit

        authDataSource.saveSessionId(sampleSession)

        coVerify(exactly = 1) {
            loginDao.saveSessionId(
                match { dto -> dto.sessionId == sampleSession }
            )
        }
    }

    @Test
    fun `getSessionId SHOULD call dao and return plain sessionId`() = runTest {
        coEvery { loginDao.getSessionId() } returns listOf(sampleDto)

        val result = authDataSource.getSessionId()

        assertEquals(sampleSession, result)
        coVerify(exactly = 1) { loginDao.getSessionId() }
    }

    @Test
    fun `saveSessionId SHOULD propagate exceptions from dao`() = runTest {
        val ex = IllegalStateException("db write error")
        coEvery { loginDao.saveSessionId(any()) } throws ex

        val thrown = assertFailsWith<IllegalStateException> {
            authDataSource.saveSessionId(sampleSession)
        }
        assertEquals("db write error", thrown.message)
    }

    @Test
    fun `getSessionId SHOULD propagate exceptions from dao`() = runTest {
        val ex = RuntimeException("db read error")
        coEvery { loginDao.getSessionId() } throws ex

        val thrown = assertFailsWith<RuntimeException> {
            authDataSource.getSessionId()
        }
        assertEquals("db read error", thrown.message)
    }

    @Test
    fun `getSessionId SHOULD return empty string when dao returns empty list`() = runTest {
        coEvery { loginDao.getSessionId() } returns emptyList()

        val result = authDataSource.getSessionId()

        assertEquals("", result)
    }
}
