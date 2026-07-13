package com.example.myhealthpassport.data.repository

import com.example.myhealthpassport.domain.model.UserHealthData
import com.example.myhealthpassport.domain.repository.HealthRepository
import com.example.myhealthpassport.util.CryptoManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val gson: Gson,
    private val cryptoManager: CryptoManager
) : HealthRepository {

    private val mutex = Mutex()
    private var cachedMasterKey: ByteArray? = null

    private val userId: String?
        get() = auth.currentUser?.uid

    private fun getHealthCollection() = firestore
        .collection("users")
        .document(userId.toString())
        .collection("health")

    private fun getSecretDocument() = firestore
        .collection("users")
        .document(userId.toString())
        .collection("secret")
        .document("metadata")

    /**
     * Retrieves the user's master key from Firestore or generates a new one.
     * This key is tied to the user's account and persists across uninstalls.
     */
    private suspend fun getMasterKey(): ByteArray = mutex.withLock {
        cachedMasterKey?.let { return it }

        val doc = getSecretDocument().get().await()
        val keyBase64 = doc.getString("masterKey")

        if (keyBase64 != null) {
            val key = android.util.Base64.decode(keyBase64, android.util.Base64.NO_WRAP)
            cachedMasterKey = key
            key
        } else {
            val newKey = cryptoManager.generateRandomKey()
            val newKeyBase64 = android.util.Base64.encodeToString(newKey, android.util.Base64.NO_WRAP)
            getSecretDocument().set(mapOf("masterKey" to newKeyBase64)).await()
            cachedMasterKey = newKey
            newKey
        }
    }

    override suspend fun saveHealthData(data: UserHealthData): Result<Unit> = try {
        val id = data.medicalID.ifBlank {
            getHealthCollection().document().id
        }
        val finalData = data.copy(medicalID = id)
        
        val masterKey = getMasterKey()
        val encryptedPayload = cryptoManager.encrypt(gson.toJson(finalData).toByteArray(), masterKey)
        
        val storageMap = mapOf(
            "medicalID" to id,
            "timestamp" to finalData.timestamp,
            "payload" to encryptedPayload
        )
        
        getHealthCollection().document(id).set(storageMap).await()
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
        val data = document.data?.let { decryptData(it) }
        if (data != null) Result.success(data) else Result.failure(Exception("Record not found or decryption failed"))
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
        snapshot.documents.firstOrNull()?.data?.let { decryptData(it) }
    } catch (e: Exception) {
        null
    }

    override suspend fun fetchAllHealthData(): Result<List<UserHealthData>> = try {
        val snapshot = getHealthCollection()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get().await()
        val dataList = snapshot.documents.mapNotNull { doc ->
            doc.data?.let { decryptData(it) }
        }
        Result.success(dataList)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun decryptData(document: Map<String, Any?>): UserHealthData? {
        val payload = document["payload"] as? String
        return if (payload != null) {
            try {
                val masterKey = getMasterKey()
                val decryptedJson = cryptoManager.decrypt(payload, masterKey).decodeToString()
                gson.fromJson(decryptedJson, UserHealthData::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
}
