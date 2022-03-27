package com.example.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private EarthquakeAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        adapter = new EarthquakeAdapter(this,new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Earthquake currentEarthquake = adapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquakeLoader(this , USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_earthquakes);
        adapter.clear();
        if(earthquakes!=null && !earthquakes.isEmpty()) adapter.addAll(earthquakes);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        adapter.clear();
    }
}