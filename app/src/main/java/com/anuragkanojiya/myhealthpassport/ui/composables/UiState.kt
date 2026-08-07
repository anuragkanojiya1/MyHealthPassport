package com.anuragkanojiya.myhealthpassport.ui.composables

import com.anuragkanojiya.myhealthpassport.domain.model.UserHealthData

/**
 * A sealed hierarchy describing the state of the text generation.
 */
sealed interface UiState {

    /**
     * Empty state when the screen is first shown
     */
    object Initial : UiState

    /**
     * Still loading
     */
    object Loading : UiState

    /**
     * Text has been generated
     */
    data class Success(val outputText: String) : UiState

    /**
     * Data has been extracted from a report
     */
    data class ExtractedData(val data: UserHealthData) : UiState

    /**
     * There was an error generating text
     */
    data class Error(val errorMessage: String) : UiState

    /**
     * Background task has been scheduled
     */
    object BackgroundScheduled : UiState
}
