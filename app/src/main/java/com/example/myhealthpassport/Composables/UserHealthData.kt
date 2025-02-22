package com.example.myhealthpassport.Composables

data class UserHealthData(
    var medicalID: String="",
    var name: String="",
    var bloodGroup: String="",
    var age: Int=0,
    var systolicBP: Int = 0,
    var diastolicBP: Int = 0,
    var bloodSugarLevel: Int=0,
    var weight: Float = 0.0f,
    var height: Float = 0.0f,
    var gender: String="",
    var healthCondition: String="",
    var emergencyPhoneNumber: String="",
    var address: String="",
    var allergies: String="",
    var medications: String=""
)
