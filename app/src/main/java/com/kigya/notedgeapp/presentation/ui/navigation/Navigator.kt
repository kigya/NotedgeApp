package com.kigya.notedgeapp.presentation.ui.navigation

import java.util.*

interface Navigator {

    fun onNoteSelected(noteId: UUID)

    fun openNoteList(
        firstTime: Boolean = false,
        addToBackStack: Boolean = true,
        clearBackstack: Boolean = false
    )

    fun openOnboarding(
        firstTime: Boolean = false,
        addToBackStack: Boolean = true,
        clearBackstack: Boolean = false
    )

}