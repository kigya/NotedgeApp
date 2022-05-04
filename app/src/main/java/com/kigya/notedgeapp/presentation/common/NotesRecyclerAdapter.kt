package com.kigya.notedgeapp.presentation.common

import android.animation.Animator
import android.animation.ValueAnimator
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.databinding.NoteItemBinding
import com.kigya.notedgeapp.utils.extensions.findIndexById
import com.kigya.notedgeapp.utils.constants.Constants.LIST_DATE_FORMAT
import java.util.*

interface NoteActionListener {
    fun onNoteDelete(id: UUID)
    fun onNoteSelected(noteId: UUID)
}

class NotesRecyclerAdapter(private val actionListener: NoteActionListener) :
    RecyclerView.Adapter<NotesRecyclerAdapter.NoteHolder>() {

    var notes: MutableList<Note> = mutableListOf()
        set(newValue) {
            val diffCallback = NotesDiffCallback(field, newValue)
            val diffUtilResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffUtilResult.dispatchUpdatesTo(this)
        }

    private var _removing: UUID? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotesRecyclerAdapter.NoteHolder {
        val binding: NoteItemBinding =
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteHolder(binding)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NotesRecyclerAdapter.NoteHolder, position: Int) {
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.main)
        val note = notes[position]
        holder.bind(note)
    }

    inner class NoteHolder(binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root),
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
            noteTitleTextView.text = this.note.title.trim()
            noteDescriptionTextView.text = this.note.noteText.trim()
            noteDateTextView.text =
                DateFormat.format(LIST_DATE_FORMAT, this.note.dateTime)
        }

        private fun enableCardViewColor(v: View) {
            itemView.findViewById<CardView>(R.id.cardView).setCardBackgroundColor(
                ContextCompat.getColor(v.context!!, R.color.viridian_green)
            )
        }

        override fun onClick(v: View) {
            if (_removing == null || note.id != _removing) {
                actionListener.onNoteSelected(note.id)
            }
        }

        override fun onLongClick(view: View?): Boolean {
            view?.let {
                enableCardViewColor(it)
            }
            val animator = animateViewAlpha(view)
            addAnimatorListener(animator)
            animator?.start()
            return true
        }

        private fun addAnimatorListener(animator: ValueAnimator?) {
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    _removing = note.id
                }

                override fun onAnimationEnd(p0: Animator?) {
                    actionListener.onNoteDelete(note.id)
                    val item = notes.findIndexById(note.id)
                    notes.remove(note)
                    notifyItemRemoved(item)
                }

                override fun onAnimationCancel(p0: Animator?) = Unit
                override fun onAnimationRepeat(p0: Animator?) = Unit
            })
        }

        private fun animateViewAlpha(view: View?): ValueAnimator? {
            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = 250
            animator.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Float
                view?.alpha = animatedValue
            }
            return animator
        }
    }
}