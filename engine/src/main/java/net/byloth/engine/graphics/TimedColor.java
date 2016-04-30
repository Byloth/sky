package net.byloth.engine.graphics;

import net.byloth.engine.utils.DayTime;

/**
 * Created by Matteo on 10/10/2015.
 */
public class TimedColor
{
    private int time;
    private Color color;

    public TimedColor()
    {
        time = DayTime.MIN_VALUE;

        color = new Color();
    }
    public TimedColor(int timeValue, Color colorValue)
    {
        setTime(timeValue);

        color = colorValue;
    }

    public int getTime()
    {
        return time;
    }
    public TimedColor setTime(double timeValue)
    {
        if ((timeValue >= DayTime.MIN_VALUE) && (timeValue < DayTime.MAX_VALUE))
        {
            time = (int) timeValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public Color getColor()
    {
        return color;
    }
    public TimedColor setColor(Color colorValue)
    {
        color = colorValue;

        return this;
    }


    @Override
    public boolean equals(Object otherTimedColor)
    {
        if ((otherTimedColor instanceof TimedColor) == true)
        {
            TimedColor timedColor = (TimedColor) otherTimedColor;

            if (time == timedColor.getTime())
            {
                if (color.equals(timedColor.getColor()) == true)
                {
                    return true;
                }
            }
        }

        return false;
    }
}