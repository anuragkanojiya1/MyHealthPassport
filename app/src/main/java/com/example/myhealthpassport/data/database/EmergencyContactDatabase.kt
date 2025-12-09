package com.example.myhealthpassport.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Entity::class], version = 1, exportSchema = false)
abstract class EmergencyContactDatabase : RoomDatabase() {
    abstract fun contactDao(): EmergencyContactDao

    companion object {
        @Volatile
        private var INSTANCE: EmergencyContactDatabase? = null

        fun getDatabase(context: Context): EmergencyContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmergencyContactDatabase::class.java,
                    "emergency_contacts_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
