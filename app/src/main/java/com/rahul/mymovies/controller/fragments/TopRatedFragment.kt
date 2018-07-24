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
import com.rahul.mymovies.controller.Interfaces.EmptyRecyclerViewObserver
import com.rahul.mymovies.controller.Interfaces.GridEndlessScrollListener
import com.rahul.mymovies.controller.Interfaces.OnFragmentInteractionListener
import com.rahul.mymovies.loaders.TopRatedLoader
import com.rahul.mymovies.controller.moviedetailactivity.MovieDetailActivity
import kotlinx.android.synthetic.main.fragment_top_rated.*


class TopRatedFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private var mContext: Context? = null

    private lateinit var databaseListAdapter: DatabaseGridAdapter
    lateinit var topRatedLoader: TopRatedLoader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (listener != null) {
            listener!!.setTitleTo("Top Rated Movies")
        }
        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context
        initalizeTopRated()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val loader = loaderManager.getLoader<TopRatedLoader>(TopRatedLoader.ID_TOP_RATED_LOADER)
        if(loader != null && loader.isStarted){
            topRatedLoader = loader as TopRatedLoader
        }else{
            loaderManager.initLoader(TopRatedLoader.ID_TOP_RATED_LOADER, null, topRatedLoader)
        }
    }

    private fun initalizeTopRated() {
        val layoutManager = GridLayoutManager(mContext, 2)
        topRatedGridView.layoutManager = layoutManager
        topRatedGridView.itemAnimator = DefaultItemAnimator()

        databaseListAdapter = DatabaseGridAdapter(mContext!!){ movie, _->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)
            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }

        databaseListAdapter.registerAdapterDataObserver(EmptyRecyclerViewObserver(topRatedGridView, cardError, progressBar))

        topRatedGridView.adapter = databaseListAdapter
        topRatedLoader = TopRatedLoader(mContext!!, databaseListAdapter, TopRatedLoader.MODE_GRID)
        topRatedGridView.addOnScrollListener(object : GridEndlessScrollListener(TopRatedLoader.ID_TOP_RATED_LOADER, loaderManager, topRatedLoader, layoutManager){})
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
