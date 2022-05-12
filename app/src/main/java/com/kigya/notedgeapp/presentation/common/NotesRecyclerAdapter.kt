package com.kigya.notedgeapp.presentation.common

import android.animation.Animator
import android.animation.ValueAnimator
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kigya.notedgeapp.R
import com.kigya.notedgeapp.data.model.Note
import com.kigya.notedgeapp.databinding.NoteItemBinding
import com.kigya.notedgeapp.utils.constants.Constants.LIST_DATE_FORMAT
import com.kigya.notedgeapp.utils.extensions.findIndexById


interface NoteActionListener {
    fun onNoteDelete(id: Long)
    fun onNoteSelected(note: Note)
    fun onItemMoved(from: Long, to: Long)
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

    private var _removing: Long? = null

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
                    val pos = notes.findIndexById(note.id)
                    //notes.remove(note)
                    actionListener.onNoteDelete(note.id)
                    Log.d(TAG, "note id: ${note.position}, item rec. pos. $pos")
                    notifyItemRemoved(pos)
                }

                override fun onAnimationCancel(p0: Animator?) = run { animator.cancel() }
                override fun onAnimationRepeat(p0: Animator?) = run { }
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

    override fun onItemMoves(from: Int, to: Int) = swapItems(from, to)

    override fun onItemMoved(from: Int, to: Int) = movedActionDone(from, to)


    private fun swapItems(from: Int, to: Int) {
        notifyItemMoved(from,to)
            /*if (from < to) {
                for (i in from until to) {
                    Collections.swap(notes, i, i + 1)
                }
            } else {
                for (i in from downTo to + 1) {
                    Collections.swap(notes, i, i - 1)
                }
            }
            notifyItemMoved(from, to)*/
    }

    private fun movedActionDone(from: Int, to: Int) {
        actionListener.onItemMoved(from.toLong(), to.toLong())
        Log.d(TAG, "from: $from, to: $to")
    }

    companion object {
        @JvmStatic
        private val TAG = "NoteRecyclerAdapter"
    }

}