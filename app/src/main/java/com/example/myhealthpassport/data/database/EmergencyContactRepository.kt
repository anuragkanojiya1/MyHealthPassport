package com.example.myhealthpassport.data.database

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
