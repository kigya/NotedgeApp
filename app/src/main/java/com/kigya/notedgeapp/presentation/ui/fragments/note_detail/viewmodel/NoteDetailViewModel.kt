package com.kigya.notedgeapp.presentation.ui.fragments.note_detail.viewmodel

import androidx.lifecycle.*
import com.kigya.notedgeapp.data.model.Event
import com.kigya.notedgeapp.data.model.MutableLiveEvent
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.share
import com.kigya.notedgeapp.domain.usecase.AddNoteUseCase
import com.kigya.notedgeapp.domain.usecase.DeleteNoteUseCase
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.EventsNotificationContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias checkIfEmptyAction = () -> Unit

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleEventObserver {

    private val _noteLiveData = savedStateHandle.getLiveData<Note>(SSH_NOTE)
    val noteLiveData = _noteLiveData.share()

    private val _newNote = savedStateHandle.getLiveData<Boolean>(NEW_NOTE)

    private val _notificationLD = MutableLiveEvent<EventsNotificationContract>()
    val notificationLD = _notificationLD.share()

    private val _popBackstack = MutableLiveEvent<Unit>()
    val popBackstack = _popBackstack.share()

    fun loadNote(note: Note) {
        if (noteLiveData.value != note) {
            _noteLiveData.value = note
        }
    }

    private fun checkIfEmpty(note: Note, delete: checkIfEmptyAction, update: checkIfEmptyAction = {}) {
        when {
            note.noteText.isBlank() && note.title.isBlank() -> {
                delete.invoke()
            }
            else -> {
                update.invoke()
            }
        }
        popBackStack()
    }

    fun onBackpressedToolbar(note: Note) {
        checkIfEmpty(note, delete = { deleteNote(note.id) })
    }

    fun onBackpressed(note: Note) {
        checkIfEmpty(note, delete = { deleteNote(note.id) }, update = { update(note) })
    }

    fun onDonePressed(note: Note) {
        checkIfEmpty(note, delete = { deleteNote(note.id) }, update = { update(note) })
    }

    fun noteStatus(status: Boolean) {
        _newNote.value = status
    }

    private fun update(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            addNoteUseCase(note)
        }
        _notificationLD.postValue(Event(EventsNotificationContract.SAVED))
    }

    private fun popBackStack() {
        _popBackstack.postValue(Event(Unit))
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase(id)
        }
        if (_newNote.value != true) {
            _notificationLD.postValue(Event(EventsNotificationContract.DELETED))
        }
    }

    private fun ssh() {
        if (!savedStateHandle.contains(SSH_NOTE)) {
            savedStateHandle.set(SSH_NOTE, noteLiveData.value)
        }
        if (!savedStateHandle.contains(NEW_NOTE)) {
            savedStateHandle.set(NEW_NOTE, _newNote.value)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                ssh()
            }
            else -> {}
        }
    }

    companion object {
        @JvmStatic
        private val TAG = "NoteDetailVM"

        @JvmStatic
        private val SSH_NOTE = "SSH_NOTE"

        @JvmStatic
        private val NEW_NOTE = "NEW_NOTE"
    }

}