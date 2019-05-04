package com.example.nebras.newsappstageone_6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsPageAdapter extends ArrayAdapter<NewsPage> {
    public NewsPageAdapter(@NonNull Context context, @NonNull List<NewsPage> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        NewsPage currentNews = getItem(position);
        TextView newsTitleTextView = convertView.findViewById(R.id.news_title);
        newsTitleTextView.setText(currentNews.getmTitle());
        TextView newsAuthorTextView = convertView.findViewById(R.id.news_author);
        newsAuthorTextView.setText(currentNews.getmAuthorName());
        TextView newsDateTextView = convertView.findViewById(R.id.news_date);
        newsDateTextView.setText(currentNews.getmDate());
        TextView newsSectionTextView = convertView.findViewById(R.id.news_section);
        newsSectionTextView.setText(currentNews.getmSection());
        return convertView;
    }
}
