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

import java.util.List;

/**
 * Created by Matteo on 17/04/2016.
 */
public class LocationUpdater implements LocationListener
{
    static final private String TAG = "LocationUpdater";

    private Location currentLocation;
    private OnLocationUpdateListener onLocationUpdateListener;

    private void getLastKnownLocation(LocationManager locationManager) throws SecurityException
    {
        Location bestRetrievedLocation = null;
        List<String> providers = locationManager.getProviders(true);

        for (String provider : providers)
        {
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null)
            {
                if ((bestRetrievedLocation == null) || (location.getAccuracy() < bestRetrievedLocation.getAccuracy()))
                {
                    bestRetrievedLocation = location;
                }
            }
        }

        if (bestRetrievedLocation != null)
        {
            Log.i(TAG, "Last known user's location has been retrieved: " + bestRetrievedLocation.getLatitude() + ", " + bestRetrievedLocation.getLongitude());

            setCurrentLocation(bestRetrievedLocation);
        }
    }

    private void requestLocationUpdates(LocationManager locationManager) throws SecurityException
    {
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
    }

    private void setCurrentLocation(Location location)
    {
        currentLocation = location;

        if (onLocationUpdateListener != null)
        {
            onLocationUpdateListener.onUpdate(location);
        }
    }

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    public LocationUpdater initializeLocationListening(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int accessFineLocationPermission = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessFineLocationPermission == PackageManager.PERMISSION_GRANTED)
            {
                getLastKnownLocation(locationManager);
                requestLocationUpdates(locationManager);
            }
            else
            {
                Log.e(TAG, "Permission denied to access user's location!");

                // TODO: Require premissions...
            }
        }
        else
        {
            getLastKnownLocation(locationManager);
            requestLocationUpdates(locationManager);
        }

        return this;
    }

    public LocationUpdater setOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListenerInstance)
    {
        onLocationUpdateListener = onLocationUpdateListenerInstance;

        return this;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (currentLocation == null)
        {
            currentLocation = location;
        }
        else if (currentLocation.getAccuracy() < location.getAccuracy())
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if ((currentLocation.getLatitude() != latitude) && (currentLocation.getLongitude() != longitude))
            {
                Log.i(TAG, "User location has been updated: " + latitude + ", " + longitude);

                setCurrentLocation(location);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    public interface OnLocationUpdateListener
    {
        void onUpdate(Location location);
    }
}
