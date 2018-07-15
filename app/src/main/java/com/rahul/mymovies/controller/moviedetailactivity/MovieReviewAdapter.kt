package com.rahul.mymovies.controller.moviedetailactivity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rahul.mymovies.R


class MovieReviewAdapter(val context: Context) : RecyclerView.Adapter<MovieReviewAdapter.MovieReviewHolder>() {

    var reviewList = ArrayList<MovieReview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_review_card, parent, false)
        return MovieReviewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: MovieReviewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    fun swapList(list: ArrayList<MovieReview>){
        reviewList = list
        notifyDataSetChanged()
    }

    inner class MovieReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val content = itemView.findViewById<TextView>(R.id.review_text)
        private val author = itemView.findViewById<TextView>(R.id.review_author)

        fun bind(movieReview: MovieReview){
                val authorString = "- by ${movieReview.author}  "
                content.text = movieReview.content
                author.text = authorString
                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse(movieReview.url))
                    context.startActivity(intent)
                }
            }
    }
}