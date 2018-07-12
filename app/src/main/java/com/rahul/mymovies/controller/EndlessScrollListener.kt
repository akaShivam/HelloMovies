package com.rahul.mymovies.controller

import android.support.v4.app.LoaderManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.rahul.mymovies.loaders.TopRatedLoader

abstract class EndlessScrollListener(val loaderId: Int, val loaderManager: LoaderManager) : RecyclerView.OnScrollListener() {

    lateinit var topRatedLoader : TopRatedLoader
    lateinit var mLayoutManager: LinearLayoutManager

    var totalLoadedPosition : Int = 0

    constructor(loaderId: Int, loaderManager: LoaderManager,
                loader: TopRatedLoader, layoutManager: LinearLayoutManager) : this(loaderId, loaderManager){
        topRatedLoader = loader
        mLayoutManager = layoutManager
    }
    fun initializeTopLoader(loader: TopRatedLoader, layoutManager: LinearLayoutManager){
        topRatedLoader = loader
        mLayoutManager = layoutManager
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if(dx > 0)
        {
            when(loaderId){
                TopRatedLoader.ID_TOP_RATED_LOADER->{
                    var visibleItemCount = mLayoutManager.childCount
                    var totalItemCount = mLayoutManager.itemCount
                    var pastVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                    if(!topRatedLoader.isLoading){
                        if(visibleItemCount + pastVisibleItem >= totalItemCount - 5)
                        {
                            topRatedLoader.isLoading = true
                            topRatedLoader.updatePage()
                            loaderManager.restartLoader(TopRatedLoader.ID_TOP_RATED_LOADER, null, topRatedLoader)
                        }
                    }
                }
            }

            if(dx < 0){
                when(loaderId) {
                    TopRatedLoader.ID_TOP_RATED_LOADER -> {
                        val visibleItemCount = mLayoutManager.childCount
                        val totalItemCount = mLayoutManager.itemCount
                        val pastVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                        if (!topRatedLoader.isLoading) {
                            if (visibleItemCount + pastVisibleItem >= totalItemCount - 5) {
                                topRatedLoader.isLoading = true
                                loaderManager.restartLoader(TopRatedLoader.ID_TOP_RATED_LOADER, null, topRatedLoader)
                            }
                        }
                    }
                }
            }

        }
    }
}