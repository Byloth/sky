package net.byloth.sky.updaters;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

import net.byloth.engine.helpers.Maths;
import net.byloth.sky.LiveWallpaper;

/**
 * Created by Matteo on 23/10/2015.
 */
final public class SunTimesUpdater
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

    static private OnSunTimesUpdateListener onSunTimesUpdateListener;
    
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

    static private void calculateDeclination(Bundle parametersBundle)
    {
        float approximateTime = parametersBundle.getFloat("approximate_time");

        float meanAnomaly = (0.9856f * approximateTime) - 3.289f;
        float trueLongitude = Maths.adjustInRange(meanAnomaly + (1.916f * Maths.sine(Maths.toRadians(meanAnomaly))) + (0.02f * Maths.sine(Maths.toRadians(2 * meanAnomaly))) + 282.634f, 360);
        float rightAscension = Maths.adjustInRange(Maths.toDegrees(Maths.arcTangent(0.91764f * Maths.tangent(Maths.toRadians(trueLongitude)))), 360);
        float trueLongitudeQuadrant = Maths.roundDown(trueLongitude / 90) * 90;
        float rightAscensionQuadrant = Maths.roundDown(rightAscension / 90) * 90;

        rightAscension = (rightAscension + (trueLongitudeQuadrant - rightAscensionQuadrant)) / 15;

        float sineDeclination = 0.39782f * Maths.sine(Maths.toRadians(trueLongitude));
        float cosineDeclination = Maths.cosine(Maths.arcSine(sineDeclination));

        parametersBundle.putFloat("right_ascension", rightAscension);

        parametersBundle.putFloat("sine_declination", sineDeclination);
        parametersBundle.putFloat("cosine_declination", cosineDeclination);
    }

    static private int calculateZenithTime(int timeType, float zenithType, Bundle inputBundle)
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
            Log.i(LiveWallpaper.APPLICATION_NAME, "Today, the Sun will never rising...");
        }
        else if (localHourAngle < -1)
        {
            Log.i(LiveWallpaper.APPLICATION_NAME, "Today, the Sun will never setting...");
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

        float utcMeanTime = Maths.adjustInRange(localMeanTime - (longitude / 15), 24);

        return (int) ((utcMeanTime * 3600000) + timeZoneOffset);
    }

    static private Bundle updateRisingTimes(Bundle inputBundle)
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

        Bundle risingTimeValues = new Bundle();

        risingTimeValues.putInt("official_sunrise_time", officialSunriseTime);
        risingTimeValues.putInt("civil_sunrise_time", civilSunriseTime);
        risingTimeValues.putInt("nautical_sunrise_time", nauticalSunriseTime);
        risingTimeValues.putInt("astronomical_sunrise_time", astronomicalSunriseTime);

        return risingTimeValues;
    }
    static private Bundle updateSettingTimes(Bundle inputBundle)
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

        Bundle settingTimeValues = new Bundle();

        settingTimeValues.putInt("official_sunset_time", officialSunsetTime);
        settingTimeValues.putInt("civil_sunset_time", civilSunsetTime);
        settingTimeValues.putInt("nautical_sunset_time", nauticalSunsetTime);
        settingTimeValues.putInt("astronomical_sunset_time", astronomicalSunsetTime);

        return settingTimeValues;
    }

    static public void setOnSunTimesUpdateListener(OnSunTimesUpdateListener onSunTimesUpdateListenerInstance)
    {
        onSunTimesUpdateListener = onSunTimesUpdateListenerInstance;
    }

    static public void updateSunTimes()
    {
        Calendar now = Calendar.getInstance();
        TimeZone timeZone = now.getTimeZone();

        Location currentLocation = LiveWallpaper.getInstance().getCurrentLocation();

        Bundle parametersBundle = new Bundle();

        parametersBundle.putInt("day_of_year", now.get(Calendar.DAY_OF_YEAR));
        parametersBundle.putInt("time_zone_offset", timeZone.getRawOffset());

        parametersBundle.putDouble("latitude", currentLocation.getLatitude());
        parametersBundle.putDouble("longitude", currentLocation.getLongitude());

        Bundle risingTimeValues = updateRisingTimes(parametersBundle);
        Bundle settingTimeValues = updateSettingTimes(parametersBundle);

        if (onSunTimesUpdateListener != null)
        {
            onSunTimesUpdateListener.onUpdate(risingTimeValues, settingTimeValues);
        }
    }

    private SunTimesUpdater() { }

    public interface OnSunTimesUpdateListener
    {
        void onUpdate(Bundle risingTimeValues, Bundle settingTimeValues);
    }
}
