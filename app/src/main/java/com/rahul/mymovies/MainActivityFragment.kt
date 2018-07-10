package com.rahul.mymovies

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rahul.mymovies.adapter.MovieListAdapter
import com.rahul.mymovies.data.MoviesContract
import com.rahul.mymovies.data.MoviesProvider
import com.rahul.mymovies.networkutils.NetworkCallHelper
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>{

    val ID_TOP_RATED_LOADER = 1201
    var mContext: Context? = null
    lateinit var topRatedAdapter: MovieListAdapter
    var page = 1
    var loading = true
    var isFirst = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context

        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        topRetedRecyclerView.layoutManager = layoutManager
        topRetedRecyclerView.itemAnimator = DefaultItemAnimator()

        topRatedAdapter = MovieListAdapter(mContext!!){
        }

        topRetedRecyclerView.adapter = topRatedAdapter


        loaderManager.initLoader(ID_TOP_RATED_LOADER, null, this)

        topRetedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dx > 0)
                {
                    var visibleItemCount = layoutManager.childCount
                    var totalItemCount = layoutManager.itemCount
                    var pastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if(loading){
                        if(visibleItemCount + pastVisibleItem >= totalItemCount - 5)
                        {
                            loading = false
                            page = page + 1
                            loaderManager.restartLoader(ID_TOP_RATED_LOADER, null, this@MainActivityFragment)
                        }
                    }

                }
            }
        })

    }




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
            NetworkCallHelper.addTopMoviesFromJson(mContext!!, page )
        }

        override fun onPostExecute(result: Unit?) {
            loaderManager.restartLoader(ID_TOP_RATED_LOADER, null, this@MainActivityFragment)
            super.onPostExecute(result)
        }
    }
}
