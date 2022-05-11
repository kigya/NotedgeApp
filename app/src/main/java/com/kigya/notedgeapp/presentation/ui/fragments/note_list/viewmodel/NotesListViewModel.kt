package com.kigya.notedgeapp.presentation.ui.fragments.note_list.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.kigya.notedgeapp.data.model.Event
import com.kigya.notedgeapp.data.model.MutableLiveEvent
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.share
import com.kigya.notedgeapp.domain.usecase.DeleteNoteUseCase
import com.kigya.notedgeapp.domain.usecase.GetNotesUseCase
import com.kigya.notedgeapp.domain.usecase.SearchUseCase
import com.kigya.notedgeapp.presentation.common.NoteActionListener
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.EventsNotificationContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val searchUseCase: SearchUseCase,
) : ViewModel(), NoteActionListener, LifecycleEventObserver {

    private val _onItemSelected = MutableLiveEvent<Note>()
    val onItemSelected = _onItemSelected.share()

    private val _notificationLD = MutableLiveEvent<EventsNotificationContract>()
    val notificationLD = _notificationLD.share()

    fun noteListLiveData() : LiveData<List<Note>> = getNotesUseCase().asLiveData()

    fun search(request: String?): LiveData<List<Note>>{
        return searchUseCase(request).asLiveData()
    }

    override fun onNoteDelete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNoteUseCase(id)
        }
        _notificationLD.postValue(Event(EventsNotificationContract.DELETED))
    }

    override fun onNoteSelected(note: Note) {
        _onItemSelected.value = Event(note)
    }

    override fun onItemMoved(from: Long, to: Long) {
        Log.d(TAG, "position changed")
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {

            Lifecycle.Event.ON_RESUME -> {
            }
            else -> return
        }
    }

    companion object{
        @JvmStatic
        private val TAG = "NoteListVM"
    }

}