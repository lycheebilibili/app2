package com.example.lychee.myanime;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class MysteryAbstractLoader extends AsyncTaskLoader {
    public ArrayList<AnimeAbstract> theCache;

    public MysteryAbstractLoader(Context context) {
        super(context);
    }

    @Override
    public List<AnimeAbstract> loadInBackground() {
        if (theCache==null)
            theCache = AnimeConnection.totalConnection(AnimeConnection.mysteryPart);
        return theCache;
    }
}
