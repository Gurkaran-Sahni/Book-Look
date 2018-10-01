package com.example.android.booklook;

import android.graphics.Bitmap;

public class Book {
    private String mBookName;
    private Bitmap mBookImageUrl;
    private String mAuthor;
    private String mRating;
    private String mDescription;
    private int mImage=0;
    private String mCount;

    public Book(String name, Bitmap imageurl, String author, String rating,String description, String count,int Image){
        mBookName = name;
        mBookImageUrl = imageurl;
        mAuthor = author;
        mRating = rating;
        mDescription = description;
        mImage = Image;
        mCount = count;

    }

    public String getmBookName() {
        return mBookName;
    }

    public Bitmap getmBookImageUrl() {
        return mBookImageUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmRating() {
        return mRating;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmImage() {
        return mImage;
    }

    public String getmCount() {
        return mCount;
    }
}
