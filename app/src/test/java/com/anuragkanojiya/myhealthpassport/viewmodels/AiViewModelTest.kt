package com.anuragkanojiya.myhealthpassport.viewmodels

import app.cash.turbine.test
import androidx.work.WorkManager
import com.anuragkanojiya.myhealthpassport.domain.model.UserHealthData
import com.anuragkanojiya.myhealthpassport.domain.repository.HealthRepository
import com.anuragkanojiya.myhealthpassport.domain.usecase.GeminiAnalysisUseCase
import com.anuragkanojiya.myhealthpassport.ui.composables.UiState
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AiViewModelTest {

    private val geminiAnalysisUseCase: GeminiAnalysisUseCase = mockk()
    private val healthRepository: HealthRepository = mockk(relaxed = true)
    private val workManager: WorkManager = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: AiViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { healthRepository.fetchMedicalIDs() } returns Result.success(listOf("ID1", "ID2"))
        viewModel = AiViewModel(geminiAnalysisUseCase, healthRepository, workManager, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Initial and medicalIds are fetched`() = runTest {
        assertThat(viewModel.uiState.value).isEqualTo(UiState.Initial)
        assertThat(viewModel.medicalIds.value).containsExactly("ID1", "ID2")
    }

    @Test
    fun `analyzeData success calls onResult`() = runTest {
        val prompt = "Test prompt"
        val expectedResult = "AI Response"
        coEvery { geminiAnalysisUseCase.analyzeData(prompt) } returns Result.success(expectedResult)

        var result: String? = null
        viewModel.analyzeData(prompt) { result = it }

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `saveExtractedData success updates state to Success`() = runTest {
        val data = UserHealthData(name = "John", medicalID = "123")
        coEvery { healthRepository.saveHealthData(data) } returns Result.success(Unit)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Initial)
            viewModel.saveExtractedData(data)
            assertThat(awaitItem()).isEqualTo(UiState.Loading)
            val successState = awaitItem() as UiState.Success
            assertThat(successState.outputText).contains("John")
        }
    }

    @Test
    fun `saveExtractedData failure updates state to Error`() = runTest {
        val data = UserHealthData(name = "John", medicalID = "123")
        coEvery { healthRepository.saveHealthData(data) } returns Result.failure(Exception("Save failed"))

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(UiState.Initial)
            viewModel.saveExtractedData(data)
            assertThat(awaitItem()).isEqualTo(UiState.Loading)
            val errorState = awaitItem() as UiState.Error
            assertThat(errorState.errorMessage).contains("Save failed")
        }
    }

    @Test
    fun `resetState sets state to Initial`() = runTest {
        viewModel.resetState()
        assertThat(viewModel.uiState.value).isEqualTo(UiState.Initial)
    }
}
