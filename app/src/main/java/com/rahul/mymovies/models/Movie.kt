package com.rahul.mymovies.models

import android.os.Parcel
import android.os.Parcelable

class Movie(var movieId: Int, var title: String, var overview: String, var imagePath: String, var backdropPath: String,
            var releaseDate: String, var averageVote: Double) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(movieId)
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeString(imagePath)
        parcel.writeString(backdropPath)
        parcel.writeString(releaseDate)
        parcel.writeDouble(averageVote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }


}