package com.rahul.mymovies.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rahul.mymovies.R
import com.rahul.mymovies.models.MovieCast


class MovieCastAdapter(val context: Context) : RecyclerView.Adapter<MovieCastAdapter.MovieCastViewHolder>() {

    var castList = ArrayList<MovieCast>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCastViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_cast_card, parent, false)
        return MovieCastViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: MovieCastViewHolder, position: Int) {
        holder.bind(castList[position])
    }

    fun swapList(list: ArrayList<MovieCast>){
        castList = list
        notifyDataSetChanged()
    }

    inner class MovieCastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbNail = itemView.findViewById<ImageView>(R.id.castImageView)
        private val castCharacter = itemView.findViewById<TextView>(R.id.castCharacterView)
        private val castName = itemView.findViewById<TextView>(R.id.castName)
        fun bind(movieCast: MovieCast) {

            Glide.with(context)
                    .load(movieCast.getPosterPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(thumbNail)

            castCharacter.text = movieCast.charcter
            castName.text = movieCast.name
        }
    }
}




