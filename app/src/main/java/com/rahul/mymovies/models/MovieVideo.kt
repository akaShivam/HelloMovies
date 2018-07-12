package com.rahul.mymovies.models

import android.util.Log

class MovieVideo(var key: String, var site: String) {
    fun isVideoYoutube() : Boolean{
        Log.v("Site", site.toLowerCase())
        return (site.toLowerCase() == "youtube")
    }
}