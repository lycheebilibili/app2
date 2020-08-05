package com.example.lychee.myanime;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class AdventureAbstractLoader extends AsyncTaskLoader<List<AnimeAbstract>> {
    public ArrayList<AnimeAbstract> theCache;

    public AdventureAbstractLoader(Context context) {
        super(context);
    }

    @Override
    public List<AnimeAbstract> loadInBackground() {
        if (theCache==null)
        theCache = AnimeConnection.totalConnection(AnimeConnection.adventurePart);
       return theCache;
    }

}
