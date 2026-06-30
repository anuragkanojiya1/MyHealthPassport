package com.example.myhealthpassport.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myhealthpassport.domain.model.HealthInfoState
import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.domain.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val repository: HealthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthInfoState())
    val uiState = _uiState.asStateFlow()

    fun updateState(newState: HealthInfoState) {
        _uiState.value = newState
    }

    fun saveHealthData(
        userHealthData: UserHealthData,
        context: Context
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = repository.saveHealthData(userHealthData)
            _uiState.update { it.copy(isLoading = false) }
            
            result.onSuccess {
                Toast.makeText(context, "Successfully Saved your Data", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                Toast.makeText(context, e.message ?: "Error saving data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchMedicalIDs(
        context: Context,
        onResult: (List<String>) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.fetchMedicalIDs()
            result.onSuccess { ids ->
                onResult(ids)
            }.onFailure { e ->
                Toast.makeText(context, "Error fetching medical IDs: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun retrieveHealthData(
        medicalID: String,
        context: Context,
        onData: (UserHealthData) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.retrieveHealthData(medicalID)
            result.onSuccess { data ->
                onData(data)
            }.onFailure { e ->
                Toast.makeText(context, e.message ?: "Error retrieving data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun delete(
        medicalID: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch {
            val result = repository.deleteHealthData(medicalID)
            result.onSuccess {
                Toast.makeText(context, "Successfully Deleted Data", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }.onFailure { e ->
                Toast.makeText(context, e.message ?: "Error deleting data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun getLatestHealthData(context: Context): UserHealthData? {
        return repository.getLatestHealthData()
    }

    fun fetchAllHealthData(
        context: Context,
        onResult: (List<UserHealthData>) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.fetchAllHealthData()
            result.onSuccess { data ->
                onResult(data)
            }.onFailure { e ->
                Toast.makeText(context, "Error fetching all records: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
