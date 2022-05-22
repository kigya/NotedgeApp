package com.kigya.notedgeapp.presentation.common

interface ItemTouchHelperAdapter {

    fun onItemMoves(from: Int, to: Int)

    fun onItemMoved(from: Int, to: Int)

    fun onItemRemoved(position: Int)

}