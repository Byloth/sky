package net.byloth.engine.graphics;

import android.util.Log;

import net.byloth.engine.utils.DayTime;
import net.byloth.engine.graphics.helpers.ColorsShaders;
import net.byloth.engine.helpers.Maths;

/**
 * Created by Matteo on 10/10/2015.
 */
public class TimedShader
{
    static final private String TAG = "TimedShader";

    private Color currentColor;

    private TimedColor[] timedColors;

    public TimedShader(TimedColor[] timedColorsValues)
    {
        if (timedColorsValues.length > 1)
        {
            currentColor = new Color();

            timedColors = timedColorsValues;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    private int getStartingColorIndex(float currentTime)
    {
        int index = -1;

        while ((index + 1) < timedColors.length)
        {
            if (timedColors[index + 1].getTime() < currentTime)
            {
                index += 1;
            }
            else
            {
                if (index < 0)
                {
                    return timedColors.length - 1;
                }
                else
                {
                    return index;
                }
            }
        }

        return index;
    }

    public Color getCurrentColor()
    {
        return currentColor;
    }

    public Color updateCurrentColor(int currentTime)
    {
        boolean endToBeginning = false;

        int firstIndex = getStartingColorIndex(currentTime);
        int secondIndex = firstIndex + 1;

        TimedColor firstTimedColor;
        TimedColor secondTimedColor;

        if (secondIndex >= timedColors.length)
        {
            secondIndex = 0;
            endToBeginning = true;
        }

        firstTimedColor = timedColors[firstIndex];
        secondTimedColor = timedColors[secondIndex];

        if ((firstTimedColor.equals(secondTimedColor) == true) || (firstTimedColor.getTime() == currentTime))
        {
            currentColor = firstTimedColor.getColor();

            if ((currentColor == null) ||
                    (currentColor.getAlpha() == 0) ||
                    (currentColor.getRed() == 0) ||
                    (currentColor.getGreen() == 0) ||
                    (currentColor.getBlue() == 0))
            {
                Log.e(TAG, "Color isn't valid!");
            }
        }
        else
        {
            int totalTimeSpan = secondTimedColor.getTime() - firstTimedColor.getTime();
            int currentTimeSpan = currentTime - firstTimedColor.getTime();

            double shadingRatio;

            if (endToBeginning == true)
            {
                totalTimeSpan = DayTime.MAX_VALUE + totalTimeSpan;

                if (currentTimeSpan < 0)
                {
                    currentTimeSpan = DayTime.MAX_VALUE + currentTimeSpan;
                }
            }

            shadingRatio = Maths.proportion(totalTimeSpan, ColorsShaders.MAX_SHADING_RATIO, currentTimeSpan);

            currentColor = ColorsShaders.alphaBlend(firstTimedColor.getColor(), shadingRatio, secondTimedColor.getColor());

            if ((currentColor == null) ||
                    (currentColor.getAlpha() == 0) ||
                    (currentColor.getRed() == 0) ||
                    (currentColor.getGreen() == 0) ||
                    (currentColor.getBlue() == 0))
            {
                Log.e(TAG, "Color isn't valid!");
            }
        }

        return currentColor;
    }
    public Color updateCurrentColor(DayTime currentDayTime)
    {
        return updateCurrentColor(currentDayTime.getMilliseconds());
    }
}
