package com.kigya.notedgeapp.presentation.ui.fragments.note_detail.view

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.data.model.observeEvent
import com.kigya.notedgeapp.databinding.FragmentCreateNoteBinding
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.EventsNotificationContract
import com.kigya.notedgeapp.presentation.ui.fragments.note_detail.viewmodel.NoteDetailViewModel
import com.kigya.notedgeapp.utils.constants.Constants.ARG_NOTE
import com.kigya.notedgeapp.utils.constants.Constants.NOTE_DATE_FORMAT
import com.kigya.notedgeapp.utils.extensions.notifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val noteDetailViewModel by viewModels<NoteDetailViewModel>()

    private var note = Note()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = arguments?.getParcelable(ARG_NOTE) ?: Note()
        noteDetailViewModel.loadNote(note)
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
        binding.noteTitle.doOnTextChanged { s, _, _, _ ->
            note.title = s.toString()
        }
        binding.noteDescription.doOnTextChanged { s, _, _, _ ->
            note.noteText = s.toString()
            Log.d(TAG, s.toString())
        }
    }

    private fun initializeListeners() {
        binding.imgDone.setOnClickListener {
            noteDetailViewModel.saveNote(note)
        }

        binding.imgBack.setOnClickListener {
            noteDetailViewModel.saveNote(note)
            Log.d(TAG, note.title)
        }
    }

    private fun initializeObservers() {
        noteDetailViewModel.noteLiveData.observe(viewLifecycleOwner) { note ->
            note?.let {
                this.note = note
                updateUI()
            }
        }
        noteDetailViewModel.savedNotificationLD.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                EventsNotificationContract.POSITIVE -> {
                    notifier().showSnackbar(getString(R.string.note_saved))
                }
                else -> {}
            }
            Log.d(TAG, "savedNotificationLD Event")
        }
        noteDetailViewModel.popBackstack.observe(viewLifecycleOwner) {
            parentFragmentManager.popBackStack()
        }
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

    companion object {

        @JvmStatic
        private val TAG = "NoteFragment"

        @JvmStatic
        fun newInstance(note: Note) = NoteFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_NOTE, note)
            }
        }

    }
}