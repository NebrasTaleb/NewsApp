package com.example.nebras.newsappstageone_6;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsPageLoader extends AsyncTaskLoader<List<NewsPage>> {
    private String mUrl;

    public NewsPageLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<NewsPage> loadInBackground() {
        if (mUrl == null)
            return null;
        return QueryOperations.fetchNewsPagesData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
