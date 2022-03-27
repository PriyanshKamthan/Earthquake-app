package com.example.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    public static final String LOG_TAG = EarthquakeLoader.class.getName();
    private String url;

    public EarthquakeLoader(Context context,String url) {
        super(context);
        this.url = url;
    }

    public void onStartLoading() { forceLoad(); }

    @Override
    public List<Earthquake> loadInBackground() {
        if(url == null)
            return null;
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(url);
        return earthquakes;
    }
}
