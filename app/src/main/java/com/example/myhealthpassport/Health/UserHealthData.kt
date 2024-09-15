package com.example.myhealthpassport.Health

data class UserHealthData(
    var medicalID: String="",
    var name: String="",
    var bloodGroup: String="",
    var age: Int=0,
    var weight: Float = 0.0f,
    var height: Float = 0.0f,
    var gender: String="",
    var healthCondition: String="",
    var emergencyPhoneNumber: Long = 0,
    var address: String="",
    var allergies: String="",
    var medications: String=""
)
