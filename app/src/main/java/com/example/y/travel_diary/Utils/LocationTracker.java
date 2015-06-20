package com.example.y.travel_diary.Utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Y on 2015-06-20.
 */
public class LocationTracker {
    private static final long POLLING = 1000 * 10;

    private double latitude;
    private double longitude;
    private double accuracy;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private boolean availGPS = false;
    private boolean availNetwork = false;
    private String provider = null;

    private Context mContext;

    public LocationTracker (Context context) {
        mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                accuracy = location.getAccuracy();

                Log.e("LocationListener", longitude+" "+latitude+" "+accuracy);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("LocationListener", "onStatusChanged");

                if (provider.equals(LocationManager.GPS_PROVIDER))
                    availGPS = false;
                else
                    availNetwork = false;
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("LocationListener", "onProviderEnabled");

                if (provider.equals(LocationManager.GPS_PROVIDER))
                    availGPS = true;
                else
                    availNetwork = true;

                locationManager.requestLocationUpdates(provider, POLLING, 0, locationListener);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("LocationListener", "onProviderDisabled");
            }
        };

        reqLocation();
    }

    public void reqLocation () {
        availGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        availNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (availGPS) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (availNetwork) {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if (availGPS || availNetwork)
            locationManager.requestLocationUpdates(provider, POLLING, 0, locationListener);
    }

    public Location getLocation () {
        if (availGPS || availNetwork) {
            Log.e("hihi", "HIHIHIHI");
            return locationManager.getLastKnownLocation(provider);
        } else
            return null;
    }

    public void destroy () {
        locationManager.removeUpdates(locationListener);
    }
}
