package com.rahul.mymovies.adapter

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rahul.mymovies.R
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.models.Movie


/*
    Class that uses a cursor to generate the layout to be displayed in the grid
 */
class DatabaseGridAdapter(val context: Context, private val itemClick: (Movie) -> Unit) : RecyclerView.Adapter<DatabaseGridAdapter.MovieGridViewHolder>() {

    private var mGridList =  ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_grid_view, parent, false)
        return MovieGridViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        return mGridList.size
    }

    override fun onBindViewHolder(holder: MovieGridViewHolder, position: Int) {
        if (position < itemCount) {
            holder.bind(context, mGridList[position])
        }
    }

    inner class MovieGridViewHolder(itemView: View, private val itemClick: (Movie) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val titleView = itemView.findViewById<TextView>(R.id.movieTitleTextView)
        private val posterView = itemView.findViewById<ImageView>(R.id.moviePosterImage)


        fun bind(context: Context, movie: Movie) {
            titleView.text = movie.title
            Glide.with(context).load(movie.imagePath)
                    .into(posterView)

            itemView.setOnClickListener { itemClick(movie) }
        }
    }

    fun addList(list: ArrayList<Movie>){
        val startIndex = itemCount
        mGridList.addAll(list)
        val endIndex = itemCount
        notifyItemRangeInserted(startIndex, endIndex-1)
    }

    fun clearList(){
        mGridList.clear()
        notifyDataSetChanged()
    }
}