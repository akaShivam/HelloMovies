package com.rahul.mymovies.controller.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rahul.mymovies.R
import com.rahul.mymovies.adapter.DatabaseGridAdapter
import com.rahul.mymovies.controller.Interfaces.EmptyRecyclerViewObserver
import com.rahul.mymovies.controller.Interfaces.OnFragmentInteractionListener
import com.rahul.mymovies.controller.moviedetailactivity.MovieDetailActivity
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.models.Movie
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_top_rated.*
import org.json.JSONObject

class FavoritesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mContext: Context
    private lateinit var databaseListAdapter: DatabaseGridAdapter

    private var observableMovieId = 0
    private var observablePositon = 0
    val favoritesList = ArrayList<Movie>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (listener != null) {
            listener!!.setTitleTo("Favorites")
        }
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context!!
        initalizeFavorites()
    }

    override fun onResume() {
        super.onResume()
        if(observableMovieId != 0){
            DatabaseAsyncTaskHelper().execute(0)
        }
    }

    private fun initalizeFavorites() {
        val layoutManager = GridLayoutManager(mContext, 2)
        favoritesGridView.layoutManager = layoutManager

        databaseListAdapter = DatabaseGridAdapter(mContext){ movie, position->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            observableMovieId = movie.movieId
            observablePositon = position

            intentDetailActivity.putExtra("movie", movie)
            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }
        databaseListAdapter.registerAdapterDataObserver(EmptyRecyclerViewObserver(favoritesGridView, favoritesEmptyView, favoritesProgressBar))
        favoritesGridView.adapter = databaseListAdapter

        DatabaseAsyncTaskHelper().execute(1)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }



    @SuppressLint("StaticFieldLeak")
    inner class DatabaseAsyncTaskHelper : AsyncTask<Int, Unit, Unit>() {
        private var isPresent = true
        private var mode = 1
        override fun doInBackground(vararg params: Int?) {
            val choice = params[0]
            Log.v("Choice", "$choice")

            when (choice) {
                0 -> {
                    mode = 0
                    val cursor = mContext.contentResolver.query(MoviesContract.FavoritesEntry.CONTENT_URI,
                            MoviesContract.FavoritesEntry.COLUMNS_FAVORITES,
                            "${MoviesContract.COLUMN_MOVIE_ID_KEY} = $observableMovieId",
                            null,
                            null)

                    isPresent = (cursor.count == 1)
                    Log.v("IS Favorite", "${cursor.count}")
                    cursor.close()
                }
                1 -> {
                    val mCursor = mContext.contentResolver.query(MoviesContract.FavoritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null)

                    val count = mCursor.count
                    for ( i in 0 until count){
                        mCursor.moveToPosition(i)
                        val movieId = mCursor.getInt(MoviesContract.INDEX_COLUMN_MOVIE_ID_KEY)
                        val title = mCursor.getString(MoviesContract.INDEX_COLUMN_TITLE)
                        val overview = mCursor.getString(MoviesContract.INDEX_COLUMN_OVERVIEW)
                        val averageVote = mCursor.getDouble(MoviesContract.INDEX_COLUMN_AVERAGE_VOTE)
                        val releaseDate = mCursor.getString(MoviesContract.INDEX_COLUMN_RELEASE_DATE)
                        val imagePath = mCursor.getString(MoviesContract.INDEX_COLUMN_POSTER_PATH)
                        val backdropPath = mCursor.getString(MoviesContract.FavoritesEntry.INDEX_COLUMN_BACKDROP_PATH)

                        val movie = Movie(movieId, title, overview, imagePath, backdropPath, releaseDate, averageVote)
                        favoritesList.add(movie)
                    }
                    databaseListAdapter.swapList(favoritesList)
                    mCursor.close()
                }
            }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            if(mode == 0 && !isPresent){
                favoritesList.removeAt(observablePositon)
                databaseListAdapter.swapList(favoritesList)
                databaseListAdapter.notifyItemRemoved(observablePositon)
                databaseListAdapter.notifyItemRangeChanged(observablePositon, databaseListAdapter.itemCount)
            }
            else if(mode == 1){
                databaseListAdapter.notifyDataSetChanged()
            }
        }
    }
}
