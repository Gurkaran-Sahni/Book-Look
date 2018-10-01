package com.example.android.booklook;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>>{

    public static String SAMPLE_JSON_RESPONSE;
    public BookCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setElevation(15.0f);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button info =findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(BookList.this,About.class);
                BookList.this.startActivity(activity);
            }
        });

        String search = getIntent().getExtras().getString("search_phrase");
        search=search.replaceAll(" ","+");
        SAMPLE_JSON_RESPONSE = "https://www.googleapis.com/books/v1/volumes?q="+search;

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            GridView view = findViewById(R.id.book_list);
            adapter = new BookCardAdapter(BookList.this, new ArrayList<Book>());
            view.setAdapter(adapter);

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
            Log.v("MainActivity", "initLoader");
        }

        else{
            findViewById(R.id.loading_spinner).setVisibility(View.GONE);
            TextView mEmpty = findViewById(R.id.empty);
            mEmpty.setText("Please, make sure you are connected to internet");
        }

//        BookAsyncTask task = new BookAsyncTask();
//        task.execute(SAMPLE_JSON_RESPONSE);
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v("MainActivity","onCreateLoader");
        return new BookLoader(this,SAMPLE_JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
//        mAdapter.clear();
        Log.v("MainActivity","onLoadFinished");
        if(books == null || books.isEmpty()){
            TextView mEmpty = findViewById(R.id.empty);
            findViewById(R.id.loading_spinner).setVisibility(View.GONE);
            mEmpty.setText("Sorry, no books found :(");
            return;
        }
        else
            {
            adapter.clear();
            findViewById(R.id.loading_spinner).setVisibility(View.GONE);
            adapter.addAll(books);
        }
//        findViewById(R.id.loading_spinner).setVisibility(View.GONE);
//        TextView emptyView = findViewById(R.id.empty_view);
//        emptyView.setText("No Earthquakes found");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader)
    {
        adapter.clear();
        Log.v("MainActivity","onLoaderReset");
    }

//    private class BookAsyncTask extends AsyncTask<String,Void,ArrayList<Book>> {
//
//        @Override
//        protected ArrayList<Book> doInBackground(String... urls) {
//            if(urls[0]==null || urls.length<1){
//                return null;
//            }
//
//            ArrayList<Book> books = QueryUtils.fetchEarthquakeData(urls[0]);
//            return books;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Book> books) {
//            if(books == null){
//                return;
//            }
//
//            BookCardAdapter adapter = new BookCardAdapter(BookList.this,books);
//            GridView view = findViewById(R.id.book_list);
//            view.setAdapter(adapter);
//        }
//    }

}
