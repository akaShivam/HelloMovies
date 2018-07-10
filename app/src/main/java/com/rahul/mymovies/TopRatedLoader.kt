package com.rahul.mymovies

import android.content.Context
import android.support.v4.app.LoaderManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import com.rahul.mymovies.adapter.MovieListAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.networkutils.NetworkCallHelper

class TopRatedLoader(val context: Context?, val topRatedAdapter: MovieListAdapter) : LoaderManager.LoaderCallbacks<Cursor>{

    val ID_TOP_RATED_LOADER = 1201

    var page = 1
    var isFirst = true
    var loading = true

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when(id){
            ID_TOP_RATED_LOADER -> {
                var maxPage = page.toString()

                val topRatedQueryUri = MoviesContract.TopRatedEntry.CONTENT_URI
                Log.v("Max PAge", maxPage)
                return CursorLoader(context!!,
                        topRatedQueryUri,
                        MoviesContract.TopRatedEntry.getColumns(),
                        "${MoviesContract.TopRatedEntry.COLUMN_ORIGINAL_LANGUAGE} = ? AND ${MoviesContract.TopRatedEntry.COLUMN_PAGE_NO} <= ? ",
                        arrayOf("en", maxPage),
                        MoviesContract.TopRatedEntry.COLUMN_PAGE_NO + " ASC")
            }
            else->throw RuntimeException("loader not implemented $id")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        Log.d("data", (data == null).toString())
        var empty = true
        if(data!= null && data.moveToFirst()){
            empty = data.getInt(0) == 0
        }
        if(empty){
            UpdateTopRatedDatabaseTask().execute(1)
        }
        else{
            Log.v("Swap Check", "${topRatedAdapter.itemCount} and ${data?.count}")
            if(topRatedAdapter.itemCount == data?.count && isFirst){
                UpdateTopRatedDatabaseTask().execute()
                isFirst = false
            }else{
                Log.v("Swapping", "I am swapping")
                topRatedAdapter.swapCursor(data)
                Log.v("Now size", topRatedAdapter.itemCount.toString())
                isFirst = true
                loading = true
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        topRatedAdapter.swapCursor(null)
    }

    inner class UpdateTopRatedDatabaseTask : AsyncTask<Int, Unit, Unit>() {
        override fun doInBackground(vararg params: Int?) {
            NetworkCallHelper.addTopMoviesFromJson(context!!, page )
        }

        override fun onPostExecute(result: Unit?) {
            onCreateLoader(ID_TOP_RATED_LOADER, null)
            super.onPostExecute(result)
        }
    }
}