package com.anuragkanojiya.myhealthpassport.viewmodels

import app.cash.turbine.test
import com.anuragkanojiya.myhealthpassport.data.datastore.UserPreferencesRepository
import com.anuragkanojiya.myhealthpassport.domain.model.SettingsEvent
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val repository: UserPreferencesRepository = mockk(relaxed = true)
    private val auth: FirebaseAuth = mockk(relaxed = true)
    private val firebaseUser: FirebaseUser = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.email } returns "test@example.com"
        every { repository.isDarkModeFlow } returns flowOf(false)
        every { repository.apiKeyFlow } returns flowOf("test-api-key")

        viewModel = SettingsViewModel(repository, auth, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.userEmail).isEqualTo("test@example.com")
            assertThat(state.isDarkMode).isFalse()
            assertThat(state.hasApiKey).isTrue()
        }
    }

    @Test
    fun `ToggleDarkMode event calls repository`() = runTest {
        viewModel.onEvent(SettingsEvent.ToggleDarkMode(true))
        coVerify { repository.setDarkMode(true) }
    }

    @Test
    fun `SignOut event calls auth signOut`() = runTest {
        viewModel.onEvent(SettingsEvent.SignOut)
        verify { auth.signOut() }
    }

    @Test
    fun `ShowNameDialog updates state`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial
            viewModel.onEvent(SettingsEvent.ShowNameDialog(true))
            val state = awaitItem()
            assertThat(state.isNameDialogOpen).isTrue()
        }
    }
}
