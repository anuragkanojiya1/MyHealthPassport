package com.example.myhealthpassport.domain.repository

import com.example.myhealthpassport.domain.model.UserHealthData

interface HealthRepository {
    suspend fun saveHealthData(data: UserHealthData): Result<Unit>
    suspend fun fetchMedicalIDs(): Result<List<String>>
    suspend fun retrieveHealthData(medicalID: String): Result<UserHealthData>
    suspend fun deleteHealthData(medicalID: String): Result<Unit>
    suspend fun getLatestHealthData(): UserHealthData?
    suspend fun fetchAllHealthData(): Result<List<UserHealthData>>
}
