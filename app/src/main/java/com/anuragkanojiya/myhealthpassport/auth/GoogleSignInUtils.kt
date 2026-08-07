package com.anuragkanojiya.myhealthpassport.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.anuragkanojiya.myhealthpassport.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

class GoogleSignInUtils {

    companion object {

        private const val TAG = "GoogleSignIn"

        suspend fun signIn(context: Context): Result<Unit> {

            // Diagnostic: Check Google Play Services
            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
            if (resultCode != ConnectionResult.SUCCESS) {
                Log.e(TAG, "Google Play Services not available: $resultCode")
                return Result.failure(Exception("Google Play Services is not available or outdated. Error code: $resultCode"))
            }

            return try {
                val credentialManager = CredentialManager.create(context)
                val clientId = context.getString(R.string.web_client_id)
                
                Log.d(TAG, "Attempting sign-in with Client ID: $clientId")

                // 1. Google ID Option
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(false)
                    .setServerClientId(clientId)
                    .build()

                // 2. Password Option (Forces the UI to show even if Google ID metadata is stale)
                val passwordOption = GetPasswordOption()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .addCredentialOption(passwordOption)
                    .build()

                // Show the selector UI
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                when {
                    credential is CustomCredential &&
                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                        
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        
                        Firebase.auth.signInWithCredential(firebaseCredential).await()
                        Result.success(Unit)
                    }
                    else -> Result.failure(Exception("Unexpected credential type: ${credential.type}"))
                }

            } catch (e: NoCredentialException) {
                Log.w(TAG, "No credentials found. Web Client ID: ${context.getString(R.string.web_client_id)}")
                Log.w(TAG, "Exception Details: ${e.message}")
                Result.failure(Exception("No accounts found. This usually means the SHA-1 fingerprint of this app isn't registered in your Firebase Console or the Web Client ID is incorrect. Check Logcat for details."))
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Credential error: ${e.type} - ${e.message}", e)
                Result.failure(e)
            } catch (e: Exception) {
                Log.e(TAG, "Google sign-in failed: ${e.message}", e)
                Result.failure(e)
            }
        }

        suspend fun signOut(context: Context): Result<Unit> {
            return try {

                FirebaseAuth.getInstance().signOut()

                val credentialManager = CredentialManager.create(context)

                withContext(Dispatchers.IO) {
                    credentialManager.clearCredentialState(
                        ClearCredentialStateRequest()
                    )
                }

                Log.d(TAG, "Sign-out successful")

                Result.success(Unit)

            } catch (e: Exception) {
                Log.e(TAG, "Sign-out failed", e)
                Result.failure(e)
            }
        }
    }
}
