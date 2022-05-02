package com.kigya.notedgeapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kigya.notedgeapp.Note
import com.kigya.notedgeapp.repository.NoteRepository
import java.util.*

class NoteDetailViewModel : ViewModel() {

    private val noteRepository = NoteRepository.get()
    private val noteIdLiveData = MutableLiveData<UUID>()

    val noteLiveData: LiveData<Note?> =
        Transformations.switchMap(noteIdLiveData) { noteId ->
            noteRepository.getNote(noteId)
        }

    fun loadNote(noteId: UUID) {
        noteIdLiveData.value = noteId
    }

    fun daveNote(note: Note) {
        noteRepository.updateNote(note)
    }

}