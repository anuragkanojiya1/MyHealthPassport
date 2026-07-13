package com.example.myhealthpassport.util

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoManager {

    /**
     * Encrypts the given bytes using the provided master key.
     * Uses AES/GCM/NoPadding.
     */
    fun encrypt(bytes: ByteArray, masterKey: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(IV_SIZE).apply { SecureRandom().nextBytes(this) }
        val keySpec = SecretKeySpec(masterKey, ALGORITHM)
        val gcmSpec = GCMParameterSpec(128, iv)
        
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        val encrypted = cipher.doFinal(bytes)
        
        // Combine IV and encrypted data for storage
        val combined = iv + encrypted
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    /**
     * Decrypts the base64 data using the provided master key.
     */
    fun decrypt(base64Data: String, masterKey: ByteArray): ByteArray {
        val combined = Base64.decode(base64Data, Base64.NO_WRAP)
        val iv = combined.copyOfRange(0, IV_SIZE)
        val encrypted = combined.copyOfRange(IV_SIZE, combined.size)

        val keySpec = SecretKeySpec(masterKey, ALGORITHM)
        val gcmSpec = GCMParameterSpec(128, iv)
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        return cipher.doFinal(encrypted)
    }

    /**
     * Generates a new random 256-bit AES key.
     */
    fun generateRandomKey(): ByteArray {
        val key = ByteArray(32) // 256 bits
        SecureRandom().nextBytes(key)
        return key
    }

    companion object {
        private const val ALGORITHM = "AES"
        private const val BLOCK_MODE = "GCM"
        private const val PADDINGS = "NoPadding"
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDINGS"
        private const val IV_SIZE = 12
    }
}
