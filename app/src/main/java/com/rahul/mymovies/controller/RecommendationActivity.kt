package com.rahul.mymovies.controller

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.rahul.mymovies.R
import com.rahul.mymovies.adapter.DatabaseGridAdapter
import com.rahul.mymovies.controller.Interfaces.EmptyRecyclerViewObserver
import com.rahul.mymovies.controller.moviedetailactivity.MovieDetailActivity
import com.rahul.mymovies.models.Movie
import com.rahul.mymovies.networkutils.JSONParserUtils
import com.rahul.mymovies.networkutils.NetworkCallUtils
import kotlinx.android.synthetic.main.activity_recommendation.*
import org.json.JSONObject

class RecommendationActivity : AppCompatActivity() {

    private lateinit var movie: Movie
    private lateinit var databaseListAdapter: DatabaseGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        movie = intent.getParcelableExtra<Movie>("movie")

        initializeSelectedMovie()
        initalizeSimilarMovies()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeSelectedMovie() {
        Glide.with(this).load(movie.imagePath).into(moviePoster)
        movieTitle.text = movie.title
    }

    private fun initalizeSimilarMovies() {
        val layoutManager = GridLayoutManager(this, 2)
        similarMoviesGridView.layoutManager = layoutManager

        databaseListAdapter = DatabaseGridAdapter(this){ movie, _->
            val intentDetailActivity = Intent(this, MovieDetailActivity::class.java)

            intentDetailActivity.putExtra("movie", movie)
            val optionsCompat = ActivityOptions.makeSceneTransitionAnimation(this)
            startActivity(intentDetailActivity, optionsCompat.toBundle())
        }

        databaseListAdapter.registerAdapterDataObserver(EmptyRecyclerViewObserver(similarMoviesGridView, similarMoviesEmptyView, similarMoviesProgressBar))
        similarMoviesGridView.adapter = databaseListAdapter

        loadRecommendedMovies()
    }

    fun loadRecommendedMovies(){
        val urlRecommendations = NetworkCallUtils.getUrlForRecommendedMovies(movie.movieId)
        AndroidNetworking.get(urlRecommendations)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        if(response != null){
                            databaseListAdapter.swapList(JSONParserUtils.returnMoviesArrayFromResultsJson(response))
                            databaseListAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        databaseListAdapter.notifyDataSetChanged()
                    }

                })
    }
}
