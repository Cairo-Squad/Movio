package com.cairosquad.viewmodel.base


//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    data class TestState(val value: Int = 0, val error: Int = 0)

    sealed class TestEvent {
        object TestEvent1 : TestEvent()
        object TestEvent2 : TestEvent()
    }

    private class TestViewModel(initialState: TestState) : BaseViewModel<TestState, TestEvent>(initialState) {
        fun updateStateValue(transform: (TestState) -> TestState) {
            updateState(transform)
        }

        fun sendTestEvent(
            event: TestEvent,
            onStart: suspend () -> Unit = {},
            onEnd: suspend () -> Unit = {}
        ) {
            sendEvent(
                event,
                onStart,
                onEnd
            )
        }

        fun testTryToCall(
            block: suspend () -> Int,
            onSuccess: (Int) -> Unit,
            onError: (Throwable) -> Unit,
            onStart: suspend () -> Unit = {},
            onEnd: suspend () -> Unit = {}
        ) {
            tryToCall(
                block = block,
                onSuccess = onSuccess,
                onError = onError,
                onStart = onStart,
                onEnd = onEnd
            )
        }
    }

    private lateinit var viewModel: TestViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TestViewModel(TestState())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateStateShouldUpdateUiStateCorrectly() = testScope.runTest {
        val newStateValue = 42

        viewModel.updateStateValue({ it.copy(value = newStateValue) })

        val state = viewModel.uiState.first()
        assertEquals(newStateValue, state.value)
        assertEquals(0, state.error)
    }

    @Test
    fun sendEventShouldEmitEventCorrectly() = testScope.runTest {
        val expectedEvent = TestEvent.TestEvent1
        var receivedEvent: TestEvent? = null
        val job = launch { viewModel.uiEvent.collect { receivedEvent = it } }

        viewModel.sendTestEvent(expectedEvent)

        assertEquals(expectedEvent, receivedEvent)
        job.cancel()
    }

    @Test
    fun tryToCallShouldUpdateStateOnSuccess() = testScope.runTest {
        val newStateValue = 42

        viewModel.testTryToCall(
            block = { newStateValue },
            onSuccess = { result -> viewModel.updateStateValue({ it.copy(value = newStateValue) }) },
            onError = { viewModel.updateStateValue({ it.copy(error = newStateValue) }) }
        )

        val state = viewModel.uiState.first()
        assertEquals(newStateValue, state.value)
        assertEquals(0, state.error)
    }

    @Test
    fun tryToCallShouldUpdateStateOnFailure() = testScope.runTest {
        val newStateValue = 42

        viewModel.testTryToCall(
            block = { throw Exception("test") },
            onSuccess = { result -> viewModel.updateStateValue({ it.copy(value = newStateValue) }) },
            onError = { viewModel.updateStateValue({ it.copy(error = newStateValue) }) },
        )

        val state = viewModel.uiState.first()
        assertEquals(0, state.value)
        assertEquals(newStateValue, state.error)
    }

    @Test
    fun sendEventShouldCallOnStartAndOnEndCallbacks() = testScope.runTest {
        var onStartCalled = false
        var onEndCalled = false
        val event = TestEvent.TestEvent2

        viewModel.sendTestEvent(
            event = event,
            onStart = { onStartCalled = true },
            onEnd = { onEndCalled = true }
        )

        assertTrue(onStartCalled)
        assertTrue(onEndCalled)
    }

    @Test
    fun tryToCallShouldCallOnStartAndOnEndCallbacksWithOnSuccess() = testScope.runTest {
        var onStartCalled = false
        var onEndCalled = false

        val newStateValue = 42

        viewModel.testTryToCall(
            block = { newStateValue },
            onSuccess = { result -> viewModel.updateStateValue({ it.copy(value = newStateValue) }) },
            onError = { viewModel.updateStateValue({ it.copy(error = newStateValue) }) },
            onStart = { onStartCalled = true },
            onEnd = { onEndCalled = true }
        )

        assertTrue(onStartCalled)
        assertTrue(onEndCalled)
    }

    @Test
    fun tryToCallShouldCallOnStartAndOnEndCallbacksWithOnError() = testScope.runTest {
        var onStartCalled = false
        var onEndCalled = false

        val newStateValue = 42

        viewModel.testTryToCall(
            block = { throw Exception("test") },
            onSuccess = { result -> viewModel.updateStateValue({ it.copy(value = newStateValue) }) },
            onError = { viewModel.updateStateValue({ it.copy(error = newStateValue) }) },
            onStart = { onStartCalled = true },
            onEnd = { onEndCalled = true }
        )

        assertTrue(onStartCalled)
        assertTrue(onEndCalled)
    }
}