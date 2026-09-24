package com.anuragkanojiya.myhealthpassport.data.repository

import android.util.Base64
import com.anuragkanojiya.myhealthpassport.domain.model.UserHealthData
import com.anuragkanojiya.myhealthpassport.domain.repository.HealthRepository
import com.anuragkanojiya.myhealthpassport.core.security.CryptoManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
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
    // Cache key in memory tied to UID to prevent cross-account issues
    private var cachedKeyEntry: Pair<String, ByteArray>? = null

    private val userId: String?
        get() = auth.currentUser?.uid

    private fun getHealthCollection() = firestore
        .collection("users")
        .document(userId ?: "anonymous") // Fallback to avoid "null" string, but better to check
        .collection("health")

    private fun getSecretDocument() = firestore
        .collection("users")
        .document(userId ?: "anonymous")
        .collection("secret")
        .document("metadata")

    /**
     * Retrieves the user's master key from Firestore.
     * Always fetches from SERVER to ensure we don't overwrite an existing key due to local cache lag.
     */
    private suspend fun getMasterKey(): ByteArray = mutex.withLock {
        val currentUid = userId ?: throw IllegalStateException("User must be logged in to access encrypted data")
        
        // 1. Return memory cache if UID matches
        if (cachedKeyEntry?.first == currentUid) {
            return cachedKeyEntry!!.second
        }

        // 2. Fetch from Server (Crucial for multi-device sync)
        return try {
            val doc = getSecretDocument().get(Source.SERVER).await()
            val keyBase64 = doc.getString("masterKey")

            if (keyBase64 != null) {
                val key = Base64.decode(keyBase64, Base64.NO_WRAP)
                cachedKeyEntry = currentUid to key
                key
            } else {
                // 3. Only generate if it truly doesn't exist on server
                val newKey = cryptoManager.generateRandomKey()
                val newKeyBase64 = Base64.encodeToString(newKey, Base64.NO_WRAP)
                getSecretDocument().set(mapOf("masterKey" to newKeyBase64)).await()
                cachedKeyEntry = currentUid to newKey
                newKey
            }
        } catch (e: Exception) {
            // Fallback to cache if server fetch fails (e.g. offline)
            val doc = getSecretDocument().get(Source.CACHE).await()
            val keyBase64 = doc.getString("masterKey")
            if (keyBase64 != null) {
                val key = Base64.decode(keyBase64, Base64.NO_WRAP)
                cachedKeyEntry = currentUid to key
                key
            } else {
                throw e // No key anywhere
            }
        }
    }

    override suspend fun saveHealthData(data: UserHealthData): Result<Unit> = try {
        val uid = userId ?: throw IllegalStateException("User not logged in")
        val collection = firestore.collection("users").document(uid).collection("health")
        
        val id = data.medicalID.ifBlank { collection.document().id }
        val finalData = data.copy(medicalID = id)
        
        val masterKey = getMasterKey()
        val encryptedPayload = cryptoManager.encrypt(gson.toJson(finalData).toByteArray(), masterKey)
        
        val storageMap = mapOf(
            "medicalID" to id,
            "timestamp" to finalData.timestamp,
            "payload" to encryptedPayload
        )
        
        collection.document(id).set(storageMap).await()
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
                gson.fromJson(decryptedJson, UserHealthData::class.java)?.apply {
                    // Sanitize fields
                    if (medications == null) medications = ""
                    if (allergies == null) allergies = ""
                    if (healthCondition == null) healthCondition = ""
                    if (address == null) address = ""
                    if (gender == null) gender = ""
                    if (bloodGroup == null) bloodGroup = ""
                    if (name == null) name = ""
                    if (timestamp == null) timestamp = com.google.firebase.Timestamp.now()
                }
            } catch (e: Exception) {
                android.util.Log.e("HealthRepository", "Decryption failed for a record. Possible key mismatch.", e)
                null
            }
        } else {
            null
        }
    }
}
