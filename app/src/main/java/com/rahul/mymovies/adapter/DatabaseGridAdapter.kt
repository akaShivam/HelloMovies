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


class DatabaseGridAdapter(val context: Context, private val itemClick: (Movie, View) -> Unit) : RecyclerView.Adapter<DatabaseGridAdapter.MovieListViewHolder>() {
    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_grid_view, parent, false)
        return MovieListViewHolder(itemView, itemClick)
    }

    override fun getItemCount(): Int {
        if (mCursor != null) {
            return mCursor!!.count
        }
        return 0
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        if (mCursor != null && !mCursor!!.isClosed && position < itemCount) {
            mCursor!!.moveToPosition(position)
            holder.bind(context, mCursor)
        }
    }
    inner class MovieListViewHolder(itemView: View, private val itemClick: (Movie, View) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.movieTitleTextView)
        private val posterView = itemView.findViewById<ImageView>(R.id.moviePosterImage)


        fun bind(context: Context, mCursor: Cursor?) {
            val movieId = mCursor!!.getInt(MoviesContract.TopRatedEntry.INDEX_COLUMN_MOVIE_ID_KEY)
            val title = mCursor.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_TITLE)
            val overview = mCursor.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_OVERVIEW)
            val averageVote = mCursor.getDouble(MoviesContract.TopRatedEntry.INDEX_COLUMN_AVERAGE_VOTE)
            val releaseDate = mCursor.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_RELEASE_DATE)
            val imagePath = mCursor.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_POSTER_PATH)
            val backdropPath = mCursor.getString(MoviesContract.TopRatedEntry.INDEX_COLUMN_BACKDROP_PATH)

            val movie = Movie(movieId, title, overview, imagePath, backdropPath, releaseDate, averageVote)
            titleView.text = movie.title
            Glide.with(context).load(movie.imagePath)
                    .into(posterView)

            itemView.setOnClickListener { itemClick(movie, itemView) }
        }
    }

    fun swapCursor(cursor: Cursor?) {
        if(cursor == null || mCursor == null){
            mCursor = cursor
            notifyDataSetChanged()
        }else{
            mCursor = cursor
            val startIndex = mCursor!!.count
            val endIndex = startIndex + cursor.count - 1
            notifyItemRangeInserted(startIndex, endIndex)
        }



    }
}