/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
    * This really only comes into play if you're using multiple loaders.
    */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Adapter for the list of earthquakes */
    private EarthquakeAdapter earthquakeAdapter;

    private ListView listView;
    private TextView emptyState;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        earthquakeAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(earthquakeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake curEarthquake = earthquakeAdapter.getItem(i);
                Intent loadPage = new Intent(Intent.ACTION_VIEW, Uri.parse(curEarthquake.getmEarthquakeURL()));
                if(loadPage.resolveActivity(getPackageManager()) != null){
                    startActivity(loadPage);
                }
            }
        });

        emptyState = (TextView) findViewById(R.id.empty_state_textview);
        listView.setEmptyView(emptyState);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_view);
        progressBar.setVisibility(View.VISIBLE);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        if(isConnected){
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyState.setText("No internet connection");
        }


//        new EarthquakeAsyncTask().execute(USGS_REQUEST_URL);
    }

//    /**
//     * {@link AsyncTask} to perform the network request on a background thread, and then update
//     * the UI with the first earthquake in the response
//     */
//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{
//        /**
//         * This method is invoked on a background thread, so we can perform long-running operations
//         * like making a network request
//         */
//        protected List<Earthquake> doInBackground(String... urls){
//            //Don't perform the request if there are no URLs, or the first URL is null.
//            if(urls.length < 1 || urls[0] == null){
//                return null;
//            }
//            // Perform the HTTP request for earthquake data and process the response.
//            //List<Earthquake> earthquakeList = QueryUtils.fetchEarthquakeData(urls[0]);
//            return QueryUtils.fetchEarthquakeData(urls[0]);
//        }
//
//        protected void onPostExecute(List<Earthquake> result){
//            //If there is no result, do nothing.
//            if(result == null){
//                return;
//            }
//            // Update the information displayed to the user.
//            updateUi(result);
//        }
//    }
//
//    /**
//     * Update the UI with the given earthquake information.
//     */
//    private void updateUi(final List<Earthquake> earthquakeList){
//        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(this, earthquakeList);
//
//        ListView listView = (ListView) findViewById(R.id.list);
//
//        listView.setAdapter(earthquakeAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Earthquake curEarthquake = earthquakeList.get(i);
//                Intent loadPage = new Intent(Intent.ACTION_VIEW, Uri.parse(curEarthquake.getmEarthquakeURL()));
//                if(loadPage.resolveActivity(getPackageManager()) != null){
//                    startActivity(loadPage);
//                }
//            }
//        });
//    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, final List<Earthquake> earthquakes) {
        // Clear the adapter of previous earthquake data
        earthquakeAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            earthquakeAdapter.addAll(earthquakes);
        } else {
            //Set empty state text to display
            emptyState.setText(R.string.no_earthquake);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        earthquakeAdapter.clear();
    }
}
