package com.example.myhealthpassport.di

import com.example.myhealthpassport.data.repository.HealthRepositoryImpl
import com.example.myhealthpassport.domain.repository.HealthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideHealthRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): HealthRepository = HealthRepositoryImpl(firestore, auth)
}
