package com.kigya.notedgeapp.presentation.common

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
        return if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
            val dragFlag = UP or DOWN or LEFT or RIGHT
            val swipeFlag = 0
            makeMovementFlags(dragFlag, swipeFlag)
        } else {
            val dragFlag = UP or DOWN or START or END
            val swipeFlag = if (viewHolder.bindingAdapterPosition % 2 == 0) LEFT else RIGHT
            makeMovementFlags(dragFlag, swipeFlag)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ACTION_STATE_DRAG -> {
                this.from = viewHolder?.bindingAdapterPosition ?: 0
                this.to = viewHolder?.bindingAdapterPosition ?: 0
            }
            ACTION_STATE_IDLE -> {
                adapter.onItemMoved(from + 1, to + 1)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) return false

        to = target.bindingAdapterPosition
        adapter.onItemMoves(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemRemoved(viewHolder.bindingAdapterPosition)
    }

}