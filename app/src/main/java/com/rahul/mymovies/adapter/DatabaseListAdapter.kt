package com.rahul.mymovies.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rahul.mymovies.R
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.models.Movie

/*
    Class that uses a cursor to generate the layout to be displayed in the list
 */
class DatabaseListAdapter(val context: Context?, private val itemClick: (Movie) -> Unit) : RecyclerView.Adapter<DatabaseListAdapter.MovieListViewHolder>() {
    private var lastLoadedPosition = -1

    private var mGridList =  ArrayList<Movie>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_list_small, parent, false)
        return MovieListViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        return mGridList.size
    }


    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        if (position < itemCount) {
            holder.bind(context!!, mGridList[position])
            startAddAnimation(holder.itemView, position)
        }
    }

    override fun onViewDetachedFromWindow(holder: MovieListViewHolder) {
        holder.clearAnimation()
    }

    private fun startAddAnimation(viewToAnimate: View, position: Int) {
        if (position > lastLoadedPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right)
            viewToAnimate.startAnimation(animation)
            lastLoadedPosition = position
        }
    }


    inner class MovieListViewHolder(itemView: View, private val itemClick: (Movie) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.movieTitleTextView)
        private val posterView = itemView.findViewById<ImageView>(R.id.moviePosterImage)


        fun bind(context: Context, movie: Movie) {
            titleView.text = movie.title
            Glide.with(context).load(movie.imagePath)
                    .into(posterView)


            itemView.setOnClickListener { itemClick(movie) }
        }

        fun clearAnimation() {
            itemView.clearAnimation()
        }
    }


    fun addList(list: ArrayList<Movie>){
        if(lastLoadedPosition == -1){
            clearList()
        }
        if(itemCount == 0){
            val startIndex = itemCount
            mGridList.addAll(list)
            val endIndex = itemCount
            notifyItemRangeInserted(startIndex, endIndex-1)
        }
        else if(mGridList[itemCount - 1].movieId != list[list.size - 1].movieId){
            val startIndex = itemCount
            mGridList.addAll(list)
            val endIndex = itemCount
            notifyItemRangeInserted(startIndex, endIndex-1)
        }

    }


    fun clearList(){
        mGridList.clear()
        notifyDataSetChanged()
    }

}