package com.example.nebras.newsappstageone_6;


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
import java.util.List;

/**
 * I copied and edited this class from QueryUtils class in the earthquake app-Android Basics Nanodegree course-Udacity
 */
public final class QueryOperations {

    public static final String LOG_TAG = QueryOperations.class.getSimpleName();

    private QueryOperations() {
    }

    public static List<NewsPage> fetchNewsPagesData(String requestUrl) {
        URL url = QueryOperations.createUrl(requestUrl);
        List<NewsPage> newsPageList = null;

        try {
            newsPageList = extractResultsFromJson(makeHttpRequest(url));
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in makeing http request ", e);
        }
        return newsPageList;

    }

    private static ArrayList<NewsPage> extractResultsFromJson(String jsonResponse) {
        ArrayList<NewsPage> pages = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject page = results.getJSONObject(i);
                String sectionName = page.getString("sectionName");
                String date = page.getString("webPublicationDate");
                date = date.substring(0, date.indexOf('T')); // to keep only the yyyy-mm-dd part of the date
                String webUrl = page.getString("webUrl");
                String title;
                String authorName = null;
                String webTitle = page.getString("webTitle");
                if (webTitle.contains("|")) {
                    title = webTitle.substring(0, webTitle.indexOf('|'));
                    authorName = webTitle.substring(webTitle.indexOf('|'), webTitle.length());
                    authorName = authorName.substring(2, authorName.length()); // to delete / and the space before the author name
                } else {
                    title = webTitle;
                }
                pages.add(new NewsPage(title, authorName, date, sectionName, webUrl));
            }

        } catch (JSONException e) {
            Log.e("QueryOperations", "Problem parsing the news pages JSON results", e);
        }

        // Return the list of earthquakes
        return pages;
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
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newsPages JSON results.", e);
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
}