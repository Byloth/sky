package net.byloth.sky.updaters;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.byloth.engine.helpers.Maths;
import net.byloth.engine.utils.DayTime;
import net.byloth.engine.utils.SunTimes;
import net.byloth.sky.LiveWallpaper;

/**
 * Created by Matteo on 23/10/2015.
 */
public class SunTimesUpdater
{
    static final private String TAG = "SunTimesUpdater";

    static private int toMilliseconds(double decimalTimeInMinutes)
    {
        return ((int) (decimalTimeInMinutes * 60000));
    }

    private SunTimes sunTimes;
    private OnSunTimesUpdateListener onSunTimesUpdateListener;

    public SunTimesUpdater()
    {
        sunTimes = new SunTimes(new Location(TAG));
    }

    public int getNoonTime()
    {
        return toMilliseconds(sunTimes.getNoonTime());
    }

    public int getOfficialDawnTime()
    {
        return toMilliseconds(sunTimes.getOfficialDawnTime());
    }
    public int getCivilDawnTime()
    {
        return toMilliseconds(sunTimes.getCivilDawnTime());
    }
    public int getNauticalDawnTime()
    {
        return toMilliseconds(sunTimes.getNauticalDawnTime());
    }
    public int getAstronomicalDawnTime()
    {
        return toMilliseconds(sunTimes.getAstronomicalDawnTime());
    }

    public int getOfficialSunsetTime()
    {
        return toMilliseconds(sunTimes.getOfficialSunsetTime());
    }
    public int getCivilSunsetTime()
    {
        return toMilliseconds(sunTimes.getCivilSunsetTime());
    }
    public int getNauticalSunsetTime()
    {
        return toMilliseconds(sunTimes.getNauticalSunsetTime());
    }
    public int getAstronomicalSunsetTime()
    {
        return toMilliseconds(sunTimes.getAstronomicalSunsetTime());
    }

    public void setOnSunTimesUpdateListener(OnSunTimesUpdateListener onSunTimesUpdateListenerInstance)
    {
        onSunTimesUpdateListener = onSunTimesUpdateListenerInstance;
    }

    public boolean updateSunTimes(Location currentLocation)
    {
        if (currentLocation != null)
        {
            sunTimes = new SunTimes(currentLocation);

            if (onSunTimesUpdateListener != null)
            {
                onSunTimesUpdateListener.onUpdate(this);
            }

            Log.i(TAG, "Today's rising / setting times have been updated!");

            Log.d(TAG, "Official dawn time: " + DayTime.toString(getOfficialDawnTime()));
            Log.d(TAG, "Civil dawn time: " + DayTime.toString(getCivilDawnTime()));
            Log.d(TAG, "Nautical dawn time: " + DayTime.toString(getNauticalDawnTime()));
            Log.d(TAG, "Astronomical dawn time: " + DayTime.toString(getAstronomicalDawnTime()));

            Log.d(TAG, "Noon time: " + DayTime.toString(getNoonTime()));

            Log.d(TAG, "Official sunset time: " + DayTime.toString(getOfficialSunsetTime()));
            Log.d(TAG, "Civil sunset time: " + DayTime.toString(getCivilSunsetTime()));
            Log.d(TAG, "Nautical sunset time: " + DayTime.toString(getNauticalSunsetTime()));
            Log.d(TAG, "Astronomical sunset time: " + DayTime.toString(getAstronomicalSunsetTime()));

            return true;
        }
        else
        {
            Log.e(TAG, "Cannot update today's rising / setting times: current user's location is not initialized!");

            return false;
        }
    }

    public interface OnSunTimesUpdateListener
    {
        void onUpdate(SunTimesUpdater sunTimesUpdaterSender);
    }
}
