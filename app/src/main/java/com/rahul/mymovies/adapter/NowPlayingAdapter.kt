package com.rahul.mymovies.adapter

import android.content.Context
import android.database.Cursor
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
    Class that uses the cursor to generate the large list of now playing items
 */
class NowPlayingAdapter(val context: Context, private val itemClick: (Movie) -> Unit) : RecyclerView.Adapter<NowPlayingAdapter.MovieListViewHolder>() {
    private var lastLoadedPosition = -1
    private var mList =  ArrayList<Movie>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_list_large, parent, false)
        return MovieListViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: NowPlayingAdapter.MovieListViewHolder, position: Int) {
        if (position < itemCount) {
            holder.bind(context, mList[position])
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
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList(){
        mList.clear()
        notifyDataSetChanged()
    }
}