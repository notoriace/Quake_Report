package com.example.android.quakereport;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.DecimalFormat;
import android.graphics.drawable.GradientDrawable;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {



    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateOfQuake) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateOfQuake);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateOfQuake) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateOfQuake);
    }


    //creates a variable to help us with splitting the string within the object at "of"
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
       int magnitudeColorResourceId;
        /**
         * The Math class below is used to truncate the decimal vale off of the
         * magnitude.
         * Also, note that a switch can't take a double, so we made it an int
         */
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




    private static final String LOG_TAG = EarthquakeAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor.
     * The context is used to "inflate" the layout file and the list is the data
     * we want to to populate the list with
     *
     * @param context is the current context. Used to inflate the layout file.
     * @param words   is a list of words objects to display in a list
     */


    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> words) {
        /**
         * This is where we initialize the ArrayAdapter's internal storage for the context
         * and the list.
         * The second argument is used when the ArrayAdapter is populating a single TextView.
         *Since this is a custom adapter for two TextView and an ImageView, the adapter is
         * not going use this second argument, so it can be any value, we used 0
         */

        super(context, 0, words);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    //Remember that the line below over rides the getView originally associated with ArrayAdapter
    //and replaces it with the code written below.
    //this method gets callled when a list view is trying to display a list of items in a list
    //view.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent,
                    false);
        }

        /**
         * Gets the object at this position within the list.
         */

        Earthquake currentEarthquake = getItem(position);

            //Finds text view for Magnitude
        TextView quakeMagnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        //Format the magnitude to show one decimal place.
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
       //Pulls the magnitude from the array into the text view
        quakeMagnitude.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) quakeMagnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        //I ASSUMED THAT THE DETAILS WITHIN THE SECTION WERE NOT CRITICAL SO I COPIED
        //AND PASTED THE CODE FROM THE ANSWER. IF I DO NEED THIS, THE DETAILS ARE IN
        //LESSON 21 OF JSON PARSING

        // Get the original location string from the Earthquake object,
        // which can be in the format of "5km N of Cairo, Egypt" or "Pacific-Antarctic Ridge".
        String originalLocation = currentEarthquake.getLocation();

        // If the original location string (i.e. "5km N of Cairo, Egypt") contains
        // a primary location (Cairo, Egypt) and a location offset (5km N of that city)
        // then store the primary location separately from the location offset in 2 Strings,
        // so they can be displayed in 2 TextViews.
        String primaryLocation;
        String locationOffset;

        // Check whether the originalLocation string contains the " of " text
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            // Split the string into different parts (as an array of Strings)
            // based on the " of " text. We expect an array of 2 Strings, where
            // the first String will be "5km N" and the second String will be "Cairo, Egypt".
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            // Location offset should be "5km N " + " of " --> "5km N of"
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            // Primary location should be "Cairo, Egypt"
            primaryLocation = parts[1];
        } else {
            // Otherwise, there is no " of " text in the originalLocation string.
            // Hence, set the default location offset to say "Near the".
            locationOffset = getContext().getString(R.string.near_the);
            // The primary location will be the full location string "Pacific-Antarctic Ridge".
            primaryLocation = originalLocation;
        }

        //Finds the text view for the location
        TextView primaryQuakeLocation = (TextView) listItemView.findViewById(R.id.primary_location);
        //Pulls the location from the array list to the location text view
        primaryQuakeLocation.setText(primaryLocation);

        // Find the TextView with view ID location offset
        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        // Display the location offset of the current earthquake in that TextView
        locationOffsetView.setText(locationOffset);


        //end of copy and paste



        //Create a new Date object from the time in milliseconds
        Date dateOfQuake = new Date(currentEarthquake.getDate());
        //Format the date string to something readable (i.e. Mar 8, 1992)
        String formattedDateOfQuake = formatDate(dateOfQuake);
       //Finds the text view for the date
        TextView quakeDate = (TextView) listItemView.findViewById(R.id.date);
        //Pulls the date from the array list to the date text view
        quakeDate.setText(formattedDateOfQuake);
        //Pulls the time of the quake into a readable format (i.e. 3PM)
        String formattedTimeofQuake = formatTime(dateOfQuake);
        //Finds the text view for the time
        TextView timeOfQuake = (TextView) listItemView.findViewById(R.id.time);
        //Pulls the time of the quake from the array and placed it in the time text view
        timeOfQuake.setText(formattedTimeofQuake);


        return listItemView;


    }
}

