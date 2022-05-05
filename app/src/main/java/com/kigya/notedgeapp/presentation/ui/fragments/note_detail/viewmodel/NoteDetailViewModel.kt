package com.kigya.notedgeapp.presentation.ui.fragments.note_detail.viewmodel

import androidx.lifecycle.*
import com.kigya.notedgeapp.data.model.Event
import com.kigya.notedgeapp.data.model.MutableLiveEvent
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.share
import com.kigya.notedgeapp.domain.repository.NoteRepository
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.EventsNotificationContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel(){

    private val noteIdLiveData = MutableLiveData<UUID>()
    private val _noteLiveData = MutableLiveEvent<Note?>()
    val noteLiveData = _noteLiveData.share()

    private val _savedNotificationLD = MutableLiveEvent<EventsNotificationContract>()
    val savedNotificationLD = _savedNotificationLD.share()

    init {
        if (noteLiveData.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val id = noteIdLiveData.value
                if (id != null) {
                    _noteLiveData.postValue(Event(noteRepository.getNote(id)))
                }
            }
        }
    }

    fun loadNote(noteId: UUID) {
        noteIdLiveData.value = noteId
    }

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(note)
        }
        _savedNotificationLD.value = Event(EventsNotificationContract.POSITIVE)
    }

    // todo delete implementation
    fun deleteNote(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.deleteNote(id)
    }

}