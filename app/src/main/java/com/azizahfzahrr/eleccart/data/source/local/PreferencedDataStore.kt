package com.azizahfzahrr.eleccart.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object DataStoreConstant {
    val IS_LOGIN = booleanPreferencesKey("IS_LOGIN")
    val IS_ONBOARD = booleanPreferencesKey("IS_ONBOARD")
}

class PreferencedDataStore constructor(private val dataStore: DataStore<Preferences>) {

    val userOnboardStatus: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DataStoreConstant.IS_ONBOARD] ?: false
    }

    val userLoginStatus: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DataStoreConstant.IS_LOGIN] ?: false
    }

    suspend fun setOnboardStatus(onboarded: Boolean) {
        dataStore.edit { preferences ->
            preferences[DataStoreConstant.IS_ONBOARD] = onboarded
        }
        Log.d("PreferencedDataStore", "Onboard status set to: $onboarded")
    }

    suspend fun getUserOnboard(): Boolean {
        return userOnboardStatus.firstOrNull() ?: false
    }

    suspend fun setUserLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[DataStoreConstant.IS_LOGIN] = isLoggedIn
        }
        Log.d("PreferencedDataStore", "Login status set to: $isLoggedIn")
    }

    suspend fun isUserLoggedIn(): Boolean {
        return userLoginStatus.firstOrNull() ?: false
    }

    companion object {
        @Volatile
        private var INSTANCE: PreferencedDataStore? = null

        fun getInstance(dataStore: DataStore<Preferences>): PreferencedDataStore {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencedDataStore(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
