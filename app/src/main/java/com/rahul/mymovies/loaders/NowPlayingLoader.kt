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
import com.rahul.mymovies.adapter.NowPlayingAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.loaders.MostPopularLoader.Companion.ID_MOST_POPULAR_LOADER
import com.rahul.mymovies.loaders.NowPlayingLoader.Companion.ID_NOW_PLAYING_LOADER
import com.rahul.mymovies.models.Movie
import com.rahul.mymovies.networkutils.NetworkCallHelper

class NowPlayingLoader(val context: Context, private val nowPlayingAdapter: NowPlayingAdapter) : LoaderManager.LoaderCallbacks<Cursor> {

    var page = 1
    private var isFirst = false
    private var isLoading = true


    companion object {
        const val ID_NOW_PLAYING_LOADER = 1205
        var statusOfRequest = 0

        class UpdateNowPlayingDatabaseTask(private val nowPlayingLoader: NowPlayingLoader) : AsyncTask<Int, Unit, Unit>() {
            override fun doInBackground(vararg params: Int?) {
                statusOfRequest = NetworkCallHelper.addNowPlayingFromJson(nowPlayingLoader.context, nowPlayingLoader.page)
            }

            override fun onPostExecute(result: Unit?) {
                if (statusOfRequest == 1) {
                    nowPlayingLoader.onCreateLoader(ID_NOW_PLAYING_LOADER, null)
                } else {
                    nowPlayingLoader.nowPlayingAdapter.notifyDataSetChanged()
                    nowPlayingLoader.isFirst = true
                    nowPlayingLoader.isLoading = false
                }
            }
        }

        class UpdateNowPlayingDataSet(private val nowPlayingLoader: NowPlayingLoader, private val mCursor: Cursor) : AsyncTask<Unit, Unit, Unit>() {
            private val arrayList = ArrayList<Movie>()

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


                nowPlayingLoader.nowPlayingAdapter.addList(arrayList)
            }
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                val maxPage = page.toString()

                var startQuery = "(${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'en'"
                //Make selection query handle extra cases
                if (Injection.isBollywoodEnabled) {
                    startQuery = "$startQuery OR ${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'hi' "
                }

                if (Injection.isAnimeEnabled) {
                    startQuery = "$startQuery OR ${MoviesContract.COLUMN_ORIGINAL_LANGUAGE} = 'ja' "
                }

                startQuery = "$startQuery)"

                val nowPlayingQueryUri = MoviesContract.NowPlayingEntry.CONTENT_URI
                Log.v("Max PAge", maxPage)

                return CursorLoader(context,
                        nowPlayingQueryUri,
                        MoviesContract.getNormalColumns(),
                        startQuery,
                        null,
                        MoviesContract.COLUMN_PAGE_NO + " ASC")


    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        Log.d("data", (data == null).toString())
        var empty = true
        if (data != null && data.moveToFirst()) {
            empty = data.getInt(0) == 0
        }
        if (empty) {
            UpdateNowPlayingDatabaseTask(this).execute(1)
        } else if (nowPlayingAdapter.itemCount < data!!.count) {
            Log.v("Swapping", "I am swapping")
            UpdateNowPlayingDataSet(this, data).execute()
            Log.v("Now size", nowPlayingAdapter.itemCount.toString())
            isLoading = false
        } else {
            isLoading = false
        }


    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        nowPlayingAdapter.clearList()
    }
}