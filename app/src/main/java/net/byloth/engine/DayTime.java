package net.byloth.engine;

import java.util.Calendar;

import net.byloth.engine.helpers.Maths;

/**
 * Created by Matteo on 11/10/2015.
 */
public class DayTime
{
    private long timeOffset;

    static final public int MIN_VALUE = 0;
    static final public int MAX_VALUE = 86400000;

    static final public float DEGREES_UNIT = Maths.MAX_DEGREES / MAX_VALUE;
    static final public float RADIANS_UNIT = Maths.MAX_RADIANS / MAX_VALUE;

    public static String toString(int dayTimeValue)
    {
        dayTimeValue = dayTimeValue / 60000;

        int minutes = dayTimeValue % 60;
        dayTimeValue = dayTimeValue / 60;

        int hours = dayTimeValue; // % 24

        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }

    public DayTime()
    {
        Calendar now = Calendar.getInstance();

        timeOffset = now.get(Calendar.ZONE_OFFSET) + now.get(Calendar.DST_OFFSET);
    }

    public int getMilliseconds()
    {
        Calendar now = Calendar.getInstance();

        return (int) ((now.getTimeInMillis() + timeOffset) % MAX_VALUE);
    }

    public float toDegrees()
    {
        return getMilliseconds() * DEGREES_UNIT;
    }
    public float toRadians()
    {
        return getMilliseconds() * RADIANS_UNIT;
    }

    /*
     * TODO: Remove this funckin' method!
     */
    public void addMilliseconds(int incrementValue)
    {
        timeOffset = (timeOffset + incrementValue) % (MAX_VALUE + 1);
    }
}
