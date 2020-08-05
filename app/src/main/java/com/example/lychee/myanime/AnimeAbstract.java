package com.example.lychee.myanime;

import android.graphics.Bitmap;

public class AnimeAbstract {
    private Bitmap mBitmap;
    private String mName;
    private String mSummary;
    private String mID;
    public AnimeAbstract(Bitmap bitmap, String name, String summary,String theID){
        mBitmap = bitmap;
        mName = name;
        mSummary = summary;
        mID = theID;
    }

    public String getmName() {
        return mName;
    }

    public String getmSummary() {
        return mSummary;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public String getmID() { return mID; }
}
