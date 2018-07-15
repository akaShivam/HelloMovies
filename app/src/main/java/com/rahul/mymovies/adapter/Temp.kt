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
class Temp(val context: Context, private val itemClick: (Movie) -> Unit) : RecyclerView.Adapter<Temp.MovieGridViewHolder>() {

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieGridViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_grid_view, parent, false)
        return MovieGridViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        if (mCursor != null) {
            return mCursor!!.count
        }
        return 0
    }

    override fun onBindViewHolder(holder: MovieGridViewHolder, position: Int) {
        if (mCursor != null && !mCursor!!.isClosed && position < itemCount) {
            mCursor!!.moveToPosition(position)
            holder.bind(context, mCursor)
        }
    }

    inner class MovieGridViewHolder(itemView: View, private val itemClick: (Movie) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val titleView = itemView.findViewById<TextView>(R.id.movieTitleTextView)
        private val posterView = itemView.findViewById<ImageView>(R.id.moviePosterImage)


        fun bind(context: Context, mCursor: Cursor?) {

            val movieId = mCursor!!.getInt(MoviesContract.INDEX_COLUMN_MOVIE_ID_KEY)
            val title = mCursor.getString(MoviesContract.INDEX_COLUMN_TITLE)
            val overview = mCursor.getString(MoviesContract.INDEX_COLUMN_OVERVIEW)
            val averageVote = mCursor.getDouble(MoviesContract.INDEX_COLUMN_AVERAGE_VOTE)
            val releaseDate = mCursor.getString(MoviesContract.INDEX_COLUMN_RELEASE_DATE)
            val imagePath = mCursor.getString(MoviesContract.INDEX_COLUMN_POSTER_PATH)
            val backdropPath = mCursor.getString(MoviesContract.INDEX_COLUMN_BACKDROP_PATH)

            val movie = Movie(movieId, title, overview, imagePath, backdropPath, releaseDate, averageVote)
            titleView.text = movie.title
            Glide.with(context).load(movie.imagePath)
                    .into(posterView)

            itemView.setOnClickListener { itemClick(movie) }
        }
    }

    fun swapCursor(cursor: Cursor?) {
        if(cursor == null || mCursor == null){
            mCursor = cursor
            notifyDataSetChanged()
        }
        else if(cursor.count > itemCount){
            val startIndex = itemCount
            val endIndex =  cursor.count - 1
            mCursor = cursor
            notifyItemRangeInserted(startIndex, endIndex)
        }
    }


}