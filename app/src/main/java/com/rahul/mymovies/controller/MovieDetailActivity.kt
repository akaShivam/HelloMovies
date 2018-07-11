package com.rahul.mymovies.controller

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.rahul.mymovies.R
import com.rahul.mymovies.models.Movie
import kotlinx.android.synthetic.main.content_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {
    lateinit var movie : Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        movie = intent.getParcelableExtra<Movie>("movie")
        initialSetting()

    }

    private fun initialSetting() {

        Log.v("Poster path", movie.imagePath)
        Glide.with(this).load(movie.imagePath).into(moviePosterImage)

        Glide.with(this).load(movie.backdropPath).transition(withCrossFade()).into(backdrop_Movie)
        movieTitleTextView.text = movie.title
        movieOverview.text = movie.overview

        movieDetailCollapsingToolbar.title = movie.title
        movieDetailCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))
        title = ""

    }

}
