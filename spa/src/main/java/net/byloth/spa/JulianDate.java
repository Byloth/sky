package net.byloth.spa;

import net.byloth.engine.helpers.Maths;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Matteo on 08/04/16.
 */
public class JulianDate
{
    static final private String TAG = "JulianDate";

    double julianDay;

    private double getJulianEphemerisDay(double timeDelta)
    {
        return (julianDay + timeDelta) / 86400.0d;
    }

    private double getJulianEphemerisCentury(double timeDelta)
    {
        return (getJulianEphemerisDay(timeDelta) - 2451545.0d) / 36525.0d;
    }

    public JulianDate()
    {
        this(Calendar.getInstance());
    }

    public JulianDate(Calendar calendar)
    {
        TimeZone timeZone = calendar.getTimeZone();

        int currentTimeZoneOffset = (timeZone.getRawOffset() + timeZone.getDSTSavings()) / 3600000;

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY) - currentTimeZoneOffset;
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        if (month > 2)
        {
            year -= 1;
            month += 12;
        }

        double decimalDayTime = day + ((hour + (minute + (second / 60.0d)) / 60.0d) / 24.0d);

        julianDay = Maths.roundDown(365.25d * (year + 4716)) + Maths.roundDown(30.6001d * (month + 1)) + decimalDayTime - 1524.5d;

        if (julianDay > 2299160)
        {
            double a = Maths.roundDown(((double) year) / 100.0d);

            julianDay += (2 - a) + Maths.roundDown(a / 4);
        }

        // julianEphemerisMillennium = getJulianEphemerisCentury(timeDelta) / 10.0d; // TODO: Time Delta = 0?
    }

    public float getJulianDay()
    {
        return ((float) julianDay);
    }

    public float getJulianCentury()
    {
        return ((float) ((julianDay - 2451545.0d) / 36525.0d));
    }

    public float getJulianEphemerisMillennium(double timeDelta)
    {
        return ((float) (getJulianEphemerisCentury(timeDelta) / 10.0d));
    }
}
