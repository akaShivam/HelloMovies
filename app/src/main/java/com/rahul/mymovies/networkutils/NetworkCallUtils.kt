package com.rahul.mymovies.networkutils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

object NetworkCallUtils {
    private val TAG = NetworkCallUtils::class.java.simpleName

    private const val APP_ID1 = "17a9e2faba4b24afe4bbf42451dd32b0"

    private const val TOP_RATED_MOVIE_URl = "https://api.themoviedb.org/3/movie/top_rated"
    private const val PARTICULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie"

    private const val APPID_PARAM = "api_key"
    private const val LANGUAGE_PARAM = "language"
    private const val PAGE_PARAM = "page"

    fun getUrlForTopRated(pageNo: Int): String {
        val topRatedQueryUri = Uri.parse(TOP_RATED_MOVIE_URl).buildUpon()
                .appendQueryParameter(APPID_PARAM, APP_ID1)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .appendQueryParameter(PAGE_PARAM, pageNo.toString())
                .build()

        return topRatedQueryUri.toString()
    }

    fun getUrlForMovieVideos(movieId: Int): String {
        val particularMovieVideoUri = Uri.parse("$PARTICULAR_MOVIE_URL/$movieId/videos").buildUpon()
                .appendQueryParameter(APPID_PARAM, APP_ID1)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .build()

        return particularMovieVideoUri.toString()
    }

    fun getUrlForMovieReviews(movieId: Int): String {
        val particularMovieReviewUri = Uri.parse("$PARTICULAR_MOVIE_URL/$movieId/reviews").buildUpon()
                .appendQueryParameter(APPID_PARAM, APP_ID1)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .build()

        return particularMovieReviewUri.toString()
    }

    fun getUrlForMovieCredits(movieId: Int) : String {
        val particularMovieCreditsUri = Uri.parse("$PARTICULAR_MOVIE_URL/$movieId/credits").buildUpon()
                .appendQueryParameter(APPID_PARAM, APP_ID1)
                .build()

        return particularMovieCreditsUri.toString()
    }

    fun getUrlForMovieDetailsAndVideos(movieId: Int) : String {
        val particularMovieDetailsandVideoUri = Uri.parse("$PARTICULAR_MOVIE_URL/$movieId").buildUpon()
                .appendQueryParameter(APPID_PARAM, APP_ID1)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .appendQueryParameter("append_to_response", "videos")
                .build()

        return particularMovieDetailsandVideoUri.toString()
    }

    fun getUrlForTmdbResponse(movieId: Int) : String {
        val tmdbUri = Uri.parse("$PARTICULAR_MOVIE_URL/$movieId").buildUpon()
                .appendQueryParameter(APPID_PARAM, APP_ID1)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .appendQueryParameter("append_to_response", "videos,reviews,credits")
                .build()
        return tmdbUri.toString()
    }

    fun getResponseFromUrl(urlString: String): String?{
        var returnResponse: String? = null
        Log.v("URL", urlString)

        var urlConnection: HttpURLConnection? = null
        try {
            val URL = URL(urlString)

            urlConnection = URL.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 30 * 1000
            urlConnection.readTimeout = 30 * 1000

            val inputStream = urlConnection.inputStream

            val scanner = Scanner(inputStream)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()

            if (hasInput) {
                returnResponse = scanner.next()
            }
            scanner.close()

        }catch(e : Exception){
            Log.v("ERROR", "ERROR MAKING NETWORK REQUEST")
        }
        finally {
            urlConnection?.disconnect()
        }

        Log.v("Hi", returnResponse.toString())
        return returnResponse
    }

}