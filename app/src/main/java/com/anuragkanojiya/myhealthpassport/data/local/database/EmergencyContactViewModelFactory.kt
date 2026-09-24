package com.anuragkanojiya.myhealthpassport.data.local.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anuragkanojiya.myhealthpassport.domain.repository.EmergencyContactRepository

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
