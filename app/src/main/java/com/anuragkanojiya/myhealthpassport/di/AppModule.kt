package com.anuragkanojiya.myhealthpassport.di

import android.content.Context
import androidx.work.WorkManager
import com.anuragkanojiya.myhealthpassport.data.repository.HealthRepositoryImpl
import com.anuragkanojiya.myhealthpassport.domain.repository.HealthRepository
import com.anuragkanojiya.myhealthpassport.util.CryptoManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(Timestamp::class.java, JsonSerializer<Timestamp> { src, _, _ ->
            com.google.gson.JsonPrimitive(src.seconds)
        })
        .registerTypeAdapter(Timestamp::class.java, JsonDeserializer { json, _, _ ->
            Timestamp(json.asLong, 0)
        })
        .create()

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = 
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideHealthRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        gson: Gson,
        cryptoManager: CryptoManager
    ): HealthRepository = HealthRepositoryImpl(firestore, auth, gson, cryptoManager)
}
