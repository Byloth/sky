package net.byloth.sky.updaters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import net.byloth.engine.helpers.Maths;
import net.byloth.sky.LiveWallpaper;

/**
 * Created by Matteo on 23/10/2015.
 */
public class SunUpdater extends BroadcastReceiver
{
    static final private int RISING_TIME = 6;
    static final private int SETTING_TIME = 18;

    static final public float OFFICIAL_ZENITH = 90.5f;
    static final public float CIVIL_ZENITH = 96;
    static final public float NAUTICAL_ZENITH = 102;
    static final public float ASTRONOMICAL_ZENITH = 108;
    
    static private int officialSunriseTime = 21827000;
    static private int civilSunriseTime = 20591000;
    static private int nauticalSunriseTime = 19151000;
    static private int astronomicalSunriseTime = 17711000;

    static private int officialSunsetTime = 65435000;
    static private int civilSunsetTime = 66671000;
    static private int nauticalSunsetTime = 68111000;
    static private int astronomicalSunsetTime = 69551000;
    
    private boolean isSet;
    
    private OnSunUpdate onSunUpdate;
    
    static public int getOfficialSunriseTime()
    {
        return officialSunriseTime;
    }
    static public int getCivilSunriseTime()
    {
        return civilSunriseTime;
    }
    static public int getNauticalSunriseTime()
    {
        return nauticalSunriseTime;
    }
    static public int getAstronomicalSunriseTime()
    {
        return astronomicalSunriseTime;
    }

    static public int getOfficialSunsetTime()
    {
        return officialSunsetTime;
    }
    static public int getCivilSunsetTime()
    {
        return civilSunsetTime;
    }
    static public int getNauticalSunsetTime()
    {
        return nauticalSunsetTime;
    }
    static public int getAstronomicalSunsetTime()
    {
        return astronomicalSunsetTime;
    }

    private void calculateDeclination(Bundle parametersBundle)
    {
        float approximateTime = parametersBundle.getFloat("approximate_time");

        float meanAnomaly = (0.9856f * approximateTime) - 3.289f;
        float trueLongitude = (meanAnomaly + (1.916f * Maths.sine(Maths.toRadians(meanAnomaly))) + (0.02f * Maths.sine(Maths.toRadians(2 * meanAnomaly))) + 282.634f) % 360;
        float rightAscension = Maths.toDegrees(Maths.arcTangent(0.91764f * Maths.tangent(Maths.toRadians(trueLongitude)))) % 360;
        float trueLongitudeQuadrant = Maths.roundDown(trueLongitude / 90) * 90;
        float rightAscensionQuadrant = Maths.roundDown(rightAscension / 90) * 90;

        rightAscension = (rightAscension + (trueLongitudeQuadrant - rightAscensionQuadrant)) / 15;

        float sineDeclination = 0.39782f * Maths.sine(Maths.toRadians(trueLongitude));
        float cosineDeclination = Maths.cosine(Maths.arcSine(sineDeclination));

        parametersBundle.putFloat("right_ascension", rightAscension);

        parametersBundle.putFloat("sine_declination", sineDeclination);
        parametersBundle.putFloat("cosine_declination", cosineDeclination);
    }

    private int calculateZenithTime(int timeType, float zenithType, Bundle inputBundle)
    {
        float timeZoneOffset = inputBundle.getInt("time_zone_offset");

        double latitude = inputBundle.getDouble("latitude");
        double longitude = inputBundle.getDouble("longitude");

        float approximateTime = inputBundle.getFloat("approximate_time");

        float rightAscension = inputBundle.getFloat("right_ascension");

        float sineDeclination = inputBundle.getFloat("sine_declination");
        float cosineDeclination = inputBundle.getFloat("cosine_declination");

        float localHourAngle = (Maths.cosine(Maths.toRadians(zenithType)) - (sineDeclination * Maths.sine(Maths.toRadians(latitude)))) / (cosineDeclination * Maths.cosine(Maths.toRadians(latitude)));

        if (localHourAngle > 1)
        {
            // Il sole non sorgerà mai...
        }
        else if (localHourAngle < -1)
        {
            // Il sole non tramonterà mai...
        }

        switch (timeType)
        {
            case RISING_TIME:
                localHourAngle = (Maths.MAX_DEGREES - Maths.toDegrees(Maths.arcCosine(localHourAngle))) / 15;
                break;

            case SETTING_TIME:
                localHourAngle = Maths.toDegrees(Maths.arcCosine(localHourAngle)) / 15;
                break;

            default:
                throw new IllegalArgumentException();
        }

        float localMeanTime = localHourAngle + rightAscension - (0.06571f * approximateTime) - 6.622f;
        float utcMeanTime = (float) ((localMeanTime - (longitude / 15)) % 24);

        int localTime = (int) ((utcMeanTime * 3600000) + timeZoneOffset);

        return localTime;
    }

