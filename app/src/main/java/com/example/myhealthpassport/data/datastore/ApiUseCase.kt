package com.example.myhealthpassport.data.datastore

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveApiKeyUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(key: String) = repository.saveApiKey(key)
}

class GetApiKeyUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<String?> = repository.apiKeyFlow
}