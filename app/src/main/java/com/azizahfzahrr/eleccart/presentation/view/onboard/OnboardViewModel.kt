package com.azizahfzahrr.eleccart.presentation.view.onboard

import androidx.lifecycle.ViewModel
import com.azizahfzahrr.eleccart.data.source.local.PreferencedDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val preferenceDataStore: PreferencedDataStore
) : ViewModel() {

    suspend fun setOnboard(onboarded: Boolean) {
        preferenceDataStore.setOnboardStatus(onboarded)
    }
}