package com.example.myhealthpassport.data.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EmergencyContactViewModelFactory(private val repository: EmergencyContactRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmergencyContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmergencyContactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
