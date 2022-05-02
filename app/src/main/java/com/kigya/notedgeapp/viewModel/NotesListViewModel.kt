package com.kigya.notedgeapp.viewModel

import androidx.lifecycle.ViewModel
import com.kigya.notedgeapp.Note
import com.kigya.notedgeapp.repository.NoteRepository

class NotesListViewModel : ViewModel() {

    private val noteRepository = NoteRepository.get()
    val noteListLiveData = noteRepository.getNotes()

    fun addNote(note: Note) {
        noteRepository.addNote(note)
    }

}