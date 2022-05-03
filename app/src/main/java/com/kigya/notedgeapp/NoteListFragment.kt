package com.kigya.notedgeapp

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.transition.Slide
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.kigya.notedgeapp.databinding.FragmentHomeBinding
import com.kigya.notedgeapp.viewModel.NotesListViewModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.itemAnimator = SlideInLeftAnimator(OvershootInterpolator(1f))
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
        View.OnClickListener, View.OnLongClickListener {

        lateinit var note: Note

        private val noteTitleTextView: TextView = itemView.findViewById(R.id.note_item_title)
        private val noteDescriptionTextView: TextView =
            itemView.findViewById(R.id.note_item_description)
        private val noteDateTextView: TextView = itemView.findViewById(R.id.note_item_datetime)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
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

        private fun enableCardViewColor(v: View) {
            itemView.findViewById<CardView>(R.id.cardView).setCardBackgroundColor(
                ContextCompat.getColor(v.context!!, R.color.viridian_green)
            )
        }

        private fun disableCardViewColor(v: View) {
            itemView.findViewById<CardView>(R.id.cardView).setCardBackgroundColor(
                ContextCompat.getColor(v.context!!, R.color.bunker)
            )
        }

        override fun onLongClick(v: View?): Boolean {
            v?.let { enableCardViewColor(it) }
            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = 500
            animator.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Float
                v?.alpha = animatedValue
            }
            animator.start()
            Executors.newSingleThreadScheduledExecutor().schedule({
                noteListViewModel.deleteNote(note.id)
            }, 300, TimeUnit.MILLISECONDS)

            return true
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
            holder.itemView.animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.main)
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