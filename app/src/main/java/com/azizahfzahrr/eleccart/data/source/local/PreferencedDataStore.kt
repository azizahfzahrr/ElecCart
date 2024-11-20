package com.azizahfzahrr.eleccart.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object DataStoreConstant {
    val IS_LOGIN = booleanPreferencesKey("IS_LOGIN")
}

class PreferencedDataStore private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun setUserLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences -> preferences[DataStoreConstant.IS_LOGIN] = isLoggedIn }
        Log.d("PreferenceDataStore", "User logged in: $isLoggedIn")
    }

    suspend fun isUserLoggedIn(): Boolean {
        return withContext(Dispatchers.IO) {
            dataStore.data.first()[DataStoreConstant.IS_LOGIN] ?: false
        }
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