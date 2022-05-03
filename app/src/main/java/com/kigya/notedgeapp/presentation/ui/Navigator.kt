package com.kigya.notedgeapp.presentation.ui

import androidx.fragment.app.Fragment
import java.util.*

fun Fragment.navigator() = requireActivity() as  Navigator

interface Navigator {

    fun onNoteSelected(noteId: UUID)

}