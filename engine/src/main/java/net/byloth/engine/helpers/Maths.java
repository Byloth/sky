package net.byloth.engine.helpers;

/**
 * Created by Matteo on 10/10/2015.
 */
final public class Maths
{
    static final public float MAX_DEGREES = 360;
    static final public float MAX_RADIANS = (float) (Math.PI * 2);

    static final public float PI = (float) Math.PI;

    private Maths() { }

    static public float adjustInRange(double value, double maximumValue)
    {
        return Maths.adjustInRange(value, 0, maximumValue, float.class);
    }
    static public float adjustInRange(double value, double minimumValue, double maximumValue)
    {
        return Maths.adjustInRange(value, minimumValue, maximumValue, float.class);
    }
    static public <NumericType extends Number> NumericType adjustInRange(double value, double maximumValue, Class<NumericType> returnType)
    {
        return Maths.adjustInRange(value, 0, maximumValue, returnType);
    }
    static public <NumericType extends Number> NumericType adjustInRange(double value, double minimumValue, double maximumValue, Class<NumericType> returnType)
    {
        if (minimumValue <= maximumValue)
        {
            if (value < minimumValue)
            {
                value += maximumValue;

                return returnType.cast(adjustInRange(value, minimumValue, maximumValue));
            }
            else if (value >= maximumValue)
            {
                value -= maximumValue;

                return returnType.cast(adjustInRange(value, minimumValue, maximumValue));
            }
            else
            {
                return returnType.cast(value);
            }
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    static public float arcSine(double value)
    {
        return (float) Math.asin(value);
    }
    static public float arcCosine(double value)
    {
        return (float) Math.acos(value);
    }
    static public float arcTangent(double value)
    {
        return (float) Math.atan(value);
    }
    static public float arcTangent(double coordinateX, double coordinateY)
    {
        return (float) Math.atan2(coordinateY, coordinateX);
    }

    static public int roundUp(double value)
    {
        return (int) Math.ceil(value);
    }
    static public int roundDown(double value)
    {
        return (int) Math.floor(value);
    }

    static public boolean isDivisible(double dividend, double divisor)
    {
        return ((dividend % divisor) == 0);
    }

    static public float sine(double radiansAngle)
    {
        return (float) Math.sin(radiansAngle);
    }
    static public float cosine(double radiansAngle)
    {
        return (float) Math.cos(radiansAngle);
    }
    static public float tangent(double radiansAngle)
    {
        return (float) Math.tan(radiansAngle);
    }

    static public float hypotenuse(double cathetus1, double cathetus2)
    {
        return Maths.hypotenuse(cathetus1, cathetus2, float.class);
    }
    static public <NumericType extends Number> NumericType hypotenuse(double cathetus1, double cathetus2, Class<NumericType> returnType)
    {
        return returnType.cast(Math.sqrt(Math.pow(cathetus1, 2) + Math.pow(cathetus2, 2)));
    }

    static public float proportion(double absoluteMaximum, double relativeMaximum, double absolutePartial)
    {
        return Maths.proportion(absoluteMaximum, relativeMaximum, absolutePartial, float.class);
    }
    static public <NumericType extends Number> NumericType proportion(double absoluteMaximum, double relativeMaximum, double absolutePartial, Class<NumericType> returnType)
    {
        if (absoluteMaximum != 0)
        {
            return returnType.cast((absolutePartial * relativeMaximum) / absoluteMaximum);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    static public float squareRoot(double value)
    {
        return (float) Math.sqrt(value);
    }

    static public float toDegrees(double radiansAngle)
    {
        return (float) Math.toDegrees(radiansAngle);
    }
    static public float toRadians(double degreesAngle)
    {
        return (float) Math.toRadians(degreesAngle);
    }
}
