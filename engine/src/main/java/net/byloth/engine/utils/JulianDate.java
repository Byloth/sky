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

    static public double toJulianCenturies(double julianDayTime)
    {
        return (julianDayTime - 2451545) / 36525;
    }
    static public double toJulianDay(double julianCenturies)
    {
        return (julianCenturies * 36525) + 2451545;
    }

    private double julianDay;
    private double julianTime;

    public JulianDate()
    {
        this(Calendar.getInstance());
    }
    public JulianDate(Calendar calendar)
    {
        double currentTimeZoneOffset = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 3600000;

        double year = calendar.get(Calendar.YEAR);
        double month = calendar.get(Calendar.MONTH) + 1;
        double day = calendar.get(Calendar.DAY_OF_MONTH);
        double hour = calendar.get(Calendar.HOUR_OF_DAY) - currentTimeZoneOffset;
        double minute = calendar.get(Calendar.MINUTE);
        double second = calendar.get(Calendar.SECOND);

        if (month > 2)
        {
            year -= 1;
            month += 12;
        }

        julianDay = Maths.roundDown(365.25f * (year + 4716)) + Maths.roundDown(30.6001f * (month + 1)) + day - 1524.5f;
        julianTime = (hour + ( minute + ( second / 60) / 60)) / 24;

        if (julianDay > 2299160)
        {
            double leap = Maths.roundDown(year / 100);

            julianDay += (2 - leap) + Maths.roundDown(leap / 4);
        }
    }

    public double getJulianDay()
    {
        return julianDay;
    }
    public double getJulianDayTime()
    {
        return (julianDay + julianTime);
    }
}
