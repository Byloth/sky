package net.byloth.engine.helpers;

import java.util.Random;

/**
 * Created by Matteo on 16/10/2015.
 */
final public class Randomize
{
    static final private Random random = new Random();

    static final public int FLOAT_PRECISION = 10000;

    private Randomize() { }

    static public boolean Boolean()
    {
        return random.nextBoolean();
    }
    static public boolean Boolean(float truePercentage)
    {
        float maxValue = 100 / truePercentage;

        if (Integer(maxValue) == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static int Integer(float maxValue)
    {
        int intMaxValue = (int) maxValue;

        if (intMaxValue == 0)
        {
            return 0;
        }
        else if (intMaxValue > 0)
        {
            return random.nextInt(intMaxValue);
        }
        else
        {
            return -random.nextInt(-intMaxValue);
        }
    }
    public static int Integer(float minValue, float maxValue)
    {
        if (minValue == maxValue)
        {
            return (int) minValue;
        }
        else if (minValue < maxValue)
        {
            maxValue -= minValue;

            return (int) (Integer((int) maxValue) + minValue);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public static float Float(float maxValue)
    {
        maxValue *= FLOAT_PRECISION;

        return ((float) Integer(maxValue)) / FLOAT_PRECISION;
    }
    public static float Float(float minValue, float maxValue)
    {
        if (minValue == maxValue)
        {
            return minValue;
        }
        else if (minValue < maxValue)
        {
            maxValue -= minValue;

            return Float(maxValue) + minValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public static float DegreesAngle()
    {
        return Float(Maths.MAX_DEGREES);
    }
    public static float RadiansAngle()
    {
        return Float(Maths.MAX_RADIANS);
    }
}
