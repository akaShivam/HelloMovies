package com.rahul.mymovies.controller.moviedetailactivity

class MovieCast(val charcter: String, val name: String, val poster_path: String){
    fun getPosterPath() : String{
        return "https://image.tmdb.org/t/p/w500$poster_path"
    }
}