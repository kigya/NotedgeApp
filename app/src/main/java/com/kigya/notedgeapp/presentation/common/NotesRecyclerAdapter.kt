package com.kigya.notedgeapp.presentation.common

import android.animation.Animator
import android.animation.ValueAnimator
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.databinding.NoteItemBinding
import com.kigya.notedgeapp.utils.constants.Constants.LIST_DATE_FORMAT
import com.kigya.notedgeapp.utils.extensions.findIndexById
import java.util.*

interface NoteActionListener {
    fun onNoteDelete(id: UUID)
    fun onNoteSelected(note: Note)
}

class NotesRecyclerAdapter(private val actionListener: NoteActionListener) :
    RecyclerView.Adapter<NotesRecyclerAdapter.NoteHolder>(), ItemTouchHelperAdapter {

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
        val binding =
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteHolder(binding)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NotesRecyclerAdapter.NoteHolder, position: Int) {
        val note = notes[position]

        holder.itemView.animation = AnimationUtils
            .loadAnimation(holder.itemView.context, R.anim.main)

        holder.bind(note)
    }

    inner class NoteHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var note: Note

        fun bind(note: Note) {
            this.note = note
            binding.dragItem.tag = note
            binding.noteItemTitle.text = this.note.title.trim()
            binding.noteItemDescription.text = this.note.noteText.trim()
            binding.noteItemDatetime.text = DateFormat.format(LIST_DATE_FORMAT, this.note.dateTime)

            binding.root.setOnClickListener {
                onRootClick(it)
            }
            binding.root.setOnLongClickListener {
                onRootLongClick(it)
                return@setOnLongClickListener true
            }
            binding.dragItem.setOnClickListener {

            }

        }

        private fun enableCardViewColor(v: View) {
            binding.cardView.setCardBackgroundColor(
                ContextCompat.getColor(v.context!!, R.color.viridian_green)
            )
        }

        private fun onRootClick(view: View?) {
            if (_removing == null || note.id != _removing) {
                actionListener.onNoteSelected(note)
            }
        }

        private fun onRootLongClick(view: View?) {
            view?.let {
                enableCardViewColor(it)
            }
            val animator = animateViewAlpha(view)
            addAnimatorListener(animator)
            animator?.start()
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
            animator.duration = 210
            animator.addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Float
                view?.alpha = animatedValue
            }
            return animator
        }

    }

    companion object {
        const val ID_REMOVE = 1

        @JvmStatic
        private val TAG = "NoteRecyclerAdapter"
    }

    override fun onMoveItem(from: Int, to: Int) {

    }

}