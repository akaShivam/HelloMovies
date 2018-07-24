package com.rahul.mymovies.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class MoviesProvider : ContentProvider() {
    private lateinit var mOpenHelper: MoviesDbHelper

    private val CODE_TOP_RATED = 100
    private val CODE_MOST_POPULAR = 200
    private val CODE_NOW_PLAYING = 300
    private val CODE_FAVORITES = 400

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init{
        val authority = MoviesContract.CONTENT_AUTHORITY

        sURIMatcher.addURI(authority, MoviesContract.PATH_TOP_RATED, CODE_TOP_RATED)
        sURIMatcher.addURI(authority, MoviesContract.PATH_MOST_POPULAR, CODE_MOST_POPULAR)
        sURIMatcher.addURI(authority, MoviesContract.PATH_NOW_PLAYING, CODE_NOW_PLAYING)
        sURIMatcher.addURI(authority, MoviesContract.PATH_FAVORITES, CODE_FAVORITES)
    }


    override fun onCreate(): Boolean {
        mOpenHelper = MoviesDbHelper(context)
        return true
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val cursor: Cursor

        when(sURIMatcher.match(uri)){
            CODE_TOP_RATED -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }

            CODE_MOST_POPULAR -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.MostPopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            CODE_NOW_PLAYING -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.NowPlayingEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            CODE_FAVORITES -> {
                cursor = mOpenHelper.readableDatabase.query(
                        MoviesContract.FavoritesEntry.TABLE_NAME,
                        projection,
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
        val db = mOpenHelper.writableDatabase

        when(sURIMatcher.match(uri)){
            CODE_FAVORITES -> {
                val id = db.insert(MoviesContract.FavoritesEntry.TABLE_NAME, null, values)
                context.contentResolver.notifyChange(uri, null)
                Log.v("Inserted", "Favorite has been inserted $id")
            }
        }
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
                        val id = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, value)
                        if(id != -1L){
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
                        val id = db.insert(MoviesContract.MostPopularEntry.TABLE_NAME, null, value)
                        if(id != -1L){
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
                        val id = db.insert(MoviesContract.NowPlayingEntry.TABLE_NAME, null, value)
                        if(id != -1L){
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
        val db = mOpenHelper.writableDatabase
        when(sURIMatcher.match(uri)){
            CODE_FAVORITES -> {
                val del = db.delete(MoviesContract.FavoritesEntry.TABLE_NAME,
                        selection,
                        selectionArgs)
                context.contentResolver.notifyChange(uri, null)
                Log.v("Deleted", "Favorite has been deleted $del")
                return 1
            }
        }

        return 0
    }

    override fun getType(uri: Uri?): String {
        return ""
    }

    override fun shutdown() {
        super.shutdown()
        mOpenHelper.close()
    }
}