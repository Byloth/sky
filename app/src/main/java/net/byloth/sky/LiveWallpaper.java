package net.byloth.sky;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import net.byloth.sky.components.DailyAlarmReceiver;
import net.byloth.sky.updaters.LocationUpdater;
import net.byloth.sky.updaters.SunTimesUpdater;

/**
 * Created by Matteo on 02/03/16.
 */
public class LiveWallpaper extends Application implements LocationUpdater.OnLocationUpdateListener
{
    static final private String TAG = "LiveWallpaper";

    static private LiveWallpaper currentInstance;

    private boolean isDailyAlarmSet;

    private LocationUpdater locationUpdater;
    private SunTimesUpdater sunTimesUpdater;

    static public LiveWallpaper getInstance()
    {
        return currentInstance;
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

        locationUpdater = new LocationUpdater();
        sunTimesUpdater = new SunTimesUpdater();
    }

    public LocationUpdater getLocationUpdater()
    {
        return locationUpdater;
    }

    public SharedPreferences getSharedPreferences()
    {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SunTimesUpdater getSunTimesUpdater()
    {
        return sunTimesUpdater;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        locationUpdater.initializeLocationListening(this);
        locationUpdater.setOnLocationUpdateListener(this);
    }

    @Override
    public void onUpdate(Location location)
    {
        if (isDailyAlarmSet == false)
        {
            setDailyAlarm();
        }
    }
}