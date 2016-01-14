package net.byloth.sky.updaters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import net.byloth.sky.LiveWallpaper;

/**
 * Created by Matteo on 30/10/2015.
 */
public class LocationUpdater implements LocationListener
{
    static private boolean hasLocation = false;

    static private double latitude;
    static private double longitude;

    private OnLocationUpdate onLocationUpdate;

    static public boolean hasLocation()
    {
        return hasLocation;
    }

    static public double getLatitude()
    {
        if (hasLocation == true)
        {
            return latitude;
        }
        else
        {
            throw new RuntimeException();
        }
    }
    static public double getLongitude()
    {
        if (hasLocation == true)
        {
            return longitude;
        }
        else
        {
            throw new RuntimeException();
        }
    }

    private void requestLocationUpdates(LocationManager locationManager) throws SecurityException
    {
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
    }

    public LocationUpdater(LocationManager locationManager, Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int accessFineLocationPermission = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessFineLocationPermission == PackageManager.PERMISSION_GRANTED)
            {
                requestLocationUpdates(locationManager);
            }
            else
            {
                Log.e(LiveWallpaper.APPLICATION_NAME, "Permission denied to access user's location!");

                // TODO: Require premissions...
            }
        }
        else
        {
            requestLocationUpdates(locationManager);
        }
    }

    public LocationUpdater setOnLocationUpdate(OnLocationUpdate onLocationUpdateInstance)
    {
        onLocationUpdate = onLocationUpdateInstance;

        return this;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if ((latitude != location.getLatitude()) && (longitude != location.getLongitude()))
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            hasLocation = true;

            Log.i(LiveWallpaper.APPLICATION_NAME, "User location has been updated: " + latitude + ", " + longitude);

            if (onLocationUpdate != null)
            {
                onLocationUpdate.onUpdate(latitude, longitude);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    public interface OnLocationUpdate
    {
        void onUpdate(double locationLatitude, double locationLongitude);
    }
}
