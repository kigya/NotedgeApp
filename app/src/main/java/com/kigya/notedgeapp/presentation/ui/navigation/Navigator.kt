package com.kigya.notedgeapp.presentation.ui.navigation

import com.kigya.notedgeapp.data.model.Note

interface Navigator {

    fun onNoteSelected(note: Note)

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