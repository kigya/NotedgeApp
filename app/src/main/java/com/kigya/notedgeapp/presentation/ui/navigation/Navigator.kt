package com.kigya.notedgeapp.presentation.ui.navigation

import java.util.*

interface Navigator {

    fun onNoteSelected(noteId: UUID)

    fun openNoteList(clearBackstack: Boolean = true, addToBackStack:Boolean = false)

    fun openOnboarding(clearBackstack: Boolean = true, addToBackStack:Boolean = false)

}