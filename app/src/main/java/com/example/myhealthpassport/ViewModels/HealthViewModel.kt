package com.example.myhealthpassport.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.myhealthpassport.Composables.UserHealthData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthViewModel: ViewModel(){

    fun saveHealthData(
        userHealthData: UserHealthData,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(userId.toString())
            .collection("health")
            .document(userHealthData.medicalID)

        try {
            firestoreRef.set(userHealthData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Saved your Data", Toast.LENGTH_SHORT)
                        .show()
                }
        } catch (e:Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchMedicalIDs(
        context: Context,
        onResult: (List<String>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return@launch
        }

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(userId)
            .collection("health")

        try {
            firestoreRef.get()
                .addOnSuccessListener { documents ->
                    val medicalIDs = documents.map { it.id }
                    Log.d("Medical ID:", medicalIDs.toString())
                    onResult(medicalIDs)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error fetching medical IDs: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveHealthData(
        medicalID: String,
        context: Context,
        data: (UserHealthData) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(userId.toString())
            .collection("health")
            .document(medicalID)

        try {
            firestoreRef.get()
                .addOnSuccessListener {
                    if(it.exists()){
                        val userHealthData = it.toObject<UserHealthData>()!!
                        data(userHealthData)
                    } else{
                        Toast.makeText(context, "No Health Data found in Database", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e:Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun delete(
        medicalID: String,
        context: Context,
        navController: NavController
    ) = CoroutineScope(Dispatchers.IO).launch {

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val firestoreRef = Firebase.firestore
            .collection("users")
            .document(userId.toString())
            .collection("health")
            .document(medicalID)

        try {
            firestoreRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Deleted Data", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
        } catch (e:Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}