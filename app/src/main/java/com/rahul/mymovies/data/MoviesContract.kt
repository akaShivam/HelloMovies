package com.rahul.mymovies.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import android.provider.BaseColumns._ID
import com.rahul.mymovies.data.MoviesContract.MovieEntry.Companion.COLUMN_ORIGINAL_TITLE
import com.rahul.mymovies.data.MoviesContract.MovieEntry.Companion.COLUMN_POPULARITY
import com.rahul.mymovies.data.MoviesContract.TopRatedEntry.Companion.COLUMN_MOVIE_ID_KEY


object MoviesContract {

    const val CONTENT_AUTHORITY = "com.rahul.mymovies"
    val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")

    const val PATH_MOVIES = "movies"
    const val PATH_MOST_POPULAR = "most_popular"
    const val PATH_HIGHEST_RATED = "highest_rated"
    const val PATH_TOP_RATED = "most_rated"
    const val PATH_FAVORITES = "favorites"



    class TopRatedEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED).build()


            val TABLE_NAME = "top_rated"

            const val COLUMN_MOVIE_ID_KEY = "movie_id"
            val COLUMN_OVERVIEW = "overview"
            val COLUMN_RELEASE_DATE = "release_date"
            val COLUMN_POSTER_PATH = "poster_path"
            val COLUMN_TITLE = "title"
            val COLUMN_AVERAGE_VOTE = "vote_average"
            val COLUMN_BACKDROP_PATH = "backdrop_path"
            val COLUMN_ORIGINAL_LANGUAGE = "original_language"
            val COLUMN_PAGE_NO = "page"

            val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_MOVIE_ID_KEY + " INTEGER, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_OVERVIEW + " TEXT, " +
                    COLUMN_RELEASE_DATE + " TEXT, " +
                    COLUMN_AVERAGE_VOTE + " REAL, " +
                    COLUMN_POSTER_PATH + " TEXT, " +
                    COLUMN_ORIGINAL_LANGUAGE + " TEXT," +
                    COLUMN_BACKDROP_PATH + " TEXT, " +
                    COLUMN_PAGE_NO + " INTEGER, " +
                    " UNIQUE (" + COLUMN_MOVIE_ID_KEY + ") ON CONFLICT REPLACE );"


            private val COLUMNS = arrayOf(COLUMN_MOVIE_ID_KEY, COLUMN_TITLE, COLUMN_OVERVIEW, COLUMN_RELEASE_DATE, COLUMN_AVERAGE_VOTE, COLUMN_POSTER_PATH,  COLUMN_ORIGINAL_LANGUAGE, COLUMN_BACKDROP_PATH, COLUMN_PAGE_NO)
            val INDEX_COLUMN_MOVIE_ID_KEY = 0
            val INDEX_COLUMN_TITLE = 1
            val INDEX_COLUMN_OVERVIEW = 2
            val INDEX_COLUMN_RELEASE_DATE = 3
            val INDEX_COLUMN_AVERAGE_VOTE = 4
            val INDEX_COLUMN_POSTER_PATH = 5
            val INDEX_COLUMN_ORIGINAL_LANGUAGE = 6
            val INDEX_COLUMN_BACKDROP_PATH = 7
            val INDEX_COLUMN_PAGE_NO = 8

            fun getColumns(): Array<String> {
                return COLUMNS
            }
        }
    }


    class MovieEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build()

            val CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES

            val TABLE_NAME = "movie_list"

            val COLUMN_ORIGINAL_TITLE = "original_title"
            val COLUMN_OVERVIEW = "overview"
            val COLUMN_RELEASE_DATE = "release_date"
            val COLUMN_POSTER_PATH = "poster_path"
            val COLUMN_POPULARITY = "popularity"
            val COLUMN_TITLE = "title"
            val COLUMN_AVERAGE_VOTE = "vote_average"
            val COLUMN_BACKDROP_PATH = "backdrop_path"
            val COLUMN_ORIGINAL_LANGUAGE = "original_language"
            val COLUMN_PAGE_NO = "page"

            val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_ORIGINAL_TITLE + " TEXT, " +
                    COLUMN_OVERVIEW + " TEXT, " +
                    COLUMN_RELEASE_DATE + " TEXT, " +
                    COLUMN_POSTER_PATH + " TEXT, " +
                    COLUMN_POPULARITY + " REAL, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_AVERAGE_VOTE + " REAL, " +
                    COLUMN_ORIGINAL_LANGUAGE + " TEXT," +
                    COLUMN_BACKDROP_PATH + " TEXT, " +
                    COLUMN_PAGE_NO + " INTEGER, " +
                    " UNIQUE (" + COLUMN_MOVIE_ID_KEY + ") ON CONFLICT REPLACE );"

            private val COLUMNS = arrayOf(BaseColumns._ID, COLUMN_ORIGINAL_TITLE, COLUMN_OVERVIEW, COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH, COLUMN_POPULARITY, COLUMN_TITLE, COLUMN_AVERAGE_VOTE, COLUMN_ORIGINAL_LANGUAGE, COLUMN_BACKDROP_PATH)

            fun getColumns(): Array<String> {
                return COLUMNS
            }
        }
    }
}