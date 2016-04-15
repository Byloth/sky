package net.byloth.engine.helpers;

import java.util.Random;

/**
 * Created by Matteo on 16/10/2015.
 */
final public class Randomize
{
    static final private int DECIMAL_PRECISION = 1000000000;

    static final private String TAG = "Randomize";
    static final private Random RANDOM = new Random();

    private Randomize() { }

    static public boolean Boolean()
    {
        return RANDOM.nextBoolean();
    }
    static public boolean Boolean(double truePercentage)
    {
        if (truePercentage == 0)
        {
            return false;
        }
        else // if (truePercentage > 0) // TODO: What if (truePercentage < 0)?
        {
            double maxValue = 100 / truePercentage;

            return (Integer(maxValue) == 0);
        }
    }

    public static int Integer(double maxValue)
    {
        int intMaxValue = (int) maxValue;

        if (intMaxValue == 0)
        {
            return 0;
        }
        else if (intMaxValue > 0)
        {
            return RANDOM.nextInt(intMaxValue);
        }
        else
        {
            return -RANDOM.nextInt(-intMaxValue);
        }
    }
    public static int Integer(double minValue, double maxValue)
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

    public static double Decimal(double maxValue)
    {
        if (maxValue == 0)
        {
            return 0;
        }
        else if (maxValue > 0)
        {
            double decimalPart = maxValue;

            int integerPart = Maths.roundDown(maxValue) - 1;

            if (integerPart > 0)
            {
                decimalPart -= integerPart;

                integerPart = Integer(integerPart + 1);
            }
            else
            {
                integerPart = 0;
            }

            decimalPart = ((double) Integer(decimalPart * DECIMAL_PRECISION)) / DECIMAL_PRECISION;

            return (decimalPart + integerPart);
        }
        else
        {
            return -Decimal(-maxValue);
        }
    }
    public static double Decimal(double minValue, double maxValue)
    {
        if (minValue == maxValue)
        {
            return minValue;
        }
        else if (minValue < maxValue)
        {
            maxValue -= minValue;

            return Decimal(maxValue) + minValue;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public static double DegreesAngle()
    {
        return Decimal(Maths.MAX_DEGREES);
    }
    public static double RadiansAngle()
    {
        return Decimal(Maths.MAX_RADIANS);
    }
}
