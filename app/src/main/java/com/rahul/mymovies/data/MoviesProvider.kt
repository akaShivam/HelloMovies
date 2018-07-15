package com.rahul.mymovies.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class MoviesProvider : ContentProvider() {
    lateinit var mOpenHelper: MoviesDbHelper

    private val CODE_TOP_RATED = 100
    private val CODE_MOST_POPULAR = 200
    private val CODE_NOW_PLAYING = 300

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init{
        val authority = MoviesContract.CONTENT_AUTHORITY

        sURIMatcher.addURI(authority, MoviesContract.PATH_TOP_RATED, CODE_TOP_RATED)
        sURIMatcher.addURI(authority, MoviesContract.PATH_MOST_POPULAR, CODE_MOST_POPULAR)
        sURIMatcher.addURI(authority, MoviesContract.PATH_NOW_PLAYING, CODE_NOW_PLAYING)
    }


    override fun onCreate(): Boolean {
        mOpenHelper = MoviesDbHelper(context)
        return true
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        var cursor: Cursor

        when(sURIMatcher.match(uri)){
            CODE_TOP_RATED -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.TopRatedEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }

            CODE_MOST_POPULAR -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.MostPopularEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            CODE_NOW_PLAYING -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.NowPlayingEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            else -> throw UnsupportedOperationException("Unknown uri $uri")
        }

        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        return Uri.EMPTY
    }

    override fun bulkInsert(uri: Uri?, values: Array<out ContentValues>?): Int {
        val db = mOpenHelper.writableDatabase

        when(sURIMatcher.match(uri)){
            CODE_TOP_RATED -> {
                db.beginTransaction()
                var rowsInserted = 0

                try{
                    for(value: ContentValues in values!!){
                        val _id = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, value)
                        if(_id != -1L){
                            rowsInserted++
                        }
                    }
                    db.setTransactionSuccessful()
                }finally {
                    db.endTransaction()
                }

                if(rowsInserted > 0){
                    context.contentResolver.notifyChange(uri, null)
                }
                return rowsInserted
            }


            CODE_MOST_POPULAR -> {
                db.beginTransaction()
                var rowsInserted = 0

                try{
                    for(value: ContentValues in values!!){
                        val _id = db.insert(MoviesContract.MostPopularEntry.TABLE_NAME, null, value)
                        if(_id != -1L){
                            rowsInserted++
                        }
                    }
                    db.setTransactionSuccessful()
                }finally {
                    db.endTransaction()
                }

                if(rowsInserted > 0){
                    context.contentResolver.notifyChange(uri, null)
                }
                return rowsInserted
            }

            CODE_NOW_PLAYING -> {
                db.beginTransaction()
                var rowsInserted = 0

                try{
                    for(value: ContentValues in values!!){
                        val _id = db.insert(MoviesContract.NowPlayingEntry.TABLE_NAME, null, value)
                        if(_id != -1L){
                            rowsInserted++
                        }
                    }
                    db.setTransactionSuccessful()
                }finally {
                    db.endTransaction()
                }

                if(rowsInserted > 0){
                    context.contentResolver.notifyChange(uri, null)
                }
                return rowsInserted
            }

            else -> return super.bulkInsert(uri, values)
        }
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri?): String {
        return ""
    }



}