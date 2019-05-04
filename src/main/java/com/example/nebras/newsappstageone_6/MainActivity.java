package com.example.nebras.newsappstageone_6;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
// note:
// In this project I used a lot of code from the earthquake app from Udacity-nanodegree

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsPage>> {
    private final String GUARDIAN_API_REQUEST_URL = "https://content.guardianapis.com/search";
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    boolean isConnected;
    NewsPageAdapter newsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listViewOfNews = findViewById(R.id.news_list);
        emptyStateTextView = findViewById(R.id.empty_state_text_view);
        progressBar = findViewById(R.id.progress_bar);
        listViewOfNews.setEmptyView(emptyStateTextView);
        connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (!isConnected) {
            emptyStateTextView.setText(R.string.no_internet_connection);
            progressBar.setVisibility(View.GONE);
        } else
            getLoaderManager().initLoader(0, null, this);

        newsPageAdapter = new NewsPageAdapter(this, new ArrayList<NewsPage>());
        listViewOfNews.setAdapter(newsPageAdapter);
        listViewOfNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsPage page = (NewsPage) parent.getItemAtPosition(position);
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(page.getmUrl())));
            }
        });
    }

    @Override
    public Loader<List<NewsPage>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String specificWord = sharedPreferences.getString(
                getString(R.string.specific_word_key),
                getString(R.string.specific_word_default_value)
        );
        String section = sharedPreferences.getString(
                getString(R.string.section_key),
                getString(R.string.section_value_default_value)
        );
        Uri uriBase = Uri.parse(GUARDIAN_API_REQUEST_URL);
        Uri.Builder uriBuilder = uriBase.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("from-date", "2018-07-15");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("q", specificWord);
        uriBuilder.appendQueryParameter("api-key", "069fe6e4-33ec-443d-9688-5a9051805678");
        return new NewsPageLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsPage>> loader, List<NewsPage> newsPages) {
        progressBar.setVisibility(View.GONE);
        newsPageAdapter.clear();
        if (newsPages != null && !newsPages.isEmpty()) {
            newsPageAdapter.addAll(newsPages);
        }
        emptyStateTextView.setText(R.string.no_news_pages_found);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        newsPageAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_button_main_menu) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
