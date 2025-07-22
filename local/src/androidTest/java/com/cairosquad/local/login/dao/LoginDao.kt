package com.cairosquad.local.login.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.cairosquad.local.utils.MovioDataBase
import com.cairosquad.repository.login.data_source.local.dto.SessionIdDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: MovioDataBase
    private lateinit var dao: LoginDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovioDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = db.loginDao()
    }

    @After
    fun close() {
        db.close()
    }

    @Test
    fun insert_and_getSessionId() = runTest {
        val dto = SessionIdDto(sessionId = "abc123")
        dao.saveSessionId(dto)

        val loaded = dao.getSessionId()
        assertThat(loaded.first().sessionId).isEqualTo("abc123")
    }

    @Test
    fun insert_overwritesExisting_onConflictReplace() = runTest {
        dao.saveSessionId(SessionIdDto(sessionId = "first"))
        dao.saveSessionId(SessionIdDto(sessionId = "second"))

        val loaded = dao.getSessionId()
        assertThat(loaded.first().sessionId).isEqualTo("second")
    }

    @Test
    fun default_sessionId_is_empty_list() = runTest {
        val loaded = dao.getSessionId()
        assertThat(loaded).isEmpty()
    }

    @Test
    fun insert_and_remove_sessionId() = runTest {
        dao.saveSessionId(SessionIdDto(sessionId = "first"))
        dao.saveSessionId(SessionIdDto(sessionId = ""))

        val loaded = dao.getSessionId()
        assertThat(loaded.first().sessionId).isEqualTo("")
    }
}
