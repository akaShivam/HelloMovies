package com.rahul.mymovies.data

import android.net.Uri
import android.provider.BaseColumns


object MoviesContract {

    const val CONTENT_AUTHORITY = "com.rahul.mymovies"
    val BASE_CONTENT_URI : Uri = Uri.parse("content://$CONTENT_AUTHORITY")

    const val PATH_MOST_POPULAR = "most_popular"
    const val PATH_NOW_PLAYING = "now_playing"
    const val PATH_TOP_RATED = "most_rated"
    const val PATH_FAVORITES = "favorites"

    const val COLUMN_MOVIE_ID_KEY = "movie_id"
    const val COLUMN_OVERVIEW = "overview"
    const val COLUMN_RELEASE_DATE = "release_date"
    const val COLUMN_POSTER_PATH = "poster_path"
    const val COLUMN_TITLE = "title"
    const val COLUMN_AVERAGE_VOTE = "vote_average"
    const val COLUMN_BACKDROP_PATH = "backdrop_path"
    const val COLUMN_ORIGINAL_LANGUAGE = "original_language"
    const val COLUMN_PAGE_NO = "page"

    private val COLUMNS_NORMAL = arrayOf(COLUMN_MOVIE_ID_KEY
            , COLUMN_TITLE
            , COLUMN_OVERVIEW
            , COLUMN_RELEASE_DATE
            , COLUMN_AVERAGE_VOTE
            , COLUMN_POSTER_PATH
            ,  COLUMN_ORIGINAL_LANGUAGE
            , COLUMN_BACKDROP_PATH
            , COLUMN_PAGE_NO)

    const val INDEX_COLUMN_MOVIE_ID_KEY = 0
    const val INDEX_COLUMN_TITLE = 1
    const val INDEX_COLUMN_OVERVIEW = 2
    const val INDEX_COLUMN_RELEASE_DATE = 3
    const val INDEX_COLUMN_AVERAGE_VOTE = 4
    const val INDEX_COLUMN_POSTER_PATH = 5
    const val INDEX_COLUMN_ORIGINAL_LANGUAGE = 6
    const val INDEX_COLUMN_BACKDROP_PATH = 7
    const val INDEX_COLUMN_PAGE_NO = 8

    fun getNormalColumns(): Array<String> {
        return COLUMNS_NORMAL
    }


    class TopRatedEntry : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED).build()

            const val TABLE_NAME = "top_rated"

            const val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
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

        }
    }


    class MostPopularEntry : BaseColumns {
        companion object {
            val CONTENT_URI : Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOST_POPULAR).build()

            const val TABLE_NAME = "most_popular"

            const val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
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
        }
    }

    class NowPlayingEntry : BaseColumns {
        companion object {
            val CONTENT_URI : Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOW_PLAYING).build()

            const val TABLE_NAME = "now_playing"

            const val SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
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
        }
    }
}