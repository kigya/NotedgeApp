package com.kigya.notedgeapp.presentation.ui.fragments.note_list.viewmodel

import androidx.lifecycle.*
import com.kigya.notedgeapp.data.model.Event
import com.kigya.notedgeapp.data.model.MutableLiveEvent
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.share
import com.kigya.notedgeapp.domain.repository.NoteRepository
import com.kigya.notedgeapp.presentation.common.NoteActionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel(), NoteActionListener, LifecycleEventObserver {

    private val _onItemSelected = MutableLiveEvent<UUID>()
    val onItemSelected = _onItemSelected.share()

    private val _noteListLiveData = MutableLiveEvent<List<Note>>()
    val noteListLD = _noteListLiveData.share()

    private fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            if (noteListLD.value != noteRepository.getNotes()) {
                _noteListLiveData.postValue(Event(noteRepository.getNotes()))
            }
        }
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.addNote(note)
    }

    override fun onNoteDelete(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(id)
        }
    }

    override fun onNoteSelected(noteId: UUID) {
        _onItemSelected.value = Event(noteId)
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                updateList()
            }
            Lifecycle.Event.ON_RESUME -> {
            }
            else -> return
        }
    }

}