package com.kigya.notedgeapp

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kigya.notedgeapp.databinding.FragmentHomeBinding
import com.kigya.notedgeapp.viewModel.NotesListViewModel
import java.util.*

private const val TAG = "NoteListFragment"

class NoteListFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: NoteAdapter? = NoteAdapter(emptyList())

    private val noteListViewModel: NotesListViewModel by lazy {
        ViewModelProvider(this).get(NotesListViewModel::class.java)
    }

    interface Callbacks {
        fun onNoteSelected(noteId: UUID)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as? Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createNoteButton.setOnClickListener {
            val note = Note()
            noteListViewModel.addNote(note)
            callbacks?.onNoteSelected(note.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        noteListViewModel.noteListLiveData.observe(
            viewLifecycleOwner,
            Observer { notes ->
                notes?.let {
                    Log.i(TAG, "Got noteLiveData ${notes.size}")
                    updateUI(notes)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(notes: List<Note>) {
        adapter?.let {
            it.notes = notes
        } ?: run {
            adapter = NoteAdapter(notes)
        }
        binding.recyclerView.adapter = adapter
    }

    private inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var note: Note

        private val noteTitleTextView: TextView = itemView.findViewById(R.id.note_item_title)
        private val noteDescriptionTextView: TextView = itemView.findViewById(R.id.note_item_description)
        private val noteDateTextView: TextView = itemView.findViewById(R.id.note_item_datetime)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(note: Note) {
            this.note = note
            noteTitleTextView.text = this.note.title
            noteDescriptionTextView.text = this.note.noteText
            noteDateTextView.text =
                DateFormat.format("EEEE, MMM dd, yyyy", this.note.dateTime)
        }

        override fun onClick(v: View) {
            callbacks?.onNoteSelected(note.id)
        }
    }

    private inner class NoteAdapter(var notes: List<Note>) :
        RecyclerView.Adapter<NoteHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : NoteHolder {
            val view = layoutInflater.inflate(R.layout.note_item, parent, false)
            return NoteHolder(view)
        }

        override fun getItemCount() = notes.size

        override fun onBindViewHolder(holder: NoteHolder, position: Int) {
            val note = notes[position]
            holder.bind(note)
        }
    }

    companion object {
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }

}