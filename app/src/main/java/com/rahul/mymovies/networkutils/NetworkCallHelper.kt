package com.rahul.mymovies.networkutils

import android.content.Context
import com.rahul.mymovies.data.MoviesContract

object NetworkCallHelper {

    fun addTopMoviesFromJson(context: Context, pageNo: Int) : Int{
        val listJsonStr: String? = NetworkCallUtils.getResponseFromUrl(NetworkCallUtils.getUrlForTopRated(pageNo))
        if(listJsonStr == null || listJsonStr.isEmpty()){
            return 0
        }

        val parsedTopMovies = JSONParserUtils.returnMovieFromMovieList(listJsonStr, pageNo)

        if(!parsedTopMovies.isEmpty()){
            val moviesContentResolver = context.contentResolver

            moviesContentResolver.bulkInsert(MoviesContract.TopRatedEntry.CONTENT_URI,
                    parsedTopMovies)
        }
        return 1
    }
}