package net.byloth.engine.helpers;

/**
 * Created by Matteo on 09/04/2016.
 */
final public class HighPrecisionMaths
{
    static final public double MAX_RADIANS = 2 * Math.PI;

    private HighPrecisionMaths() { }

    static public double adjustInRange(double value, double maximumValue)
    {
        return HighPrecisionMaths.adjustInRange(value, 0, maximumValue);
    }
    static public double adjustInRange(double value, double minimumValue, double maximumValue)
    {
        if (minimumValue <= maximumValue)
        {
            if (value < minimumValue)
            {
                value += maximumValue;

                return adjustInRange(value, minimumValue, maximumValue);
            }
            else if (value >= maximumValue)
            {
                value -= maximumValue;

                return adjustInRange(value, minimumValue, maximumValue);
            }
            else
            {
                return value;
            }
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    static public double hypotenuse(double cathetus1, double cathetus2)
    {
        return Math.sqrt(Math.pow(cathetus1, 2) + Math.pow(cathetus2, 2));
    }

    static public double proportion(double absoluteMaximum, double relativeMaximum, double absolutePartial)
    {
        if (absoluteMaximum != 0)
        {
            return ((absolutePartial * relativeMaximum) / absoluteMaximum);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
