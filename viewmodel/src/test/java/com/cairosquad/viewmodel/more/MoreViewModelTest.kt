package com.cairosquad.viewmodel.more

import com.cairosquad.domain.usecase.AccountUseCase
import com.cairosquad.domain.usecase.GetAppVersionUseCase
import com.cairosquad.domain.usecase.LanguageManagerUseCase
import com.cairosquad.domain.usecase.LoginUseCase
import com.cairosquad.domain.usecase.ThemeManagerUseCase
import com.cairosquad.entity.Language
import com.cairosquad.entity.Theme
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoreViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var themeManagerUseCase: ThemeManagerUseCase
    private lateinit var languageManagerUseCase: LanguageManagerUseCase
    private lateinit var versionUseCase: GetAppVersionUseCase
    private lateinit var accountUseCase: AccountUseCase

    private lateinit var viewModel: MoreViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher

        loginUseCase = mockk(relaxed = true)
        themeManagerUseCase = mockk(relaxed = true)
        languageManagerUseCase = mockk(relaxed = true)
        versionUseCase = mockk(relaxed = true)
        accountUseCase = mockk(relaxed = true)

        coEvery { themeManagerUseCase.getTheme() } returns flowOf(Theme.DARK)
        coEvery { languageManagerUseCase.getLanguage() } returns flowOf(Language("ar", "Arabic"))
        every { versionUseCase.getAppVersion() } returns "1.0.0"

        viewModel = MoreViewModel(
            loginUseCase,
            themeManagerUseCase,
            languageManagerUseCase,
            versionUseCase,
            accountUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should set initial theme and language from use cases`() = runTest {
        assertThat(viewModel.screenState.value.currentTheme).isEqualTo(MoreScreenState.Theme.DARK)
        assertThat(viewModel.screenState.value.currentLanguage)
            .isEqualTo(MoreScreenState.Language("ar", "Arabic"))
        assertThat(viewModel.screenState.value.appVersion).isEqualTo("1.0.0")
    }

    @Test
    fun `should open and close theme bottom sheet`() {
        viewModel.onThemeClick()
        assertThat(viewModel.screenState.value.isThemeBottomSheetOpen).isTrue()

        viewModel.onThemeBottomSheetDismiss()
        assertThat(viewModel.screenState.value.isThemeBottomSheetOpen).isFalse()
    }

    @Test
    fun `should save theme and close bottom sheet on confirm`() = runTest {
        val theme = MoreScreenState.Theme.LIGHT
        viewModel.onThemeBottomSheetConfirm(theme)
        advanceUntilIdle()

        coVerify { themeManagerUseCase.saveTheme(Theme.LIGHT) }
        assertThat(viewModel.screenState.value.isThemeBottomSheetOpen).isFalse()
        assertThat(viewModel.screenState.value.currentTheme).isEqualTo(theme)
    }

    @Test
    fun `should open and close language bottom sheet`() {
        viewModel.onLanguageClick()
        assertThat(viewModel.screenState.value.isLanguageBottomSheetOpen).isTrue()

        viewModel.onLanguageBottomSheetDismiss()
        assertThat(viewModel.screenState.value.isLanguageBottomSheetOpen).isFalse()
    }

    @Test
    fun `should save language and close bottom sheet on confirm`() = runTest {
        val language = MoreScreenState.Language("en", "English")
        viewModel.onLanguageBottomSheetConfirm(language)
        advanceUntilIdle()

        coVerify { languageManagerUseCase.saveLanguage(Language("en", "English")) }
        assertThat(viewModel.screenState.value.isLanguageBottomSheetOpen).isFalse()
        assertThat(viewModel.screenState.value.currentLanguage).isEqualTo(language)
    }

    @Test
    fun `should show logout confirmation on logout click`() {
        viewModel.onLogoutClick()
        assertThat(viewModel.screenState.value.isLogoutButtonVisible).isTrue()
    }

    @Test
    fun `should logout and hide confirmation on logout confirm`() = runTest {
        viewModel.onLogoutConfirm()
        advanceUntilIdle()

        coVerify { loginUseCase.logout() }
        coVerify { accountUseCase.removeAccountDetails() }
        assertThat(viewModel.screenState.value.isLogoutButtonVisible).isFalse()
    }

    @Test
    fun `should hide logout confirmation on logout dismiss`() {
        viewModel.onLogoutDismiss()
        assertThat(viewModel.screenState.value.isLogoutButtonVisible).isFalse()
    }
}