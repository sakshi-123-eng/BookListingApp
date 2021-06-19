package com.sakshi.booklistingapp;

public class BookInfo {
    // Book information which we have to show
    private  String mTitle;
    private String mUrl;
    private String mThumbnail;


    public BookInfo(String title, String url, String thumbnail){
        mTitle = title;
        mUrl = url;
        mThumbnail = thumbnail;
    }

   // function to access mTitle
    public String getTitle(){
        return mTitle;
    }

    // function to access mUrl
    public String getUrl(){ return mUrl; }

    // function to access mImg
    public  String getImg(){ return  mThumbnail; }
}
