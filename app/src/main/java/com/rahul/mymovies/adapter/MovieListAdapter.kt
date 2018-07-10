package com.rahul.mymovies.adapter

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rahul.mymovies.R
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.models.Movie


class MovieListAdapter(val context: Context, val itemClick: (Cursor) -> Unit) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder{
        val itemView = LayoutInflater.from(context).inflate(R.layout.top_rated_card, parent, false)
        return MovieListViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        if(mCursor != null){
            return mCursor!!.count
        }
        return 0
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        if(mCursor != null && !mCursor!!.isClosed) {
            mCursor!!.moveToPosition(position)
            holder.bind(context, mCursor)
        }
    }

    inner class MovieListViewHolder(itemView: View, val itemClick: (Cursor) -> Unit): RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.movieTitleTextView)
        val posterView = itemView.findViewById<ImageView>(R.id.moviePosterImage)

        fun bind(context: Context, mCursor: Cursor?){
            titleView.text = mCursor?.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_TITLE)
            Log.v("Name", mCursor?.getInt(MoviesContract.TopRatedEntry.INDEX_COLUMN_PAGE_NO).toString())
            Glide.with(context).load(mCursor!!.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_POSTER_PATH))
                    .into(posterView)

            itemView.setOnClickListener{itemClick(mCursor)}
        }
    }

    fun swapCursor(cursor : Cursor?){
        mCursor = cursor
        notifyDataSetChanged()
    }
}