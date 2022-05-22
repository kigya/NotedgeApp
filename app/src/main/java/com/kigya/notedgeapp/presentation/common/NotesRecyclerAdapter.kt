package com.kigya.notedgeapp.presentation.common

import android.animation.ValueAnimator
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
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
    private var _selectedPosition = arrayListOf<Int>()
    private var _removing: Long? = null
    private var actionMode: ActionMode? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotesRecyclerAdapter.NoteHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteHolder(binding)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NotesRecyclerAdapter.NoteHolder, position: Int) {
        val note = notes[position]

        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.main)

        holder.bind(note)
    }


    inner class NoteHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var note: Note

        fun bind(note: Note) {
            this.note = note
            binding.noteItemTitle.text = this.note.title.trim()
            binding.noteItemDescription.text = this.note.noteText.trim()
            binding.noteItemDatetime.text = DateFormat.format(LIST_DATE_FORMAT, this.note.dateTime)

            binding.root.setOnClickListener { rootView ->
                when (actionMode) {
                    null -> {
                        onRootClick(rootView)
                        Log.i(this.javaClass.simpleName, "onBindView holderClick")
                    }
                    else -> {
                        rootView.apply {
                            onItemSelected(this@NoteHolder.bindingAdapterPosition)
                        }

                    }
                }
            }

            binding.root.setOnLongClickListener { rootView ->
                rootView.startActionMode(actionModelCallback(rootView))

                return@setOnLongClickListener true
            }

        }

        private fun actionModelCallback(view: View) = object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                val inflater: MenuInflater = mode.menuInflater
                mode.title = _selectedPosition.size.toString()
                inflater.inflate(R.menu.note_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_delete -> {

                        onDelete(view)
                        mode.finish() // Action picked, so close the CAB

                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                destroyActionMode()
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

        private fun onDelete(view: View) {
            enableCardViewColor(view)

            val animator = animateViewAlpha(view)

            val pos = notes.findIndexById(note.id)
            _removing = note.id

            notes.remove(note)
            actionListener.onNoteDelete(note.id)

            notifyItemRemoved(pos)

            animator?.start()
        }

        private fun onItemSelected(position: Int) {
            notes[position].isSelected = !notes[position].isSelected

            if (notes[position].isSelected) {
                _selectedPosition.add(position)
            } else {
                _selectedPosition.remove(position)
            }
            actionMode!!.title = _selectedPosition.size.toString()
            Log.i("selectedList", "$_selectedPosition")

            if (_selectedPosition.size == 0) {
                actionMode!!.finish()
            }
            notifyItemChanged(position)
        }

        private fun destroyActionMode() {
            if (_selectedPosition.size > 0) {
                for (position in _selectedPosition) {
                    notes[position].isSelected = false
                    notifyItemChanged(position)
                }
                _selectedPosition.clear()
            }
            actionMode = null
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

    override fun onItemRemoved(position: Int) = removeNote(position)

    private fun swapItems(from: Int, to: Int) {

        if (from < to) {
            for (i in from until to) {
                Collections.swap(notes, i, i + 1)
            }

            for (x in notes) {
                Log.d(TAG, "tit: ${x.title} - pos: ${notes.indexOf(x) + 1}")
            }
            Log.d(TAG, "\n")
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(notes, i, i - 1)
            }

            for (x in notes) {
                Log.d(TAG, "tit: ${x.title} - pos: ${notes.indexOf(x) + 1}")
            }
            Log.d(TAG, "\n")
        }

        notifyItemMoved(from, to)
    }

    private fun movedActionDone(from: Int, to: Int) {
        actionListener.onItemMoved(from.toLong(), to.toLong())
        Log.d(TAG, "from: $from, to: $to")
    }

    private fun removeNote(position: Int) {
        notifyItemRemoved(position)
        actionListener.onNoteDelete(notes[position].id)
        Log.d(TAG, "note id: ${notes[position].position}, item rec. pos. $position")
    }

    companion object {
        @JvmStatic
        private val TAG = "NoteRecyclerAdapter"
    }

}