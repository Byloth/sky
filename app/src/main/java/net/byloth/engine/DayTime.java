package net.byloth.engine;

import net.byloth.engine.helpers.Maths;

import java.util.Calendar;

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

    public DayTime()
    {
        Calendar rightNow = Calendar.getInstance();

        timeOffset = rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET);
    }

    public int getMilliseconds()
    {
        Calendar rightNow = Calendar.getInstance();

        return (int) ((rightNow.getTimeInMillis() + timeOffset) % MAX_VALUE);
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
