package com.rahul.mymovies.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.rahul.mymovies.R
import com.rahul.mymovies.models.MovieVideo

class MovieVideosAdapter(val context : Context) : RecyclerView.Adapter<MovieVideosAdapter.MovieVideoViewHolder>() {
    var movieList = ArrayList<MovieVideo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVideoViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_video_card, parent, false)
        return MovieVideoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieVideoViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    fun swapList(list: ArrayList<MovieVideo>){
        movieList = list
        notifyDataSetChanged()
    }

    inner class MovieVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val thumbNail = itemView.findViewById<ImageView>(R.id.movie_video_thumbnail)

        fun bind(movieVideo : MovieVideo){
            if(movieVideo.isVideoYoutube()){
                Glide.with(context)
                        .load("https://img.youtube.com/vi/${movieVideo.key}/mqdefault.jpg")
                        .transition(withCrossFade())
                        .into(thumbNail)

                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=${movieVideo.key}"))
                    context.startActivity(intent)
                }
            }
        }
    }
}