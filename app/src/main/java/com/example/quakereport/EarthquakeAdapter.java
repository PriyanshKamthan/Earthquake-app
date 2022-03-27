package com.example.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) { super(context,0,earthquakes);}

    public String dateFormatter(Date dateObject) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd,yy");
        return simpleDateFormat.format(dateObject);
    }

    public String timeFormatter(Date dateObject) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("h:mm a");
        return simpleTimeFormat.format(dateObject);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Earthquake currentEarthquake = getItem(position);
        View listView = convertView;
        if(listView == null) listView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_item,parent,false);

        TextView magnitudeView = (TextView) listView.findViewById(R.id.magnitude);
        TextView cityTextView = (TextView) listView.findViewById(R.id.primary_location);
        TextView offsetTextView = (TextView) listView.findViewById(R.id.location_offset);
        TextView dateTextView = (TextView) listView.findViewById(R.id.date);
        TextView timeTextView = (TextView) listView.findViewById(R.id.time);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        DecimalFormat formatter = new DecimalFormat("0.0");
        String magnitude = formatter.format(currentEarthquake.getMagnitude());
        String location = currentEarthquake.getLocation();
        Date dateObject = new Date(currentEarthquake.getTime());

        String primaryLocation;
        String locationOffset;
        final String LOCATION_SEPARATOR = " of ";
        if(location.contains(LOCATION_SEPARATOR)) {
            String[] parts = location.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }
        else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = location;
        }

        magnitudeView.setText(magnitude);
        cityTextView.setText(primaryLocation);
        offsetTextView.setText(locationOffset);
        dateTextView.setText(dateFormatter(dateObject));
        timeTextView.setText(timeFormatter(dateObject));

        return listView;
    }
}
