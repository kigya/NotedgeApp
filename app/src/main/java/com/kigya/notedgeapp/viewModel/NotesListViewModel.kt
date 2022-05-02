package com.kigya.notedgeapp.viewModel

import androidx.lifecycle.ViewModel
import com.kigya.notedgeapp.repository.NoteRepository

class NotesListViewModel : ViewModel() {

    private val crimeRepository = NoteRepository.get()
    val crimeListLiveData = crimeRepository.getNotes()

}