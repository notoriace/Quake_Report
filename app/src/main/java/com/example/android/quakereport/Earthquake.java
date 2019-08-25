package com.example.android.quakereport;

public class Earthquake {


    private double mMagitudeOfQuake;
    private String mLocationOfQuake;
    private long mDateOfQuake;
    private String mUrl;

    public Earthquake (double magnitudeOfQuake, String locationOfQuake, long dateOfQuake,
                       String url) {

        mMagitudeOfQuake = magnitudeOfQuake;
        mLocationOfQuake = locationOfQuake;
        mDateOfQuake = dateOfQuake;
        mUrl = url;

    }
    //returns the mag of quake
    public double getMagnitude() {return mMagitudeOfQuake;}
    //returns location of quake
    public String getLocation() {return mLocationOfQuake;}
    //returns date of quake
    public long getDate() {return mDateOfQuake;}
    //returns url of quake
    public String getUrl() {return mUrl;}

}
