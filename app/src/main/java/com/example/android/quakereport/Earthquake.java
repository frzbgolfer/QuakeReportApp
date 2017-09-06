package com.example.android.quakereport;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by frzbg_orpozj7 on 9/4/2017.
 */

public class Earthquake extends ArrayList {
    private double mMagnitude;
    private String mPlace;
    private long mTimeInMilliseconds;
    private String mEarthquakeURL;

    /**
     *
     * @param mMagnitude is the magnitude (size) of the earthquake
     * @param mPlace is the location the earthquake occurred
     * @param mTimeInMilliseconds is the date/time the earthquake happened
     * @param mEarthquakeURL is the url of the earthquake
     */
    public Earthquake(double mMagnitude, String mPlace, long mTimeInMilliseconds, String mEarthquakeURL) {
        this.mMagnitude = mMagnitude;
        this.mPlace = mPlace;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.mEarthquakeURL = mEarthquakeURL;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getmEarthquakeURL() {
        return mEarthquakeURL;
    }
}
