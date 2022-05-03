package com.kigya.notedgeapp.presentation.ui.note_detail.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.databinding.FragmentCreateNoteBinding
import com.kigya.notedgeapp.presentation.ui.note_detail.viewmodel.NoteDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val ARG_NOTE_ID = "crime_id"

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val noteDetailViewModel by viewModels<NoteDetailViewModel>()

    private lateinit var note: Note

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

        binding.noteDatetime.apply {
            text = note.dateTime.toString()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = arguments?.getSerializable(ARG_NOTE_ID) as UUID
        noteDetailViewModel.loadNote(noteId)
        noteDetailViewModel.noteLiveData.observe(
            viewLifecycleOwner
        ) { note ->
            note?.let {
                this.note = note
                updateUI()
            }
        }

        binding.imgDone.setOnClickListener {
            noteDetailViewModel.saveNote(note)

        }
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
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

        val noteTextWatcher = object : TextWatcher {
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

        binding.noteTitle.addTextChangedListener(titleWatcher)
        binding.noteDescription.addTextChangedListener(noteTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        noteDetailViewModel.saveNote(note)
    }

    private fun updateUI() {
        binding.noteTitle.setText(note.title.trim())
        binding.noteDescription.setText(note.noteText.trim())
        binding.noteDatetime.text = DateFormat.format("dd/M/yyyy hh:mm:ss", note.dateTime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

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