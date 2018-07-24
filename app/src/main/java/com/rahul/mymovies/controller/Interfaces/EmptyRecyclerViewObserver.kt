package com.rahul.mymovies.controller.Interfaces

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar

class EmptyRecyclerViewObserver(val rv: RecyclerView, val emptyView: View, val progressBar: ProgressBar) : RecyclerView.AdapterDataObserver() {

    fun checkEmpty(){
        if(rv.adapter.itemCount == 0){
            emptyView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            rv.visibility = View.GONE
        }
        else{
            emptyView.visibility = View.GONE
            progressBar.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }
    }

    override fun onChanged() {
        super.onChanged()
        checkEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkEmpty()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        checkEmpty()
    }
}