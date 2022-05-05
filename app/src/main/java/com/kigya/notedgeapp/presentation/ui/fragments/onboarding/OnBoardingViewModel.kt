package com.kigya.notedgeapp.presentation.ui.fragments.onboarding

import androidx.lifecycle.ViewModel
import com.kigya.notedgeapp.domain.preferences.NoteAppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val noteAppPreferences: NoteAppPreferences
) : ViewModel() {

    fun setOnboardingDone(status: Boolean) {
        noteAppPreferences.setOnboardingDone(status)
    }

}