    private void updateRisingTimes(Bundle inputBundle)
    {
        int dayOfYear = inputBundle.getInt("day_of_year");
        double longitude = inputBundle.getDouble("longitude");

        float approximateTime = (float) (dayOfYear + (RISING_TIME - (longitude / 15) / 24));

        Bundle parametersBundle = new Bundle();

        parametersBundle.putAll(inputBundle);
        parametersBundle.putFloat("approximate_time", approximateTime);

        calculateDeclination(parametersBundle);

        officialSunriseTime = calculateZenithTime(RISING_TIME, OFFICIAL_ZENITH, parametersBundle);
        civilSunriseTime = calculateZenithTime(RISING_TIME, CIVIL_ZENITH, parametersBundle);
        nauticalSunriseTime = calculateZenithTime(RISING_TIME, NAUTICAL_ZENITH, parametersBundle);
        astronomicalSunriseTime = calculateZenithTime(RISING_TIME, ASTRONOMICAL_ZENITH, parametersBundle);
    }
    private void updateSettingTimes(Bundle inputBundle)
    {
        int dayOfYear = inputBundle.getInt("day_of_year");
        double longitude = inputBundle.getDouble("longitude");

        float approximateTime = (float) (dayOfYear + (SETTING_TIME - (longitude / 15) / 24));

        Bundle parametersBundle = new Bundle();

        parametersBundle.putAll(inputBundle);
        parametersBundle.putFloat("approximate_time", approximateTime);

        calculateDeclination(parametersBundle);

        officialSunsetTime = calculateZenithTime(SETTING_TIME, OFFICIAL_ZENITH, parametersBundle);
        civilSunsetTime = calculateZenithTime(SETTING_TIME, CIVIL_ZENITH, parametersBundle);
        nauticalSunsetTime = calculateZenithTime(SETTING_TIME, NAUTICAL_ZENITH, parametersBundle);
        astronomicalSunsetTime = calculateZenithTime(SETTING_TIME, ASTRONOMICAL_ZENITH, parametersBundle);
    }

    public SunUpdater()
    {
        isSet = false;
    }

    public SunUpdater setAlarm(long repeatingInterval, Context context)
    {
        Intent intent = new Intent(context, SunUpdater.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), repeatingInterval, pendingIntent);

        isSet = true;

        Log.i(LiveWallpaper.APPLICATION_NAME, "SunUpdater's alarm has been set correctly!");

        return this;
    }

    public boolean isAlarmSet()
    {
        return isSet;
    }
    
    public SunUpdater setOnSunUpdate(OnSunUpdate onSunUpdateInstance)
    {
        onSunUpdate = onSunUpdateInstance;

        return this;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Calendar now = Calendar.getInstance();
        TimeZone timeZone = now.getTimeZone();

        Bundle parametersBundle = new Bundle();

        parametersBundle.putInt("day_oy_year", now.get(Calendar.DAY_OF_YEAR));
        parametersBundle.putInt("time_zone_offset", timeZone.getRawOffset());
        parametersBundle.putDouble("latitude", LocationUpdater.getLatitude());
        parametersBundle.putDouble("longitude", LocationUpdater.getLongitude());

        updateRisingTimes(parametersBundle);
        updateSettingTimes(parametersBundle);
        
        if (onSunUpdate != null)
        {
            Bundle sunUpdatedTimes = new Bundle();

            sunUpdatedTimes.putInt("official_sunrise_time", officialSunriseTime);
            sunUpdatedTimes.putInt("civil_sunrise_time", civilSunriseTime);
            sunUpdatedTimes.putInt("nautical_sunrise_time", nauticalSunriseTime);
            sunUpdatedTimes.putInt("astronomical_sunrise_time", astronomicalSunriseTime);

            sunUpdatedTimes.putInt("official_sunset_time", officialSunsetTime);
            sunUpdatedTimes.putInt("civil_sunset_time", civilSunsetTime);
            sunUpdatedTimes.putInt("nautical_sunset_time", nauticalSunsetTime);
            sunUpdatedTimes.putInt("astronomical_sunset_time", astronomicalSunsetTime);

            onSunUpdate.onUpdate(sunUpdatedTimes);
        }

        Log.i(LiveWallpaper.APPLICATION_NAME, "Today's rising / setting time have been updated!");
        Toast.makeText(context, "\t\tLe ore di alba e tramonto\nodierne sono state aggiornate!", Toast.LENGTH_LONG).show();
    }
    
    public interface OnSunUpdate
    {
        void onUpdate(Bundle sunUpdatedTimes);
    }
}
