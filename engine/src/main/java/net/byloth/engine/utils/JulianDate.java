package net.byloth.engine.utils;

import net.byloth.engine.helpers.Maths;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Matteo on 08/04/16.
 */
public class JulianDate
{
    static final private String TAG = "JulianDate";

    static public float computeJulianCentury(double julianDayTime)
    {
        return (((float) (julianDayTime)) - 2451545) / 36525;
    }

    float julianDay;
    float julianTime;

    public JulianDate()
    {
        this(Calendar.getInstance());
    }
    public JulianDate(Calendar calendar)
    {
        float currentTimeZoneOffset = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 3600000;

        float year = calendar.get(Calendar.YEAR);
        float month = calendar.get(Calendar.MONTH) + 1;
        float day = calendar.get(Calendar.DAY_OF_MONTH);
        float hour = calendar.get(Calendar.HOUR_OF_DAY) - currentTimeZoneOffset;
        float minute = calendar.get(Calendar.MINUTE);
        float second = calendar.get(Calendar.SECOND);

        if (month > 2)
        {
            year -= 1;
            month += 12;
        }

        julianDay = Maths.roundDown(365.25f * (year + 4716)) + Maths.roundDown(30.6001f * (month + 1)) + day - 1524.5f;
        julianTime = (hour + ( minute + ( second / 60) / 60)) / 24;

        if (julianDay > 2299160)
        {
            float leap = Maths.roundDown(year / 100);

            julianDay += (2 - leap) + Maths.roundDown(leap / 4);
        }
    }

    public float getJulianDay()
    {
        return julianDay;
    }
    public float getJulianDayTime()
    {
        return (julianDay + julianTime);
    }
}
