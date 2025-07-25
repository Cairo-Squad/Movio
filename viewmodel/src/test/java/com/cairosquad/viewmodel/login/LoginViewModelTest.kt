package com.cairosquad.viewmodel.login

import app.cash.turbine.test
import com.cairosquad.domain.usecase.authentication.LoginUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = StandardTestDispatcher()
    private val loginUseCase: LoginUseCase = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        viewModel = LoginViewModel(loginUseCase)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN username changed SHOULD update state`() = runTest {
        viewModel.onUsernameChange("newUser")
        assertThat(viewModel.screenState.value.username).isEqualTo("newUser")
    }

    @Test
    fun `WHEN username changed SHOULD reset validation`() = runTest {
        viewModel.updateState { it.copy(errors = mapOf(LoginScreenState.FormField.USERNAME to LoginScreenState.ValidationError.EMPTY_FIELD)) }
        viewModel.onUsernameChange("newUser")
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.USERNAME]).isNull()
    }

    @Test
    fun `WHEN password changed SHOULD update state`() = runTest {
        viewModel.onPasswordChange("secret")
        assertThat(viewModel.screenState.value.password).isEqualTo("secret")
    }

    @Test
    fun `WHEN password changed SHOULD reset validation`() = runTest {
        viewModel.updateState { it.copy(errors = mapOf(LoginScreenState.FormField.PASSWORD to LoginScreenState.ValidationError.EMPTY_FIELD)) }
        viewModel.onPasswordChange("secret")
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.PASSWORD]).isNull()
    }

    @Test
    fun `WHEN password visibility icon clicked SHOULD toggle visibility`() = runTest {
        val initial = viewModel.screenState.value.isPasswordVisible
        viewModel.onPasswordVisibilityIconClick()
        assertThat(viewModel.screenState.value.isPasswordVisible).isEqualTo(!initial)
    }

    @Test
    fun `WHEN forget password clicked SHOULD send NavigateToForgetPassword effect`() = runTest {
        viewModel.effect.test {
            viewModel.onForgetPasswordClick()
            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateToForgetPassword)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN continue as guest clicked SHOULD send NavigateToGuestHome effect`() = runTest {
        viewModel.effect.test {
            viewModel.onContinueAsAGuestClick()
            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateToGuestHome)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN sign up clicked SHOULD send NavigateToSignUp effect`() = runTest {
        viewModel.effect.test {
            viewModel.onSignUpClick()
            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateToSignUp)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN login clicked SHOULD validate fields`() = runTest {
        viewModel.onLoginClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.USERNAME]).isEqualTo(
            LoginScreenState.ValidationError.EMPTY_FIELD
        )
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.PASSWORD]).isEqualTo(
            LoginScreenState.ValidationError.EMPTY_FIELD
        )
    }

    @Test
    fun `WHEN login clicked with short username SHOULD validate username`() = runTest {
        viewModel.onUsernameChange("a")
        viewModel.onLoginClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.USERNAME]).isEqualTo(
            LoginScreenState.ValidationError.TOO_SHORT_FIELD
        )
    }

    @Test
    fun `WHEN login clicked with short password SHOULD validate password`() = runTest {
        viewModel.onPasswordChange("1234567")
        viewModel.onLoginClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.PASSWORD]).isEqualTo(
            LoginScreenState.ValidationError.TOO_SHORT_FIELD
        )
    }

    @Test
    fun `WHEN login clicked with valid fields SHOULD reset validation`() = runTest {
        viewModel.onUsernameChange("user")
        viewModel.onPasswordChange("password")
        viewModel.onLoginClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.USERNAME]).isNull()
        assertThat(viewModel.screenState.value.errors[LoginScreenState.FormField.PASSWORD]).isNull()
    }

//    @Test
//    fun `WHEN login successful SHOULD call login THEN navigate to home`() = runTest {
//        coEvery { loginUseCase.login(any(), any()) } returns Unit
//
//        viewModel.effect.test {
//            viewModel.onUsernameChange("user")
//            viewModel.onPasswordChange("password")
//            viewModel.onLoginClick()
//            advanceUntilIdle()
//
//            coVerify(exactly = 1) { loginUseCase.login("user", "password") }
//            assertThat(awaitItem()).isEqualTo(LoginEffect.NavigateToHome)
//            cancelAndIgnoreRemainingEvents()
//        }
//    }

    @Test
    fun `loading indicator SHOULD hide after login`() = runTest {
        viewModel.onLoginClick()
        advanceUntilIdle()
        assertThat(viewModel.screenState.value.isLoading).isFalse()
    }

    class MainDispatcherRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}
