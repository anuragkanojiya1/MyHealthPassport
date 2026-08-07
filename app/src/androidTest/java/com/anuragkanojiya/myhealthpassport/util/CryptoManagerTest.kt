package com.anuragkanojiya.myhealthpassport.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CryptoManagerTest {

    private lateinit var cryptoManager: CryptoManager

    @Before
    fun setup() {
        cryptoManager = CryptoManager()
    }

    @Test
    fun encryptionAndDecryption_returnsOriginalData() {
        val originalText = "Sensitive Health Data"
        val masterKey = cryptoManager.generateRandomKey()
        
        val encrypted = cryptoManager.encrypt(originalText.toByteArray(), masterKey)
        val decrypted = cryptoManager.decrypt(encrypted, masterKey).decodeToString()
        
        assertThat(decrypted).isEqualTo(originalText)
    }

    @Test
    fun encryptionWithDifferentKeys_producesDifferentResults() {
        val data = "Important Data".toByteArray()
        val key1 = cryptoManager.generateRandomKey()
        val key2 = cryptoManager.generateRandomKey()
        
        val encrypted1 = cryptoManager.encrypt(data, key1)
        val encrypted2 = cryptoManager.encrypt(data, key2)
        
        assertThat(encrypted1).isNotEqualTo(encrypted2)
    }

    @Test(expected = Exception::class)
    fun decryptionWithWrongKey_fails() {
        val data = "Important Data".toByteArray()
        val key1 = cryptoManager.generateRandomKey()
        val key2 = cryptoManager.generateRandomKey()
        
        val encrypted = cryptoManager.encrypt(data, key1)
        cryptoManager.decrypt(encrypted, key2)
    }
}
