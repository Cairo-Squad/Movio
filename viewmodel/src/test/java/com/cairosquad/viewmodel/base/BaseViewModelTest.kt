package com.cairosquad.viewmodel.base

import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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

    private class TestViewModel(
        initialState: TestState,
        private val dispatcher: CoroutineDispatcher
    ) : BaseViewModel<TestState, TestEvent>(initialState) {

        fun updateStateValue(transform: (TestState) -> TestState) {
            updateState(transform)
        }

        fun sendTestEvent(
            event: TestEvent,
            onStart: suspend () -> Unit = {},
            onEnd: suspend () -> Unit = {},
        ) {
            sendEffect(event, onStart, onEnd)
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
                onEnd = onEnd,
                dispatcher = dispatcher
            )
        }
    }

    private lateinit var viewModel: TestViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = TestViewModel(TestState(), testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update uiState when updateStateValue is called`() = runTest(testDispatcher) {
        // Given
        val newStateValue = 42

        // When
        viewModel.updateStateValue { it.copy(value = newStateValue) }

        // Then
        val state = viewModel.screenState.first()
        assertEquals(newStateValue, state.value)
        assertEquals(0, state.error)
    }

    @Test
    fun `should emit uiEvent when sendEvent is called`() = runTest(testDispatcher) {
        // Given
        val expectedEvent = TestEvent.TestEvent1
        var receivedEvent: TestEvent? = null
        val job = launch { viewModel.effect.collect { receivedEvent = it } }

        // When
        viewModel.sendTestEvent(expectedEvent)

        // Then
        assertEquals(expectedEvent, receivedEvent)
        job.cancel()
    }

    @Test
    fun `should update state with success value when tryToCall succeeds`() =
        runTest(testDispatcher) {
            // Given
            val newStateValue = 42

            // When
            viewModel.testTryToCall(
                block = { newStateValue },
                onSuccess = { result -> viewModel.updateStateValue { it.copy(value = newStateValue) } },
                onError = { viewModel.updateStateValue { it.copy(error = newStateValue) } }
            )

            // Then
            val state = viewModel.screenState.first()
            assertEquals(newStateValue, state.value)
            assertEquals(0, state.error)
        }

    @Test
    fun `should update state with error value when tryToCall throws`() = runTest(testDispatcher) {
        // Given
        val newStateValue = 42

        // When
        viewModel.testTryToCall(
            block = { throw Exception("test") },
            onSuccess = { viewModel.updateStateValue { it.copy(value = newStateValue) } },
            onError = { viewModel.updateStateValue { it.copy(error = newStateValue) } },
        )

        // Then
        advanceUntilIdle()
        val state = viewModel.screenState.value
        assertEquals(0, state.value)
        assertEquals(newStateValue, state.error)
    }

    @Test
    fun `should call onStart and onEnd when sendEvent is executed`() = runTest(testDispatcher) {
        // Given
        var onStartCalled = false
        var onEndCalled = false
        val event = TestEvent.TestEvent2

        // When
        viewModel.sendTestEvent(
            event = event,
            onStart = { onStartCalled = true },
            onEnd = { onEndCalled = true }
        )

        // Then
        assertTrue(onStartCalled)
        assertTrue(onEndCalled)
    }

    @Test
    fun `should call onStart and onEnd when tryToCall succeeds`() = runTest(testDispatcher) {
        var onStartCalled = false
        var onEndCalled = false

        val newStateValue = 42

        viewModel.testTryToCall(
            block = { newStateValue },
            onSuccess = { result -> viewModel.updateStateValue { it.copy(value = newStateValue) } },
            onError = { viewModel.updateStateValue { it.copy(error = newStateValue) } },
            onStart = { onStartCalled = true },
            onEnd = { onEndCalled = true }
        )
        advanceUntilIdle()
        assertTrue(onStartCalled)
        assertTrue(onEndCalled)
    }

    @Test
    fun `should call onStart and onEnd when tryToCall fails`() = runTest(testDispatcher) {
        // Given
        var onStartCalled = false
        var onEndCalled = false

        // When
        viewModel.testTryToCall(
            block = { throw Exception("test") },
            onSuccess = { },
            onError = { },
            onStart = { onStartCalled = true },
            onEnd = { onEndCalled = true }

        )
        // Then
        advanceUntilIdle()
        assertTrue(onStartCalled)
        assertTrue(onEndCalled)
    }
}