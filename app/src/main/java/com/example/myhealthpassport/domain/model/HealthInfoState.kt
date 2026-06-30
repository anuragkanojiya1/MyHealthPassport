package com.example.myhealthpassport.domain.model

data class HealthInfoState(
    val medicalID: String = "",
    val name: String = "",
    val bloodGroup: String = "",
    val age: String = "",
    val systolicBP: String = "",
    val diastolicBP: String = "",
    val bloodSugarLevel: String = "",
    val weight: String = "",
    val height: String = "",
    val gender: String = "",
    val healthCondition: String = "",
    val emergencyPhoneNumber: String = "",
    val address: String = "",
    val allergies: String = "",
    val medications: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)