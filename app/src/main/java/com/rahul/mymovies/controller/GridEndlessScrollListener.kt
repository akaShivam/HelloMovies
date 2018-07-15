package com.rahul.mymovies.controller

import android.support.v4.app.LoaderManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.rahul.mymovies.loaders.MostPopularLoader
import com.rahul.mymovies.loaders.TopRatedLoader

abstract class GridEndlessScrollListener(val loaderId: Int, val loaderManager: LoaderManager) : RecyclerView.OnScrollListener() {

    var topRatedLoader : TopRatedLoader? = null
    var mostPopularLoader: MostPopularLoader? = null

    lateinit var mLayoutManager: GridLayoutManager


    constructor(loaderId: Int, loaderManager: LoaderManager,
                loader: TopRatedLoader, layoutManager: GridLayoutManager) : this(loaderId, loaderManager){
        topRatedLoader = loader
        mLayoutManager = layoutManager
    }

    constructor(loaderId: Int, loaderManager: LoaderManager,
                loader: MostPopularLoader, layoutManager: GridLayoutManager) : this(loaderId, loaderManager){
        mostPopularLoader = loader
        mLayoutManager = layoutManager
    }



    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if(dy > 0)
        {
            when(loaderId){
                TopRatedLoader.ID_TOP_RATED_LOADER->{
                    var visibleItemCount = mLayoutManager.childCount
                    var totalItemCount = mLayoutManager.itemCount
                    var pastVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                    if(!topRatedLoader!!.isLoading){
                        if(visibleItemCount + pastVisibleItem >= totalItemCount - 20)
                        {
                            topRatedLoader!!.isLoading = true
                            topRatedLoader!!.updatePage()
                            loaderManager.restartLoader(TopRatedLoader.ID_TOP_RATED_LOADER, null, topRatedLoader!!)
                        }
                    }
                }

                MostPopularLoader.ID_MOST_POPULAR_LOADER->{
                    var visibleItemCount = mLayoutManager.childCount
                    var totalItemCount = mLayoutManager.itemCount
                    var pastVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                    if(!mostPopularLoader!!.isLoading){
                        if(visibleItemCount + pastVisibleItem >= totalItemCount - 20)
                        {
                            mostPopularLoader!!.isLoading = true
                            mostPopularLoader!!.updatePage()
                            loaderManager.restartLoader(MostPopularLoader.ID_MOST_POPULAR_LOADER, null, mostPopularLoader!!)
                        }
                    }
                }
            }

        }
    }
}