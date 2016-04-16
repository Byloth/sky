package net.byloth.engine.helpers;

import java.security.InvalidParameterException;

/**
 * Created by Matteo on 10/10/2015.
 */
final public class Maths
{
    static final public int DEGREES = 1;
    static final public int RADIANS = 2;

    static final public double MAX_DEGREES = 360;
    static final public double MAX_RADIANS = (Math.PI * 2);

    private Maths() { }

    static private double checkInputType(double value, int inputType)
    {
        if (inputType == DEGREES)
        {
            return Math.toRadians(value);
        }
        else if (inputType == RADIANS)
        {
            return value;
        }
        else
        {
            throw new InvalidParameterException();
        }
    }

    static private double checkReturnType(double value, int returnType)
    {
        if (returnType == DEGREES)
        {
            return Math.toDegrees(value);
        }
        else if (returnType == RADIANS)
        {
            return value;
        }
        else
        {
            throw new InvalidParameterException();
        }
    }

    static public double adjustInRange(double value, double maximumValue)
    {
        return Maths.adjustInRange(value, 0, maximumValue);
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

    static public double arcSine(double value)
    {
        return Maths.arcSine(value, RADIANS);
    }
    static public double arcSine(double value, int returnType)
    {
        double arcSine = Math.asin(value);

        return checkReturnType(arcSine, returnType);
    }

    static public double arcCosine(double value)
    {
        return arcCosine(value, RADIANS);
    }
    static public double arcCosine(double value, int returnType)
    {
        double arcCosine = Math.acos(value);

        return checkReturnType(arcCosine, returnType);
    }

    static public double arcTangent(double value)
    {
        return arcTangent(value, RADIANS);
    }
    static public double arcTangent(double value, int returnType)
    {
        double arcTangent = Math.atan(value);

        return checkReturnType(arcTangent, returnType);
    }

    static public double arcTangent(double coordinateX, double coordinateY)
    {
        return arcTangent(coordinateX, coordinateY, RADIANS);
    }
    static public double arcTangent(double coordinateX, double coordinateY, int returnType)
    {
        double arcTangent = Math.atan2(coordinateY, coordinateX);

        return checkReturnType(arcTangent, returnType);
    }

    static public double hypotenuse(double cathetus1, double cathetus2)
    {
        return Math.sqrt(Math.pow(cathetus1, 2) + Math.pow(cathetus2, 2));
    }

    static public boolean isDivisible(double dividend, double divisor)
    {
        return ((dividend % divisor) == 0);
    }

    static public int roundUp(double value)
    {
        return (int) Math.ceil(value);
    }
    static public int roundDown(double value)
    {
        return (int) Math.floor(value);
    }

    static public double sine(double radiansAngle)
    {
        return sine(radiansAngle, RADIANS);
    }
    static public double sine(double value, int inputType)
    {
        double angle = checkInputType(value, inputType);

        return Math.sin(angle);
    }

    static public double cosine(double radiansAngle)
    {
        return cosine(radiansAngle, RADIANS);
    }
    static public double cosine(double value, int inputType)
    {
        double angle = checkInputType(value, inputType);

        return Math.cos(angle);
    }

    static public double tangent(double radiansAngle)
    {
        return tangent(radiansAngle, RADIANS);
    }
    static public double tangent(double value, int inputType)
    {
        double angle = checkInputType(value, inputType);

        return Math.tan(angle);
    }
    static public double polynomialFunction(double[] values, double x)
    {
        double value = values[0];

        for (int index = 1; index < values.length; index += 1)
        {
            value = (value * x) + values[index];
        }

        return value;
    }

    static public double proportion(double absoluteMaximum, double relativeMaximum, double absolutePartial)
    {
        if (absoluteMaximum != 0)
        {
            return (absolutePartial * relativeMaximum) / absoluteMaximum;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}
