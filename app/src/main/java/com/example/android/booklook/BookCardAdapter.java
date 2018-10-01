package com.example.android.booklook;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BookCardAdapter extends ArrayAdapter<Book> {

    public BookCardAdapter(Activity context, ArrayList<Book> books){
        super(context,0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View ListView = convertView;

        if(ListView == null) {
            ListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.card_book, parent, false);
        }

        final Book currentCard = getItem(position);
        TextView BookRating = ListView.findViewById(R.id.book_rating);
        RatingBar ratingBar = ListView.findViewById(R.id.rating_bar);
        ratingBar.setVisibility(View.VISIBLE);
        BookRating.setVisibility(View.VISIBLE);



        ImageView BookImage = ListView.findViewById(R.id.book_image);
        if(currentCard.getmImage()==0) {
            BookImage.setImageBitmap(currentCard.getmBookImageUrl());
        }
        else{
            BookImage.setImageResource(currentCard.getmImage());
        }
//        DownloadImageTask bookImage = new DownloadImageTask(BookImage);
//        bookImage.execute(currentCard.getmBookImageUrl());

        TextView BookName = ListView.findViewById(R.id.book_name);
        BookName.setText(currentCard.getmBookName());

        TextView BookAuthor = ListView.findViewById(R.id.book_author);
        BookAuthor.setText(currentCard.getmAuthor());

        if(currentCard.getmRating()=="no rating") {
            BookRating.setText(currentCard.getmRating());
            ratingBar.setVisibility(View.GONE);
        }
        else{
            BookRating.setVisibility(View.GONE);
            ratingBar.setRating(Float.valueOf(currentCard.getmRating()));
        }
        TextView BookDescription = ListView.findViewById(R.id.book_description);
        BookDescription.setText(currentCard.getmDescription());

        TextView BookCount = ListView.findViewById(R.id.book_pages);
        BookCount.setText(currentCard.getmCount());

        return ListView;
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap bmp = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                bmp = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return bmp;
//        }
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }
}

