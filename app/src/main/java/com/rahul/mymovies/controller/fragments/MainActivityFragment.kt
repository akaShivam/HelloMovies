package com.rahul.mymovies.controller.fragments

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rahul.mymovies.R
import com.rahul.mymovies.adapter.DatabaseListAdapter
import com.rahul.mymovies.adapter.NowPlayingAdapter
import com.rahul.mymovies.controller.Interfaces.EmptyRecyclerViewObserver
import com.rahul.mymovies.controller.Interfaces.ListEndlessScrollListener
import com.rahul.mymovies.controller.Interfaces.OnFragmentInteractionListener
import com.rahul.mymovies.loaders.MostPopularLoader
import com.rahul.mymovies.loaders.NowPlayingLoader
import com.rahul.mymovies.loaders.TopRatedLoader
import com.rahul.mymovies.controller.moviedetailactivity.MovieDetailActivity
import android.util.Pair as UtilPair
import kotlinx.android.synthetic.main.fragment_movies_home.*

class MainActivityFragment : Fragment(){

    private var listener: OnFragmentInteractionListener? = null

    private var mContext: Context? = null

    private lateinit var topRatedAdapter: DatabaseListAdapter
    lateinit var topLoader: TopRatedLoader

    private lateinit var databaseListAdapter: DatabaseListAdapter
    lateinit var popularLoader: MostPopularLoader

    private lateinit var nowPlayingAdapter: NowPlayingAdapter
    lateinit var nowPlayingLoader: NowPlayingLoader


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (listener != null) {
            listener!!.setTitleTo("Home")
        }
        return inflater.inflate(R.layout.fragment_movies_home, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context
       initializeAllViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(NowPlayingLoader.ID_NOW_PLAYING_LOADER, null, nowPlayingLoader)
        loaderManager.initLoader(MostPopularLoader.ID_MOST_POPULAR_LOADER, null, popularLoader)
        loaderManager.initLoader(TopRatedLoader.ID_TOP_RATED_LOADER, null, topLoader)
    }


    private fun initializeAllViews(){
        initalizeNowPlaying()
        initalizeMostPopular()
        initializeTopRatedMovies()
    }

    private fun initalizeNowPlaying() {
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        nowPlayingRecyclerView.layoutManager = layoutManager
        nowPlayingRecyclerView.itemAnimator = DefaultItemAnimator()

        nowPlayingAdapter = NowPlayingAdapter(mContext!!){movie->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)
            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }
        nowPlayingAdapter.registerAdapterDataObserver(EmptyRecyclerViewObserver(nowPlayingRecyclerView, nowPlayingErrorView, nowPlayingProgressBar))
        nowPlayingRecyclerView.adapter = nowPlayingAdapter

        nowPlayingLoader = NowPlayingLoader(mContext!!, nowPlayingAdapter)
    }

    private fun initalizeMostPopular() {
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mostPopularRecyclerView.layoutManager = layoutManager
        mostPopularRecyclerView.itemAnimator = DefaultItemAnimator()

        databaseListAdapter = DatabaseListAdapter(mContext){ movie->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)
            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }

        databaseListAdapter.registerAdapterDataObserver(EmptyRecyclerViewObserver(mostPopularRecyclerView, mostPopularErrorView, mostPopularProgressBar))
        mostPopularRecyclerView.adapter = databaseListAdapter

        popularLoader = MostPopularLoader(mContext!!, databaseListAdapter, MostPopularLoader.MODE_LIST)
        mostPopularRecyclerView.addOnScrollListener(object : ListEndlessScrollListener(MostPopularLoader.ID_MOST_POPULAR_LOADER, loaderManager, popularLoader, layoutManager){})
    }

    private fun initializeTopRatedMovies(){

        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        topRatedRecyclerView.layoutManager = layoutManager
        topRatedRecyclerView.itemAnimator = DefaultItemAnimator()

        topRatedAdapter = DatabaseListAdapter(mContext!!){movie->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)

            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }

        topRatedAdapter.registerAdapterDataObserver(EmptyRecyclerViewObserver(topRatedRecyclerView, topRatedErrorView, topRatedProgressBar))
        topRatedRecyclerView.adapter = topRatedAdapter

        topLoader = TopRatedLoader(mContext!!, topRatedAdapter, TopRatedLoader.MODE_LIST)
        topRatedRecyclerView.addOnScrollListener(object : ListEndlessScrollListener(TopRatedLoader.ID_TOP_RATED_LOADER, loaderManager, topLoader, layoutManager){})
    }

}
