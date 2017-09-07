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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        //final ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

//        // Create a fake list of earthquake locations.
//        final ArrayList<Earthquake> earthquakes = new ArrayList<>();
//        earthquakes.add(new Earthquake("7.2", "San Francisco", "Feb 2, 2016"));
//        earthquakes.add(new Earthquake("6.1", "London", "July 20, 2015"));
//        earthquakes.add(new Earthquake("3.9", "Tokyo", "Nov 10, 2014"));
//        earthquakes.add(new Earthquake("5.4", "Mexico City", "May 3, 2014"));
//        earthquakes.add(new Earthquake("2.8", "Moscow", "Jun 31, 2013"));
//        earthquakes.add(new Earthquake("4.9", "Rio de Janeiro", "Aug 19, 2012"));
//        earthquakes.add(new Earthquake("1.6", "Paris", "Oct 30, 2011"));

        new EarthquakeAsyncTask().execute(USGS_REQUEST_URL);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then update
     * the UI with the first earthquake in the response
     */
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{
        /**
         * This method is invoked on a background thread, so we can perform long-running operations
         * like making a network request
         */
        protected List<Earthquake> doInBackground(String... urls){
            //Don't perform the request if there are no URLs, or the first URL is null.
            if(urls.length < 1 || urls[0] == null){
                return null;
            }
            // Perform the HTTP request for earthquake data and process the response.
            //List<Earthquake> earthquakeList = QueryUtils.fetchEarthquakeData(urls[0]);
            return QueryUtils.fetchEarthquakeData(urls[0]);
        }

        protected void onPostExecute(List<Earthquake> result){
            //If there is no result, do nothing.
            if(result == null){
                return;
            }
            // Update the information displayed to the user.
            updateUi(result);
        }
    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(final List<Earthquake> earthquakeList){
        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(this, earthquakeList);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(earthquakeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake curEarthquake = earthquakeList.get(i);
                Intent loadPage = new Intent(Intent.ACTION_VIEW, Uri.parse(curEarthquake.getmEarthquakeURL()));
                if(loadPage.resolveActivity(getPackageManager()) != null){
                    startActivity(loadPage);
                }
            }
        });
    }
}
