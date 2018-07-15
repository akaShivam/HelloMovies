package com.rahul.mymovies.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MoviesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        const val DATABASE_NAME = "movies.db"
        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MoviesContract.TopRatedEntry.SQL_CREATE_TABLE)
        db?.execSQL(MoviesContract.MostPopularEntry.SQL_CREATE_TABLE)
        db?.execSQL(MoviesContract.NowPlayingEntry.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TopRatedEntry.TABLE_NAME)
        db?.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MostPopularEntry.TABLE_NAME)
        db?.execSQL("DROP TABLE IF EXISTS " + MoviesContract.NowPlayingEntry.TABLE_NAME)
        onCreate(db)
    }
}