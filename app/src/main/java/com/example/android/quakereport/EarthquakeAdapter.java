package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.floor;

/**
 * Created by frzbg_orpozj7 on 9/4/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    /**
     *
     * @param context is the context of the app
     * @param earthquakes is the list of earthquake objects
     */
    public EarthquakeAdapter(@NonNull Context context, @NonNull List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        //Check if the existing view is being reused, otherwise inflate the view
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final Earthquake currentEarthquake = getItem(position);

        //Setting up the format for the Earthquake magnitude to be displayed formatted to 1 decimal place
        DecimalFormat formattedMag = new DecimalFormat("0.0");

        //Get the TextView for the magnitude and display the magnitude of the current Earthquake object
        TextView magTextView = (TextView) listItemView.findViewById(R.id.mag_text_view);
        magTextView.setText(formattedMag.format(currentEarthquake.getmMagnitude()));

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        //Splitting the Earthquake Place string into 2 chunks to be displayed in separate TextViews
        String offset;
        String primLoc;
        if(currentEarthquake.getmPlace().indexOf(" of ") != -1) {
            offset = currentEarthquake.getmPlace().toString().substring(0, currentEarthquake.getmPlace().indexOf(" of ")+4);
            primLoc = currentEarthquake.getmPlace().toString().substring(currentEarthquake.getmPlace().indexOf(" of ")+4);
        }else{
            offset = "Near the";
            primLoc = currentEarthquake.getmPlace().toString();
        }

        //Get the TextView for the proximity details and display it in the current Earthquake object
        TextView proxTextView = (TextView) listItemView.findViewById(R.id.proximity_text_view);
        proxTextView.setText(offset);

        //Get the TextView for the location and display the location of the current Earthquake object
        TextView locTextView = (TextView) listItemView.findViewById(R.id.place_text_view);
        locTextView.setText(primLoc);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getmTimeInMilliseconds());

        //Get the TextView for the date and display the date of the current Earthquake object
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);

        //Get the TextView for the time and display the time of the current Earthquake object
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        timeTextView.setText(formattedTime);

        return listItemView;
    }

    /**
     *
     * @param mag is the magnitude of the earthquake
     * @return
     */
    private int getMagnitudeColor(double mag) {
        int magColorResourceId;
        int magnitudeFloor = (int) Math.floor(mag);

        switch (magnitudeFloor){
            case 0:
            case 1:
                magColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magColorResourceId = R.color.magnitude9;
                break;
            default:
                magColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magColorResourceId);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
