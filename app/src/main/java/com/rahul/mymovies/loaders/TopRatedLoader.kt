package com.rahul.mymovies.loaders

import android.content.Context
import android.support.v4.app.LoaderManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import com.rahul.mymovies.Injection
import com.rahul.mymovies.adapter.DatabaseGridAdapter
import com.rahul.mymovies.adapter.DatabaseListAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.data.MoviesContract.COLUMN_PAGE_NO
import com.rahul.mymovies.loaders.TopRatedLoader.Companion.ID_TOP_RATED_LOADER
import com.rahul.mymovies.models.Movie
import com.rahul.mymovies.networkutils.NetworkCallHelper

class TopRatedLoader(val context: Context, val mode: Int) : LoaderManager.LoaderCallbacks<Cursor> {

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

        const val ID_TOP_RATED_LOADER = 1201
        var statusOfRequest = 0

        class UpdateTopRatedDatabaseTask(private val topRatedLoader: TopRatedLoader) : AsyncTask<Int, Unit, Unit>() {
            override fun doInBackground(vararg params: Int?) {
                statusOfRequest = NetworkCallHelper.addTopMoviesFromJson(topRatedLoader.context, topRatedLoader.page)
            }

            override fun onPostExecute(result: Unit?) {
                if (statusOfRequest == 1) {
                    topRatedLoader.onCreateLoader(ID_TOP_RATED_LOADER, null)
                } else if (topRatedLoader.page == 1) {
                    if(topRatedLoader.mode == MODE_GRID){
                        topRatedLoader.mDatabaseGridAdapter!!.notifyDataSetChanged()
                    }else{
                        topRatedLoader.mDatabaseListAdapter!!.notifyDataSetChanged()
                    }
                    topRatedLoader.isFirst = true
                    topRatedLoader.isLoading = false
                } else {
                    topRatedLoader.page = topRatedLoader.page - 1
                    topRatedLoader.isFirst = true
                    topRatedLoader.isLoading = false
                }
                super.onPostExecute(result)
            }
        }

        class UpdateTopRatedDataset(private val topRatedLoader: TopRatedLoader, private val mCursor: Cursor) : AsyncTask<Unit, Unit, Unit>(){
            private val topRatedList = ArrayList<Movie>()
            override fun doInBackground(vararg params: Unit?) {
                val count = mCursor.count
                for ( i in 0 until count){
                    mCursor.moveToPosition(i)
                    val movieId = mCursor.getInt(MoviesContract.INDEX_COLUMN_MOVIE_ID_KEY)
                    val title = mCursor.getString(MoviesContract.INDEX_COLUMN_TITLE)
                    val overview = mCursor.getString(MoviesContract.INDEX_COLUMN_OVERVIEW)
                    val averageVote = mCursor.getDouble(MoviesContract.INDEX_COLUMN_AVERAGE_VOTE)
                    val releaseDate = mCursor.getString(MoviesContract.INDEX_COLUMN_RELEASE_DATE)
                    val imagePath = mCursor.getString(MoviesContract.INDEX_COLUMN_POSTER_PATH)
                    val backdropPath = mCursor.getString(MoviesContract.INDEX_COLUMN_BACKDROP_PATH)

                    val movie = Movie(movieId, title, overview, imagePath, backdropPath, releaseDate, averageVote)
                    topRatedList.add(movie)
                }
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                if(topRatedLoader.mode == TopRatedLoader.MODE_GRID){
                    topRatedLoader.mDatabaseGridAdapter!!.addList(topRatedList)
                }else {
                    topRatedLoader.mDatabaseListAdapter!!.addList(topRatedList)
                }

            }
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                val maxPage = page.toString()

                val topRatedQueryUri = MoviesContract.TopRatedEntry.CONTENT_URI

                var startQuery = "(${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'en'"
                //Make selection query handle extra cases
                if (Injection.isBollywoodEnabled) {
                    startQuery = "$startQuery OR ${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'hi'"
                }
                if(Injection.isAnimeEnabled) {
                    startQuery = "$startQuery OR ${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'ja' "
                }

                startQuery = "$startQuery)"

                Log.v("Max PAge", maxPage)
                return CursorLoader(context!!,
                        topRatedQueryUri,
                        MoviesContract.getNormalColumns(),
                        "$startQuery AND ${MoviesContract.COLUMN_PAGE_NO} = ? ",
                        arrayOf(maxPage),
                        MoviesContract.COLUMN_PAGE_NO + " ASC")

        }


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        Log.d("data", (data == null).toString())
        var empty = true
        if (data != null && data.moveToFirst()) {
            empty = data.getInt(0) == 0
        }
        //If we are working with a list of views
        if (mode == MostPopularLoader.MODE_LIST) {
            if (empty && isFirst) {
                UpdateTopRatedDatabaseTask(this).execute()
                isFirst = false
            } else if(!empty){
                UpdateTopRatedDataset(this, data!!).execute()
                Log.v("Now size", mDatabaseListAdapter!!.itemCount.toString())
                isLoading = false
                isFirst = true
            }
        }

        //If we are working with a grid of views
        else {
            if (empty && isFirst) {
                UpdateTopRatedDatabaseTask(this).execute()
                isFirst = false
            } else if(!empty){
                UpdateTopRatedDataset(this, data!!).execute()
                Log.v("Now size", mDatabaseGridAdapter!!.itemCount.toString())
                isLoading = false
                isFirst = true
            }
        }


    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (mode == MostPopularLoader.MODE_LIST) {
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