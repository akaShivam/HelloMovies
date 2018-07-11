package com.rahul.mymovies.networkutils

import android.content.ContentValues
import android.util.Log
import com.rahul.mymovies.data.MoviesContract
import org.json.JSONException
import org.json.JSONObject

object JSONParserUtils {

    const val TMDB_RESULTS = "results"

    const val TMDB_TITLE = "title"
    const val TMDB_MOVIE_ID = "id"
    const val TMDB_POSTER_IMG = "poster_path"
    const val TMDB_VOTES = "vote_average"
    const val TMDB_LANGUAGE = "original_language"
    const val TMDB_OVERVIEW = "overview"
    const val TMDB_RELEASE_DATE = "release_date"
    const val TMDB_BACKDROP_PATH = "backdrop_path"

    fun returnMovieFromMovieList(JSONStr : String?, pageNo: Int) : Array<ContentValues?>{

        val listJson = JSONObject(JSONStr)
        val listArray = listJson.getJSONArray(TMDB_RESULTS)
        val parsedTopMovies = Array<ContentValues?>(listArray.length()) { null }

        try {
            var movieObject: JSONObject
            var title: String
            var movieID: Int
            var imagePath: String
            var backdropPath: String
            var overview: String
            var originalLanguage: String
            var voteAverage: Double
            var releseDate: String

            val imageBasePath = "https://image.tmdb.org/t/p/w500"

            for (i in 0..listArray.length() - 1) {
                movieObject = listArray.getJSONObject(i)

                title = movieObject.getString(TMDB_TITLE)
                movieID = movieObject.getInt(TMDB_MOVIE_ID)
                imagePath = movieObject.getString(TMDB_POSTER_IMG)
                backdropPath = movieObject.getString(TMDB_BACKDROP_PATH)
                overview = movieObject.getString(TMDB_OVERVIEW)
                originalLanguage = movieObject.getString(TMDB_LANGUAGE)
                voteAverage = movieObject.getDouble(TMDB_VOTES)
                releseDate = movieObject.getString(TMDB_RELEASE_DATE)

                val contentValues = ContentValues()
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_MOVIE_ID_KEY, movieID)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_TITLE, title)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_OVERVIEW, overview)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_POSTER_PATH, imageBasePath + imagePath)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_BACKDROP_PATH, imageBasePath + backdropPath)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_ORIGINAL_LANGUAGE, originalLanguage)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_AVERAGE_VOTE, voteAverage)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_RELEASE_DATE, releseDate)
                contentValues.put(MoviesContract.TopRatedEntry.COLUMN_PAGE_NO, pageNo)

                parsedTopMovies[i] = contentValues
            }
        }catch (e: JSONException){
            Log.d("JSON Error", "Not able to parse json")
        }
        return parsedTopMovies
    }
}