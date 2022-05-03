package com.kigya.notedgeapp.presentation.ui.note_detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val noteIdLiveData = MutableLiveData<UUID>()

    private val _noteLiveData = MutableLiveData<Note?>()
    val noteLiveData: LiveData<Note?> = _noteLiveData

    init {
        if (noteLiveData.value == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val id = noteIdLiveData.value
                if (id != null)
                    _noteLiveData.postValue(noteRepository.getNote(id))
            }
        }
    }

    fun loadNote(noteId: UUID) {
        noteIdLiveData.value = noteId
    }

    fun saveNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.updateNote(note)
    }

}