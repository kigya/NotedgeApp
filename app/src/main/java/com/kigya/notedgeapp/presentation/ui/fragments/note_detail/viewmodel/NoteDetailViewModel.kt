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
import java.util.*
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

    private val _notificationLD = MutableLiveEvent<EventsNotificationContract>()
    val notificationLD = _notificationLD.share()

    private val _popBackstack = MutableLiveEvent<Unit>()
    val popBackstack = _popBackstack.share()

    fun loadNote(note: Note) {
        if (noteLiveData.value != note) {
            _noteLiveData.value = note
        }
    }

    private fun checkIfEmpty(note: Note, action1: checkIfEmptyAction, action2: checkIfEmptyAction) {
        if (note.noteText.isBlank()
            && note.title.isBlank()
        ) {
            action1.invoke()
        } else {
            action2.invoke()
        }
        popBackStack()
    }

    fun onBackpressedToolbar(note: Note) {
        checkIfEmpty(note, { deleteNote(note.id) }, {})
    }

    fun onBackpressed(note: Note) {
        checkIfEmpty(note, { deleteNote(note.id) }, { update(note) })
    }

    fun onDonePressed(note: Note) {
        checkIfEmpty(note, { deleteNote(note.id) }, { update(note) })
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

    private fun deleteNote(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase(id)
        }
        _notificationLD.postValue(Event(EventsNotificationContract.DELETED))
    }

    companion object {
        @JvmStatic
        private val TAG = "NoteDetailVM"

        @JvmStatic
        private val SSH_NOTE = "SSH_NOTE"
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                if (!savedStateHandle.contains(SSH_NOTE)) {
                    savedStateHandle.set(SSH_NOTE, noteLiveData.value)
                }
            }
            else -> {}
        }
    }

}