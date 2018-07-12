package com.rahul.mymovies.controller

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.rahul.mymovies.R
import com.rahul.mymovies.adapter.MovieCastAdapter
import com.rahul.mymovies.adapter.MovieReviewAdapter
import com.rahul.mymovies.adapter.MovieVideosAdapter
import com.rahul.mymovies.models.Movie
import com.rahul.mymovies.models.MovieVideo
import com.rahul.mymovies.networkutils.JSONParserUtils
import com.rahul.mymovies.networkutils.NetworkCallUtils
import kotlinx.android.synthetic.main.content_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var movie: Movie
    private lateinit var movieListAdapter : MovieVideosAdapter
    var movieDetailsJson : JSONObject? = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        window.sharedElementReturnTransition = null
        window.sharedElementReenterTransition = null


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        movie = intent.getParcelableExtra<Movie>("movie")

        movieListAdapter = MovieVideosAdapter(this)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        movieVideoListRecyclerView.layoutManager = layoutManager
        movieVideoListRecyclerView.adapter = movieListAdapter

        val reviewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        movieReviewListRecyclerView.layoutManager = reviewLayoutManager
        movieReviewListRecyclerView.adapter = MovieReviewAdapter(this)

        val creditsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        movieCastListRecyclerView.layoutManager = creditsLayoutManager
        movieCastListRecyclerView.adapter = MovieCastAdapter(this)


        initToolBar()
        initialSetting()

        loadMovieDetailsList()
    }

    private fun loadMovieDetailsList() {
        val urlDetailsAndVideosList = NetworkCallUtils.getUrlForMovieDetailsAndVideos(movie.movieId)
        AndroidNetworking.get(urlDetailsAndVideosList)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        movieDetailsJson = response
                        loadMovieCastList()
                        Log.v("Response details", response.toString())
                        try{
                            var imdb = response?.getString("imdb_id")
                            Log.v("IMDB_ID", imdb)
                        }catch(e: JSONException){
                            Log.v("NO IMDB", "IMDB NOT FOUND IN RESPONSE")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.v("Error", "Unable to fetch data")
                    }
                })
    }


    private fun initToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        movieDetailCollapsingToolbar.title = movie.title
        movieDetailCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))
        title = ""
    }

    private fun initialSetting() {

        Log.v("Poster path", movie.imagePath)
        Glide.with(this).load(movie.imagePath).into(moviePosterImage)

        Glide.with(this).load(movie.backdropPath).transition(withCrossFade()).into(backdrop_Movie)
        movieTitleTextView.text = movie.title
        movieOverview.text = movie.overview
    }


    private fun loadMovieCastList() {
        val urlCastList = NetworkCallUtils.getUrlForMovieCredits(movie.movieId)
        AndroidNetworking.get(urlCastList)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        loadMovieVideosList()
                        (movieCastListRecyclerView.adapter as MovieCastAdapter).swapList(JSONParserUtils.getMovieCredits(response))
                    }

                    override fun onError(anError: ANError?) {
                        Log.v("Error", "Unable to fetch data")
                    }
                })
    }

    private fun loadMovieVideosList() {
        movieListAdapter.swapList(JSONParserUtils.getMovieVideos(movieDetailsJson))
        loadMovieReviewList()
    }

    private fun loadMovieReviewList() {
        val urlVideoList = NetworkCallUtils.getUrlForMovieReviews(movie.movieId)
        AndroidNetworking.get(urlVideoList)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        (movieReviewListRecyclerView.adapter as MovieReviewAdapter).swapList(JSONParserUtils.getMovieReviews(response))
                    }

                    override fun onError(anError: ANError?) {
                        Log.v("Error", "Unable to fetch data")
                    }
                })
    }



    override fun onPostResume() {
        super.onPostResume()
        moviePosterImage.transitionName = null
        movieTitleTextView.transitionName = null
    }
}
