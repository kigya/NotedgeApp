package com.kigya.notedgeapp.presentation.common

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class NoteTouchHelper(private val adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {

    private var from = 0
    private var to = 0

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag = if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
            UP or DOWN or LEFT or RIGHT
        } else {
            UP or DOWN or START or END
        }
        return makeMovementFlags(dragFlag, 0)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when (actionState) {
            ACTION_STATE_DRAG -> {
                this.from = viewHolder?.bindingAdapterPosition ?: 0
            }
            ACTION_STATE_IDLE -> {
                adapter.onItemMoved(from + 1, to + 1)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        this.to = target.bindingAdapterPosition
        Log.d("helpI", "$to")
        adapter.onItemMoves(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

}