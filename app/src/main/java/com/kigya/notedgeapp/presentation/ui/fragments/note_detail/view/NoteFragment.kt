package com.kigya.notedgeapp.presentation.ui.fragments.note_detail.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.observeEvent
import com.kigya.notedgeapp.databinding.FragmentCreateNoteBinding
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.EventsNotificationContract
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.viewmodel.NoteDetailViewModel
import com.kigya.notedgeapp.utils.constants.Constants.ARG_NOTE_ID
import com.kigya.notedgeapp.utils.constants.Constants.NOTE_DATE_FORMAT
import com.kigya.notedgeapp.utils.extensions.notifier
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val noteDetailViewModel by viewModels<NoteDetailViewModel>()

    private lateinit var note: Note

    private val opPauseExecute = { noteDetailViewModel.saveNote(note) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = Note()
        val noteId: UUID = arguments?.getSerializable(ARG_NOTE_ID) as UUID
        noteDetailViewModel.loadNote(noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.noteDatetime.apply { text = note.dateTime.toString() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeNote()
        initializeListeners()
        initializeObservers()
    }

    private fun initializeNote() {
        val noteId = arguments?.getSerializable(ARG_NOTE_ID) as UUID
        noteDetailViewModel.loadNote(noteId)

        val titleWatcher = titleTextWatcher()
        binding.noteTitle.addTextChangedListener(titleWatcher)

        val noteTextWatcher = noteContentTextWatcher()
        binding.noteDescription.addTextChangedListener(noteTextWatcher)
    }

    private fun initializeListeners() {
        binding.imgDone.setOnClickListener {
            noteDetailViewModel.saveNote(note)
            parentFragmentManager.popBackStack()
        }

        binding.imgBack.setOnClickListener {
            noteDetailViewModel.saveNote(note)
            parentFragmentManager.popBackStack()
        }
    }

    private fun initializeObservers() {
        noteDetailViewModel.noteLiveData.observeEvent(viewLifecycleOwner) { note ->
            note?.let {
                this.note = note
                updateUI()
            }
        }
        noteDetailViewModel.savedNotificationLD.observeEvent(viewLifecycleOwner){ event ->
            when(event){
                EventsNotificationContract.POSITIVE -> {notifier().showSnackbar(getString(R.string.note_saved))}
                else ->{}
            }
            Log.d(TAG, "savedNotificationLD Event")
        }
    }

    override fun onPause() {
        super.onPause()
        opPauseExecute.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI() {
        binding.noteTitle.setText(note.title.trim())
        binding.noteDescription.setText(note.noteText.trim())
        binding.noteDatetime.text = DateFormat.format(NOTE_DATE_FORMAT, note.dateTime)
    }

    private fun titleTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(
            sequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            note.title = s.toString()
        }

        override fun afterTextChanged(sequence: Editable?) = Unit
    }

    private fun noteContentTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(
            sequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            note.noteText = s.toString()
        }

        override fun afterTextChanged(sequence: Editable?) = Unit
    }

    companion object {

        @JvmStatic
        private val TAG = "NoteFragment"

        fun newInstance(noteId: UUID): NoteFragment {
            val args = Bundle().apply {
                putSerializable(ARG_NOTE_ID, noteId)
            }
            return NoteFragment().apply {
                arguments = args
            }
        }
    }

}