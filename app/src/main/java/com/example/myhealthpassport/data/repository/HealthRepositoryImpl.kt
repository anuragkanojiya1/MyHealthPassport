package com.example.myhealthpassport.data.repository

import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.domain.repository.HealthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : HealthRepository {

    private val userId: String?
        get() = auth.currentUser?.uid

    private fun getHealthCollection() = firestore
        .collection("users")
        .document(userId.toString())
        .collection("health")

    override suspend fun saveHealthData(data: UserHealthData): Result<Unit> = try {
        val finalData = if (data.medicalID.isBlank()) {
            val generatedId = getHealthCollection().document().id
            data.copy(medicalID = generatedId)
        } else {
            data
        }
        getHealthCollection().document(finalData.medicalID).set(finalData).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun fetchMedicalIDs(): Result<List<String>> = try {
        val snapshot = getHealthCollection()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        Result.success(snapshot.documents.map { it.id })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun retrieveHealthData(medicalID: String): Result<UserHealthData> = try {
        val document = getHealthCollection().document(medicalID).get().await()
        val data = document.toObject(UserHealthData::class.java)
        if (data != null) Result.success(data) else Result.failure(Exception("Record not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteHealthData(medicalID: String): Result<Unit> = try {
        getHealthCollection().document(medicalID).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getLatestHealthData(): UserHealthData? = try {
        val snapshot = getHealthCollection()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()
        snapshot.documents.firstOrNull()?.toObject(UserHealthData::class.java)
    } catch (e: Exception) {
        null
    }

    override suspend fun fetchAllHealthData(): Result<List<UserHealthData>> = try {
        val snapshot = getHealthCollection()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        val dataList = snapshot.documents.mapNotNull { it.toObject(UserHealthData::class.java) }
        Result.success(dataList)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
