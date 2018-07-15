package com.rahul.mymovies.loaders

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import com.rahul.mymovies.adapter.NowPlayingAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.loaders.MostPopularLoader.Companion.ID_MOST_POPULAR_LOADER
import com.rahul.mymovies.networkutils.NetworkCallHelper

class NowPlayingLoader(val context: Context?, private val mostPopularAdapter: NowPlayingAdapter) : LoaderManager.LoaderCallbacks<Cursor>{

    var page = 1
    private var isFirst = false
    var isLoading = true


    companion object {
        const val ID_NOW_PLAYING_LOADER = 1205
        var statusOfRequest = 0
        class UpdateTopRatedDatabaseTask(private val topRatedLoader: NowPlayingLoader) : AsyncTask<Int, Unit, Unit>() {
            override fun doInBackground(vararg params: Int?) {
                statusOfRequest = NetworkCallHelper.addNowPlayingFromJson(topRatedLoader.context!!, topRatedLoader.page)
            }

            override fun onPostExecute(result: Unit?) {
                if(statusOfRequest == 1){
                    topRatedLoader.onCreateLoader(ID_NOW_PLAYING_LOADER, null)
                }else{

                    if(topRatedLoader.page != 1){
                        topRatedLoader.page = topRatedLoader.page - 1
                    }
                    topRatedLoader.isFirst = true
                    topRatedLoader.isLoading = false
                }
                super.onPostExecute(result)
            }
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        when(id){
            ID_NOW_PLAYING_LOADER -> {
                val maxPage = page.toString()

                val mostPopularQueryUri = MoviesContract.NowPlayingEntry.CONTENT_URI
                Log.v("Max PAge", maxPage)
                return CursorLoader(context!!,
                        mostPopularQueryUri,
                        MoviesContract.NowPlayingEntry.getColumns(),
                        "${MoviesContract.NowPlayingEntry.COLUMN_ORIGINAL_LANGUAGE} = ? AND ${MoviesContract.NowPlayingEntry.COLUMN_PAGE_NO} <= ? ",
                        arrayOf("en", maxPage),
                        MoviesContract.NowPlayingEntry.COLUMN_PAGE_NO + " ASC")
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
            UpdateTopRatedDatabaseTask(this).execute(1)
        }
        else if(mostPopularAdapter.itemCount < data!!.count){
                Log.v("Swapping", "I am swapping")
                mostPopularAdapter.swapCursor(data)
                Log.v("Now size", mostPopularAdapter.itemCount.toString())
                isLoading = false
            }else{
                isLoading = false
            }


    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mostPopularAdapter.swapCursor(null)
    }

    fun updatePage(){
        page = page + 1
        isFirst = true
    }
}