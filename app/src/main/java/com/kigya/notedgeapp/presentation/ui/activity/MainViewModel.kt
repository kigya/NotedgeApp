package com.kigya.notedgeapp.presentation.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kigya.notedgeapp.data.model.Event
import com.kigya.notedgeapp.data.model.MutableLiveEvent
import com.kigya.notedgeapp.data.model.share
import com.kigya.notedgeapp.domain.preferences.NoteAppPreferences
import com.kigya.notedgeapp.presentation.ui.activity.Directions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteAppPreferences: NoteAppPreferences
): ViewModel() {

    private val _warpToFragment = MutableLiveEvent<Directions>()
    val warpToFragment = _warpToFragment.share()

    fun defineIfFirstTime(){
        viewModelScope.launch {
            delay(1500)
            if (noteAppPreferences.getOnboardingPassedStatus()){
                _warpToFragment.value = Event(Directions.MainFragment)
            }else{
                _warpToFragment.value = Event(Directions.Onboarding)
            }
        }
    }

}