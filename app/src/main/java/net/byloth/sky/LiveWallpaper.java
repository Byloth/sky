package net.byloth.sky;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import net.byloth.sky.components.DailyUpdater;

import java.util.List;

/**
 * Created by Matteo on 02/03/16.
 */
public class LiveWallpaper extends Application implements LocationListener
{
    static final public String APPLICATION_NAME = "SkyLiveWallpaper";

    static private LiveWallpaper currentInstance;

    private Location currentLocation;
    private OnLocationUpdateListener onLocationUpdateListener;

    private DailyUpdater dailyUpdater;

    static public LiveWallpaper getInstance()
    {
        return currentInstance;
    }

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
            Log.i(APPLICATION_NAME, "Last known user's location has been retrieved: " + bestRetrievedLocation.getLongitude() + ", " + bestRetrievedLocation.getLatitude());

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

        if (dailyUpdater.isSet() == false)
        {
            dailyUpdater.setAlarm(this);
        }
    }

    public LiveWallpaper()
    {
        currentInstance = this;

        dailyUpdater = new DailyUpdater();
    }

    public LiveWallpaper initializeLocationListening()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int accessFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessFineLocationPermission == PackageManager.PERMISSION_GRANTED)
            {
                getLastKnownLocation(locationManager);
                requestLocationUpdates(locationManager);
            }
            else
            {
                Log.e(APPLICATION_NAME, "Permission denied to access user's location!");

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

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    public void setOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListenerInstance)
    {
        onLocationUpdateListener = onLocationUpdateListenerInstance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        initializeLocationListening();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (currentLocation.getAccuracy() < location.getAccuracy())
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if ((currentLocation.getLatitude() != latitude) && (currentLocation.getLongitude() != longitude))
            {
                Log.i(APPLICATION_NAME, "User location has been updated: " + latitude + ", " + longitude);

                setCurrentLocation(location);

                if (onLocationUpdateListener != null)
                {
                    onLocationUpdateListener.onUpdate(location);
                }
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