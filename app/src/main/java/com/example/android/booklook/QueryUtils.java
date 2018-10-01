package com.example.android.booklook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static String authors="";
    public static String rating;
    public static Bitmap smallThumbnail = null;
    public static String description;
    public static String count;

    public QueryUtils() {
    }

    public static ArrayList<Book>fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        Log.v("QueryUtils","fetchEarthquakeData");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static ArrayList<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.

        ArrayList<Book> books = new ArrayList<>();

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        try {

            JSONObject jsonObject = new JSONObject(bookJSON);
            if (jsonObject.has("items")) {
                JSONArray items = jsonObject.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {

                    JSONObject book = items.getJSONObject(i);
                    JSONObject volume = book.getJSONObject("volumeInfo");
                    String title = volume.getString("title");
                    if (volume.has("authors")) {
                        JSONArray author_list = volume.getJSONArray("authors");
                        authors = "";
                        authors += author_list.getString(0);
                        for (int j = 1; j < author_list.length(); j++) {
                            authors += ", " + author_list.getString(j);
                        }
                    } else {
                        authors = "no authors";
                    }
                    if (volume.has("averageRating")) {
                        double ratings = volume.getDouble("averageRating");
                        rating = Double.toString(ratings);
                        Log.v("rating", rating);
                    } else {
                        rating = "no rating";
                        Log.v("rating", rating);
                    }

                    if (volume.has("description")) {
                        description = volume.getString("description");
                    } else {
                        description = "no description";
                    }

                    if (volume.has("pageCount")) {
                        count = String.valueOf(volume.getInt("pageCount")) + " pages";
                    } else {
                        count = "no page count";
                    }

                    if (volume.has("imageLinks")) {
                        JSONObject imageLinks = volume.getJSONObject("imageLinks");
                        smallThumbnail = fetchThumbnail(imageLinks.getString("thumbnail"));
                        books.add(new Book(title, smallThumbnail, authors, rating, description, count, 0));
                    } else {
                        books.add(new Book(title, smallThumbnail, authors, rating, description, count, R.drawable.background));
                    }


                }
            }
            else{
                return books;
            }
        }

        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Books JSON results", e);
        }

        return books;

    }

    public static Bitmap fetchThumbnail(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        Bitmap smallThumbnail = null;
        try {
            smallThumbnail = makeHttpRequestForBitmap(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        Log.v("SmallThumbnail", String.valueOf(smallThumbnail));
        return smallThumbnail;
    }

    //Method that makes HTTP request and returns a Bitmap as the response
    private static Bitmap makeHttpRequestForBitmap(URL url) throws IOException {
        Bitmap bitmapResponse=null;

        //If the URL is null, then return early.
        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                bitmapResponse = BitmapFactory.decodeStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return bitmapResponse;
    }
}