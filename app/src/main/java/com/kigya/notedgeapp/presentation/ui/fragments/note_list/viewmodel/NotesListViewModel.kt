package com.kigya.notedgeapp.presentation.ui.note_list.viewmodel

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
    val noteListLiveData = _noteListLiveData.share()

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.addNote(note)
    }

    private fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            _noteListLiveData.postValue(Event(noteRepository.getNotes()))
        }
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
            Lifecycle.Event.ON_CREATE -> {
                if (noteListLiveData.value?.get()?.isEmpty() == true
                    || noteListLiveData.value == null
                ) {
                    updateList()
                }
            }
            Lifecycle.Event.ON_RESUME -> {
                updateList()
            }
            else -> return
        }
    }

}