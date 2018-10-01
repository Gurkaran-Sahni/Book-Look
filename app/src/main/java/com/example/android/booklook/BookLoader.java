package com.example.android.booklook;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v("BookLoader","onStartLoading");
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if(mUrl==null){
            return null;
        }
        Log.v("BookLoader","loadInBackground");

        ArrayList<Book> books = QueryUtils.fetchEarthquakeData(mUrl);
        return books;
    }
}
