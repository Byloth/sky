package net.byloth.sky;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import net.byloth.engine.utils.DayTime;
import net.byloth.engine.utils.SunTime;
import net.byloth.sky.components.DailyAlarmReceiver;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Matteo on 02/03/16.
 */
public class LiveWallpaper extends Application implements LocationListener
{
    static final private String TAG = "LiveWallpaper";

    static private LiveWallpaper currentInstance;

    private boolean isDailyAlarmSet;

    private Location currentLocation;
    private OnLocationUpdateListener onLocationUpdateListener;

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

        if (isDailyAlarmSet == false)
        {
            setDailyAlarm();
        }

        SunTime sunTime = new SunTime(location);

        Log.d(TAG, "Sunrise time: " + DayTime.toString(sunTime.getSunriseTime() * 60000));
        Log.d(TAG, "Noon time: " + DayTime.toString(sunTime.getNoonTime() * 60000));
        Log.d(TAG, "Sunset time: " + DayTime.toString(sunTime.getSunsetTime() * 60000));
    }

    private void setDailyAlarm()
    {
        Intent intent = new Intent(this, DailyAlarmReceiver.class);
        intent.setAction("net.byloth.sky.components.ALERT_EXPIRED");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        isDailyAlarmSet = true;

        Log.i(TAG, "Daily alarm has been set correctly!");
    }

    public LiveWallpaper()
    {
        currentInstance = this;

        isDailyAlarmSet = false;
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

    public Location getCurrentLocation()
    {
        return currentLocation;
    }

    public SharedPreferences getSharedPreferences()
    {
        return PreferenceManager.getDefaultSharedPreferences(this);
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