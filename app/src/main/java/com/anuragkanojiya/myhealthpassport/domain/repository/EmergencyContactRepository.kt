package com.anuragkanojiya.myhealthpassport.domain.repository

import com.anuragkanojiya.myhealthpassport.data.local.database.EmergencyContactDao
import com.anuragkanojiya.myhealthpassport.data.local.database.Entity
import kotlinx.coroutines.flow.Flow

class EmergencyContactRepository(private val dao: EmergencyContactDao) {

    val allContacts: Flow<List<Entity>> = dao.getAllContacts()

    suspend fun insert(contact: Entity) {
        dao.insertContact(contact)
    }

    suspend fun delete(contact: Entity) {
        dao.deleteContact(contact)
    }
}