package com.rahul.mymovies.controller

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
import com.rahul.mymovies.loaders.TopRatedLoader.Companion.ID_TOP_RATED_LOADER
import com.rahul.mymovies.adapter.TopRatedAdapter
import com.rahul.mymovies.loaders.TopRatedLoader
import android.util.Pair as UtilPair
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivityFragment : Fragment(){


    private var mContext: Context? = null
    private lateinit var topRatedAdapter: TopRatedAdapter

    lateinit var topLoader: TopRatedLoader


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = context

       initializeAllViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(ID_TOP_RATED_LOADER, null, topLoader)
    }


    private fun initializeAllViews(){
        initializeTopRatedMovies()
    }

    private fun initializeTopRatedMovies(){
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        topRatedRecyclerView.layoutManager = layoutManager
        topRatedRecyclerView.itemAnimator = DefaultItemAnimator()

        topRatedAdapter = TopRatedAdapter(mContext!!){movie, itemView->
            val intentDetailActivity = Intent(mContext, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)

            val posterTransitionPair = UtilPair.create(itemView.findViewById<View>(R.id.moviePosterImage),resources.getString(R.string.poster_image_transition))
            val titleTransitionPair = UtilPair.create(itemView.findViewById<View>(R.id.movieTitleTextView), resources.getString(R.string.title_transition_name))

            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity as Activity, posterTransitionPair,titleTransitionPair)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }

        topRatedRecyclerView.adapter = topRatedAdapter

        topLoader = TopRatedLoader(mContext, topRatedAdapter)
        topRatedRecyclerView.addOnScrollListener(object : EndlessScrollListener(TopRatedLoader.ID_TOP_RATED_LOADER, loaderManager, topLoader, layoutManager){})
    }
}
