package net.byloth.engine.utils;

import java.util.Calendar;

import net.byloth.engine.helpers.Maths;

/**
 * Created by Matteo on 11/10/2015.
 */
public class DayTime
{
    private long timeZoneOffset;

    static final public int MIN_VALUE = 0;
    static final public int MAX_VALUE = 86400000;

    static final public float DEGREES_UNIT = Maths.MAX_DEGREES / MAX_VALUE;
    static final public float RADIANS_UNIT = Maths.MAX_RADIANS / MAX_VALUE;

    public static String toString(double dayTimeValue)
    {
        dayTimeValue = dayTimeValue / 60000;

        int minutes = (int) (dayTimeValue % 60);
        dayTimeValue = dayTimeValue / 60;

        int hours = (int) (dayTimeValue); // % 24

        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }

    public DayTime()
    {
        this(Calendar.getInstance());
    }
    public DayTime(Calendar calendar)
    {
        timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
    }

    public int getMilliseconds()
    {
        Calendar now = Calendar.getInstance();

        return (int) ((now.getTimeInMillis() + timeZoneOffset) % MAX_VALUE);
    }

    public float toDegrees()
    {
        return getMilliseconds() * DEGREES_UNIT;
    }
    public float toRadians()
    {
        return getMilliseconds() * RADIANS_UNIT;
    }
}
