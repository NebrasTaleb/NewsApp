package com.example.nebras.newsappstageone_6;

// since the api returns pages then the class name is page
public class NewsPage {
    private String mTitle;
    private String mAuthorName;
    private String mDate;
    private String mSection;
    private String mUrl;

    public NewsPage(String mTitle, String mAuthorName, String mDate, String mSection, String mUrl) {
        this.mTitle = mTitle;
        this.mAuthorName = mAuthorName;
        this.mDate = mDate;
        this.mSection = mSection;
        this.mUrl = mUrl;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmSection() {
        return mSection;
    }
}
