package com.kigya.notedgeapp.presentation.ui.fragments.note_list.viewmodel

import androidx.lifecycle.*
import com.kigya.notedgeapp.data.model.Event
import com.kigya.notedgeapp.data.model.MutableLiveEvent
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.share
import com.kigya.notedgeapp.domain.usecase.AddNoteUseCase
import com.kigya.notedgeapp.domain.usecase.DeleteNoteUseCase
import com.kigya.notedgeapp.domain.usecase.GetNotesUseCase
import com.kigya.notedgeapp.domain.usecase.SearchUseCase
import com.kigya.notedgeapp.presentation.common.NoteActionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val searchUseCase: SearchUseCase,
) : ViewModel(), NoteActionListener, LifecycleEventObserver {

    private val _onItemSelected = MutableLiveEvent<UUID>()
    val onItemSelected = _onItemSelected.share()

    fun noteListLiveData() : LiveData<List<Note>> = getNotesUseCase().asLiveData()

    fun search(request: String?): LiveData<List<Note>>{
        return searchUseCase(request).asLiveData()
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        addNoteUseCase(note)
    }

    override fun onNoteDelete(id: UUID) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase(id)
        }
    }

    override fun onNoteSelected(noteId: UUID) {
        _onItemSelected.value = Event(noteId)
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {

            Lifecycle.Event.ON_RESUME -> {
            }
            else -> return
        }
    }

}