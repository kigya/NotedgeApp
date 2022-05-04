package com.kigya.notedgeapp.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.kigya.notedgeapp.domain.preferences.NoteAppPreferences
import com.kigya.notedgeapp.utils.constants.Constants.ONBOARDING
import javax.inject.Inject

class NoteAppPreferencesImpl @Inject constructor() : NoteAppPreferences {

    companion object {

        private var rootPreferences: SharedPreferences? = null
        private var instance: NoteAppPreferences? = null


        fun getDefaultPreferenceInstance(context: Context): NoteAppPreferences {
            if (instance == null) {
                instance = NoteAppPreferencesImpl()
                rootPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return instance!!
        }
    }

    override fun setOnboardingDone(status: Boolean) {
        rootPreferences?.edit()?.putBoolean(ONBOARDING, status)?.apply() ?: Unit
    }

    override fun getOnboardingPassedStatus(): Boolean = rootPreferences?.getBoolean(ONBOARDING, false) ?: false

    override fun clearPreferences() {
        rootPreferences?.edit { clear() }
    }

}