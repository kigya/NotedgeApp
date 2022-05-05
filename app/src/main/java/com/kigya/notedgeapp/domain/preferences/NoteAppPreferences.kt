package com.kigya.notedgeapp.domain.preferences

interface NoteAppPreferences {

    fun setOnboardingDone(status: Boolean)

    fun getOnboardingPassedStatus(): Boolean

    fun clearPreferences()

}