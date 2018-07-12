package com.rahul.mymovies.loaders

import android.content.Context
import android.support.v4.app.LoaderManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import android.widget.Toast
import com.rahul.mymovies.adapter.TopRatedAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.loaders.TopRatedLoader.Companion.ID_TOP_RATED_LOADER
import com.rahul.mymovies.networkutils.NetworkCallHelper

class TopRatedLoader(val context: Context?, private val topRatedAdapter: TopRatedAdapter) : LoaderManager.LoaderCallbacks<Cursor>{

    var page = 1
    private var isFirst = false
    var isLoading = true


    companion object {
        const val ID_TOP_RATED_LOADER = 1201
        var statusOfRequest = 0
        class UpdateTopRatedDatabaseTask(private val topRatedLoader: TopRatedLoader) : AsyncTask<Int, Unit, Unit>() {
            override fun doInBackground(vararg params: Int?) {
                statusOfRequest = NetworkCallHelper.addTopMoviesFromJson(topRatedLoader.context!!, topRatedLoader.page)
            }

            override fun onPostExecute(result: Unit?) {
                if(statusOfRequest == 1){
                    topRatedLoader.onCreateLoader(ID_TOP_RATED_LOADER, null)
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
            ID_TOP_RATED_LOADER -> {
                val maxPage = page.toString()

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
            UpdateTopRatedDatabaseTask(this).execute(1)
        }
        else{
            Log.v("Swap Check", "${topRatedAdapter.itemCount} and ${data?.count}")
            if(topRatedAdapter.itemCount == data?.count && isFirst){
                UpdateTopRatedDatabaseTask(this).execute()
                isFirst = false
            }else if(topRatedAdapter.itemCount < data!!.count){
                Log.v("Swapping", "I am swapping")
                val startIndex = topRatedAdapter.itemCount
                val endIndex = startIndex + data.count - 1
                topRatedAdapter.swapCursor(data)
                topRatedAdapter.notifyItemRangeChanged(startIndex, endIndex)
                Log.v("Now size", topRatedAdapter.itemCount.toString())
                isLoading = false
            }else{
                isLoading = false
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        topRatedAdapter.swapCursor(null)
        topRatedAdapter.notifyDataSetChanged()
    }

    fun updatePage(){
        page = page + 1
        isFirst = true
    }
}