package com.anuragkanojiya.myhealthpassport.domain.usecase

import com.anuragkanojiya.myhealthpassport.data.datastore.GetApiKeyUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class GeminiAnalysisUseCaseTest {

    private val getApiKeyUseCase: GetApiKeyUseCase = mockk()
    private lateinit var useCase: GeminiAnalysisUseCase

    @Before
    fun setup() {
        useCase = GeminiAnalysisUseCase(getApiKeyUseCase)
    }

    @Test
    fun `extractMedicalReport correctly parses valid JSON`() {
        val json = """
            {
                "name": "John Doe",
                "bloodGroup": "O+",
                "age": 30,
                "systolicBP": 120,
                "diastolicBP": 80,
                "bloodSugarLevel": 90,
                "gender": "Male",
                "healthCondition": "Healthy",
                "emergencyPhoneNumber": "1234567890",
                "address": "123 Street",
                "allergies": "None",
                "medications": "None",
                "weight": 70.5,
                "height": 175.0
            }
        """.trimIndent()

        val result = useCase.extractMedicalReport(json)

        assertThat(result.name).isEqualTo("John Doe")
        assertThat(result.age).isEqualTo(30)
        assertThat(result.weight).isEqualTo(70.5f)
    }

    @Test
    fun `extractMedicalReport handles markdown wrapped JSON`() {
        val json = """
            ```json
            {
                "name": "Jane Doe",
                "age": "25"
            }
            ```
        """.trimIndent()

        val result = useCase.extractMedicalReport(json)

        assertThat(result.name).isEqualTo("Jane Doe")
        assertThat(result.age).isEqualTo(25)
    }

    @Test
    fun `extractMedicalReport handles invalid numeric strings`() {
        val json = """
            {
                "age": "25 years",
                "weight": "65.5 kg"
            }
        """.trimIndent()

        val result = useCase.extractMedicalReport(json)

        assertThat(result.age).isEqualTo(25)
        assertThat(result.weight).isEqualTo(65.5f)
    }

    @Test
    fun `extractMedicalReport provides default values for missing keys`() {
        val json = "{}"
        val result = useCase.extractMedicalReport(json)

        assertThat(result.name).isEqualTo("Unknown")
        assertThat(result.age).isEqualTo(0)
    }
}
