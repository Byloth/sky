package net.byloth.engine.graphics;

import net.byloth.engine.DayTime;
import net.byloth.engine.graphics.helpers.ColorsShaders;
import net.byloth.engine.helpers.Maths;

/**
 * Created by Matteo on 10/10/2015.
 */
public class TimedShader
{
    private TimedColor[] timedColors;

    public TimedShader(TimedColor[] timedColors)
    {
        if (timedColors.length > 1)
        {
            this.timedColors = timedColors;
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

    public Color getCurrentColor(int currentTime)
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
            return firstTimedColor.getColor();
        }
        else
        {
            float totalTimeSpan = secondTimedColor.getTime() - firstTimedColor.getTime();
            float currentTimeSpan = currentTime - firstTimedColor.getTime();

            float shadingRatio;

            if (endToBeginning == true)
            {
                totalTimeSpan = DayTime.MAX_VALUE + totalTimeSpan;

                if (currentTimeSpan < 0)
                {
                    currentTimeSpan = DayTime.MAX_VALUE + currentTimeSpan;
                }
            }

            shadingRatio = Maths.proportion(totalTimeSpan, ColorsShaders.MAX_SHADING_RATIO, currentTimeSpan);

            return ColorsShaders.alphaBlend(firstTimedColor.getColor(), shadingRatio, secondTimedColor.getColor());
        }
    }
}
