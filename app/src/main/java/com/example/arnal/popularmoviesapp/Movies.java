package com.example.arnal.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arnal on 2/16/17.
 */
public class Movies implements Parcelable {
    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {

        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
    private String mTitle;
    private String mPosterPath;
    private String mOverview;
    private String mRating;
    private String mReleaseDate;

    public Movies(String title, String posterPath, String overview, String rating, String releaseDate){
        mTitle = title;
        mPosterPath = posterPath;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    private Movies(Parcel in) {
        mTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mRating = in.readString();
        mReleaseDate = in.readString();
    }

    public String getTitle(){return mTitle;}

    public String getPosterPath(){return mPosterPath;}

    public String getOverview(){return mOverview;}

    public String getRating (){return mRating;}

    public String getReleaseDate(){return mReleaseDate; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOverview);
        parcel.writeString(mRating);
        parcel.writeString(mReleaseDate);
    }
}
