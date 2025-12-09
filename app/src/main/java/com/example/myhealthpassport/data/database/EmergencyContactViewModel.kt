package com.example.myhealthpassport.data.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmergencyContactViewModel(private val repository: EmergencyContactRepository) : ViewModel() {

    val contacts = repository.allContacts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addContact(name: String, phone: String) {
        viewModelScope.launch {
            repository.insert(Entity(name = name, phone = phone))
        }
    }

    fun removeContact(contact: Entity) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }
}
