package com.rahul.mymovies.loaders

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import com.rahul.mymovies.Injection
import com.rahul.mymovies.adapter.DatabaseGridAdapter
import com.rahul.mymovies.adapter.DatabaseListAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.models.Movie
import com.rahul.mymovies.networkutils.NetworkCallHelper

class MostPopularLoader(val context: Context, private val mode: Int) : LoaderManager.LoaderCallbacks<Cursor> {

    var page = 1
    private var isFirst = true
    var isLoading = true

    private var mDatabaseListAdapter: DatabaseListAdapter? = null
    private var mDatabaseGridAdapter: DatabaseGridAdapter? = null

    constructor(context: Context, databaseListAdapter: DatabaseListAdapter, mode: Int) : this(context, mode) {
        mDatabaseListAdapter = databaseListAdapter
    }

    constructor(context: Context, databaseGridAdapter: DatabaseGridAdapter, mode: Int) : this(context, mode) {
        mDatabaseGridAdapter = databaseGridAdapter
    }


    companion object {
        const val MODE_LIST = 1
        const val MODE_GRID = 2

        const val ID_MOST_POPULAR_LOADER = 1202

        var statusOfRequest = 0

        class UpdateMostPopularDatabaseTask(private val mostPopularLoader: MostPopularLoader) : AsyncTask<Int, Unit, Unit>() {
            override fun doInBackground(vararg params: Int?) {
                statusOfRequest = NetworkCallHelper.addMostPopularFromJson(mostPopularLoader.context, mostPopularLoader.page)
            }

            override fun onPostExecute(result: Unit?) {
                when {
                    statusOfRequest == 1 -> mostPopularLoader.onCreateLoader(ID_MOST_POPULAR_LOADER, null)

                    mostPopularLoader.page == 1 -> {
                        if (mostPopularLoader.mode == MostPopularLoader.MODE_GRID) {
                            mostPopularLoader.mDatabaseGridAdapter!!.notifyDataSetChanged()
                        } else {
                            mostPopularLoader.mDatabaseListAdapter!!.notifyDataSetChanged()
                        }
                        mostPopularLoader.isFirst = true
                        mostPopularLoader.isLoading = false
                    }

                    else -> {
                        mostPopularLoader.page = mostPopularLoader.page - 1
                        mostPopularLoader.isFirst = true
                        mostPopularLoader.isLoading = false
                    }
                }
                super.onPostExecute(result)
            }
        }

        class UpdateMostPopularDataSet(private val mostPopularLoader: MostPopularLoader, private val mCursor: Cursor) : AsyncTask<Unit, Unit, Unit>() {
            val arrayList = ArrayList<Movie>()
            override fun doInBackground(vararg params: Unit?) {
                val count = mCursor.count
                for (i in 0 until count) {
                    mCursor.moveToPosition(i)
                    val movieId = mCursor.getInt(MoviesContract.INDEX_COLUMN_MOVIE_ID_KEY)
                    val title = mCursor.getString(MoviesContract.INDEX_COLUMN_TITLE)
                    val overview = mCursor.getString(MoviesContract.INDEX_COLUMN_OVERVIEW)
                    val averageVote = mCursor.getDouble(MoviesContract.INDEX_COLUMN_AVERAGE_VOTE)
                    val releaseDate = mCursor.getString(MoviesContract.INDEX_COLUMN_RELEASE_DATE)
                    val imagePath = mCursor.getString(MoviesContract.INDEX_COLUMN_POSTER_PATH)
                    val backdropPath = mCursor.getString(MoviesContract.INDEX_COLUMN_BACKDROP_PATH)

                    val movie = Movie(movieId, title, overview, imagePath, backdropPath, releaseDate, averageVote)
                    arrayList.add(movie)
                }
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                if (mostPopularLoader.mode == MODE_GRID) {
                    mostPopularLoader.mDatabaseGridAdapter!!.addList(arrayList)
                } else {
                    mostPopularLoader.mDatabaseListAdapter!!.addList(arrayList)
                }

            }
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val maxPage = page.toString()
        Log.v("Loader", "Most Popular Loader is now searching for page no $maxPage")

        //Get reference to the table for most popular
        val mostPopularQueryUri = MoviesContract.MostPopularEntry.CONTENT_URI

        var startQuery = "(${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'en'"
        //Make selection query handle extra cases
        if (Injection.isBollywoodEnabled) {
            startQuery = "$startQuery OR ${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'hi'"
        }
        if (Injection.isAnimeEnabled) {
            startQuery = "$startQuery OR ${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'ja' "
        }

        startQuery = "$startQuery)"

        return CursorLoader(context,
                mostPopularQueryUri,
                MoviesContract.getNormalColumns(),
                "$startQuery AND ${MoviesContract.COLUMN_PAGE_NO} = ? ",
                arrayOf(maxPage),
                MoviesContract.COLUMN_PAGE_NO + " ASC")

    }


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {

        var empty = true
        if (data != null && data.moveToFirst()) {
            empty = data.getInt(0) == 0
        }

        //If we are working with a list of views
        if (mode == MODE_LIST) {
            if (empty && isFirst) {
                UpdateMostPopularDatabaseTask(this).execute()
                isFirst = false
            } else if(!empty){
                UpdateMostPopularDataSet(this, data!!).execute()
                Log.v("Now size", mDatabaseListAdapter!!.itemCount.toString())
                isLoading = false
                isFirst = true
            }
        }

        //If we are working with a grid of views
        else {
            if (empty && isFirst) {
                UpdateMostPopularDatabaseTask(this).execute()
                isFirst = false
            } else if(!empty){
                UpdateMostPopularDataSet(this, data!!).execute()
                Log.v("Now size", mDatabaseGridAdapter!!.itemCount.toString())
                isLoading = false
            }
        }

    }


    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (mode == MODE_LIST) {
            mDatabaseListAdapter!!.clearList()

        } else {
            mDatabaseGridAdapter!!.clearList()
        }
    }

    fun updatePage() {
        page = page + 1
        isFirst = true
    }
}