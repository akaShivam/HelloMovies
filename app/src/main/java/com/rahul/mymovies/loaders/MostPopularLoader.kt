package com.rahul.mymovies.loaders

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import com.rahul.mymovies.adapter.DatabaseGridAdapter
import com.rahul.mymovies.adapter.DatabaseListAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.networkutils.NetworkCallHelper

class MostPopularLoader(val context: Context, val mode: Int): LoaderManager.LoaderCallbacks<Cursor>{

    var page = 1
    private var isFirst = false
    var isLoading = true

    private var mDatabaseListAdapter: DatabaseListAdapter? = null
    private var mDatabaseGridAdapter: DatabaseGridAdapter? = null

    constructor(context: Context, databaseListAdapter: DatabaseListAdapter, mode: Int): this(context, mode){
        mDatabaseListAdapter = databaseListAdapter
    }

    constructor(context: Context, databaseGridAdapter: DatabaseGridAdapter, mode: Int) : this(context, mode){
        mDatabaseGridAdapter = databaseGridAdapter
    }


    companion object {
        const val MODE_LIST = 1
        const val MODE_GRID = 2

        const val ID_MOST_POPULAR_LOADER = 1202
        var statusOfRequest = 0
        class UpdateTopRatedDatabaseTask(private val topRatedLoader: MostPopularLoader) : AsyncTask<Int, Unit, Unit>() {
            override fun doInBackground(vararg params: Int?) {
                statusOfRequest = NetworkCallHelper.addMostPopularFromJson(topRatedLoader.context!!, topRatedLoader.page)
            }

            override fun onPostExecute(result: Unit?) {
                if(statusOfRequest == 1){
                    topRatedLoader.onCreateLoader(ID_MOST_POPULAR_LOADER, null)
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
            ID_MOST_POPULAR_LOADER -> {
                val maxPage = page.toString()

                val mostPopularQueryUri = MoviesContract.MostPopularEntry.CONTENT_URI
                Log.v("Max PAge", maxPage)
                return CursorLoader(context,
                        mostPopularQueryUri,
                        MoviesContract.MostPopularEntry.getColumns(),
                        "${MoviesContract.MostPopularEntry.COLUMN_ORIGINAL_LANGUAGE} = ? AND ${MoviesContract.MostPopularEntry.COLUMN_PAGE_NO} <= ? ",
                        arrayOf("en", maxPage),
                        MoviesContract.MostPopularEntry.COLUMN_PAGE_NO + " ASC")
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
            if(mode == MODE_LIST){
                Log.v("Swap Check", "${mDatabaseListAdapter!!.itemCount} and ${data?.count}")
                if(mDatabaseListAdapter!!.itemCount == data?.count && isFirst){
                    UpdateTopRatedDatabaseTask(this).execute()
                    isFirst = false
                }else if(mDatabaseListAdapter!!.itemCount < data!!.count){
                    Log.v("Swapping", "I am swapping")
                    mDatabaseListAdapter!!.swapCursor(data)
                    Log.v("Now size", mDatabaseListAdapter!!.itemCount.toString())
                    isLoading = false
                }else{
                    isLoading = false
                }
            }

            else{
                Log.v("Swap Check", "${mDatabaseGridAdapter!!.itemCount} and ${data?.count}")
                if(mDatabaseGridAdapter!!.itemCount == data?.count && isFirst){
                    UpdateTopRatedDatabaseTask(this).execute()
                    isFirst = false
                }else{
                    Log.v("Swapping", "I am swapping")
                    mDatabaseGridAdapter!!.swapCursor(data)
                    Log.v("Now size", mDatabaseGridAdapter!!.itemCount.toString())
                    isLoading = false
                }
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if(mode == MODE_LIST){
            mDatabaseListAdapter!!.swapCursor(null)
            mDatabaseListAdapter!!.notifyDataSetChanged()
        }
        else {
            mDatabaseGridAdapter!!.swapCursor(null)
            mDatabaseGridAdapter!!.notifyDataSetChanged()
        }
    }

    fun updatePage(){
        page = page + 1
        isFirst = true
    }
}