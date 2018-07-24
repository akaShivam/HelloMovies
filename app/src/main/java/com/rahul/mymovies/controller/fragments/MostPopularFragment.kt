package com.rahul.mymovies.controller.fragments

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rahul.mymovies.R
import com.rahul.mymovies.adapter.DatabaseGridAdapter
import com.rahul.mymovies.controller.Interfaces.GridEndlessScrollListener
import com.rahul.mymovies.controller.Interfaces.OnFragmentInteractionListener
import com.rahul.mymovies.loaders.MostPopularLoader
import com.rahul.mymovies.controller.moviedetailactivity.MovieDetailActivity
import kotlinx.android.synthetic.main.fragment_most_popular.*

class MostPopularFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private var mContext: Context? = null

    private lateinit var databaseListAdapter: DatabaseGridAdapter
    lateinit var popularLoader: MostPopularLoader


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (listener != null) {
            listener!!.setTitleTo("Popular Movies")
        }
        return inflater.inflate(R.layout.fragment_most_popular, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context
        initalizeMostPopular()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loader = loaderManager.getLoader<MostPopularLoader>(MostPopularLoader.ID_MOST_POPULAR_LOADER)
        if(loader != null && loader.isStarted){
            popularLoader = loader as MostPopularLoader
        }else{
            loaderManager.initLoader(MostPopularLoader.ID_MOST_POPULAR_LOADER, null, popularLoader)
        }

    }

    private fun initalizeMostPopular() {
        val layoutManager = GridLayoutManager(mContext, 2)
        mostPopularGridView.layoutManager = layoutManager
        mostPopularGridView.itemAnimator = DefaultItemAnimator()

        databaseListAdapter = DatabaseGridAdapter(mContext!!){ movie, _->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)
            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }

        mostPopularGridView.hasFixedSize()
        mostPopularGridView.adapter = databaseListAdapter
        popularLoader = MostPopularLoader(mContext!!, databaseListAdapter, MostPopularLoader.MODE_GRID)
        mostPopularGridView.addOnScrollListener(object : GridEndlessScrollListener(MostPopularLoader.ID_MOST_POPULAR_LOADER, loaderManager, popularLoader, layoutManager){})
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


}
