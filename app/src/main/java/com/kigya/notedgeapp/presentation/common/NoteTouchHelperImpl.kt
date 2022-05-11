package com.kigya.notedgeapp.presentation.common

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class NoteTouchHelper(private val adapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {

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

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition

        adapter.onItemMoved(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }


}