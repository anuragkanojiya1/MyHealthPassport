package com.example.myhealthpassport.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Entity)

    @Delete
    suspend fun deleteContact(contact: Entity)

    @Query("SELECT * FROM emergency_contacts ORDER BY id DESC")
    fun getAllContacts(): Flow<List<Entity>>
}
